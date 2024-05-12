package com.nikolabojanic.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.nikolabojanic.dto.AuthDtoRequest;
import com.nikolabojanic.dto.AuthDtoResponse;
import com.nikolabojanic.dto.RegistrationResponseDto;
import com.nikolabojanic.dto.TraineeRegistrationRequestDto;
import com.nikolabojanic.dto.TrainerRegistrationRequestDto;
import com.nikolabojanic.dto.TrainerWorkloadResponseDto;
import com.nikolabojanic.dto.TrainingRequestDto;
import com.nikolabojanic.dto.TrainingResponseDto;
import com.nikolabojanic.dto.TrainingTypeRequestDto;
import com.nikolabojanic.dto.TrainingTypeResponseDto;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

public class MicroserviceTest {
    private static final String GET_WORKLOAD_URI = "http://localhost:8081/api/v1/trainers/{username}";
    private static final String MAIN_SERVICE_URI = "http://localhost:8080/api/v1/";

    @Test
    void microserviceIT() {
        //arrange
        RegistrationResponseDto traineeRegistrationResponseDto = registerTrainee();
        AuthDtoResponse authDtoResponse = loginUser(traineeRegistrationResponseDto);
        String jwt = "Bearer " + authDtoResponse.getAccessToken();
        TrainingTypeResponseDto trainingTypeResponseDto = createTrainingType(jwt);
        TrainerRegistrationRequestDto trainerRegistrationRequestDto = new TrainerRegistrationRequestDto(
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            trainingTypeResponseDto.getId()
        );
        RegistrationResponseDto trainerRegistrationResponseDto = registerTrainer(trainerRegistrationRequestDto);
        TrainingRequestDto trainingRequestDto = new TrainingRequestDto(
            traineeRegistrationResponseDto.getUsername(),
            trainerRegistrationResponseDto.getUsername(),
            RandomStringUtils.randomAlphabetic(5),
            LocalDate.now(),
            Double.parseDouble(RandomStringUtils.randomNumeric(5))
        );
        //act
        TrainingResponseDto trainingResponseDto = createTraining(jwt, trainingRequestDto);
        await().atMost(10_000, TimeUnit.MILLISECONDS).untilAsserted(() -> {
            //assert
            validateAddTraining(
                jwt,
                trainerRegistrationResponseDto,
                trainerRegistrationRequestDto,
                trainingRequestDto
            );
        });
        //act
        deleteTraining(jwt, trainingResponseDto);
        //assert
        validateDeleteTraining(jwt, trainerRegistrationResponseDto);
    }

    private void validateDeleteTraining(String jwt, RegistrationResponseDto trainerRegistrationResponseDto) {
        TrainerWorkloadResponseDto deleteResponseDto =
            given()
                .header("Authorization", jwt)
                .pathParam("username", trainerRegistrationResponseDto.getUsername())
                .when()
                .get(GET_WORKLOAD_URI)
                .then()
                .statusCode(200)
                .extract().as(TrainerWorkloadResponseDto.class);
        assertThat(deleteResponseDto.getYears()).isEmpty();
    }

    private void deleteTraining(String jwt, TrainingResponseDto trainingResponseDto) {
        given()
            .contentType("application/json")
            .header("Authorization", jwt)
            .pathParam("id", trainingResponseDto.getId())
            .when()
            .delete(MAIN_SERVICE_URI + "trainings/{id}");
    }

    private void validateAddTraining(
        String jwt,
        RegistrationResponseDto trainerRegistrationResponseDto,
        TrainerRegistrationRequestDto trainerRegistrationRequestDto,
        TrainingRequestDto trainingRequestDto) {
        TrainerWorkloadResponseDto responseDto =
            given()
                .header("Authorization", jwt)
                .pathParam("username", trainerRegistrationResponseDto.getUsername())
                .when()
                .get(GET_WORKLOAD_URI)
                .then()
                .statusCode(200)
                .extract().as(TrainerWorkloadResponseDto.class);
        //assert
        assertThat(responseDto.getUsername()).isEqualTo(trainerRegistrationResponseDto.getUsername());
        assertThat(responseDto.getFirstName()).isEqualTo(trainerRegistrationRequestDto.getFirstName());
        assertThat(responseDto.getLastName()).isEqualTo(trainerRegistrationRequestDto.getLastName());
        assertThat(responseDto.getYears().get(0).getYear()).isEqualTo(trainingRequestDto.getDate().getYear());
        assertThat(responseDto.getYears().get(0).getMonths().get(0).getMonth())
            .isEqualTo(trainingRequestDto.getDate().getMonth());
    }

    private TrainingResponseDto createTraining(String jwt, TrainingRequestDto trainingRequestDto) {
        return given()
            .contentType("application/json")
            .header("Authorization", jwt)
            .body(trainingRequestDto)
            .when()
            .post(MAIN_SERVICE_URI + "trainings")
            .then()
            .extract().as(TrainingResponseDto.class);
    }

    private RegistrationResponseDto registerTrainee() {
        TraineeRegistrationRequestDto traineeRegistrationRequestDto = new TraineeRegistrationRequestDto(
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            LocalDate.now(),
            RandomStringUtils.randomAlphabetic(10)
        );
        return given()
            .contentType("application/json")
            .body(traineeRegistrationRequestDto)
            .when()
            .post(MAIN_SERVICE_URI + "trainees")
            .then()
            .extract().as(RegistrationResponseDto.class);
    }

    private AuthDtoResponse loginUser(RegistrationResponseDto traineeRegistrationResponseDto) {
        AuthDtoRequest authDtoRequest = new AuthDtoRequest(
            traineeRegistrationResponseDto.getUsername(),
            traineeRegistrationResponseDto.getPassword()
        );
        return given()
            .contentType("application/json")
            .body(authDtoRequest)
            .when()
            .post(MAIN_SERVICE_URI + "users/login")
            .then()
            .extract().as(AuthDtoResponse.class);
    }

    private TrainingTypeResponseDto createTrainingType(String jwt) {
        TrainingTypeRequestDto trainingTypeRequestDto = new TrainingTypeRequestDto(
            RandomStringUtils.randomAlphabetic(5)
        );
        return given()
            .contentType("application/json")
            .header("Authorization", jwt)
            .body(trainingTypeRequestDto)
            .when()
            .post(MAIN_SERVICE_URI + "training-types")
            .then()
            .extract().as(TrainingTypeResponseDto.class);
    }

    private RegistrationResponseDto registerTrainer(TrainerRegistrationRequestDto trainerRegistrationRequestDto) {
        return given()
            .contentType("application/json")
            .body(trainerRegistrationRequestDto)
            .when()
            .post(MAIN_SERVICE_URI + "trainers")
            .then()
            .extract().as(RegistrationResponseDto.class);
    }
}