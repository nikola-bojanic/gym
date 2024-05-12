package com.nikolabojanic.controller.it;


import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.nikolabojanic.config.security.JwtIssuer;
import com.nikolabojanic.dto.RegistrationResponseDto;
import com.nikolabojanic.dto.TraineeRegistrationRequestDto;
import com.nikolabojanic.dto.TraineeResponseDto;
import com.nikolabojanic.dto.TraineeTrainerResponseDto;
import com.nikolabojanic.dto.TraineeTrainerUpdateRequestDto;
import com.nikolabojanic.dto.TraineeUpdateRequestDto;
import com.nikolabojanic.dto.TraineeUpdateResponseDto;
import com.nikolabojanic.entity.TokenEntity;
import com.nikolabojanic.entity.TraineeEntity;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.entity.UserEntity;
import com.nikolabojanic.enumeration.UserRole;
import com.nikolabojanic.repository.TokenRepository;
import com.nikolabojanic.repository.TraineeRepository;
import com.nikolabojanic.repository.TrainerRepository;
import com.nikolabojanic.repository.UserRepository;
import com.nikolabojanic.testcontainers.psql.DbContainer;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TraineeControllerIT {
    @Container
    private static final PostgreSQLContainer<DbContainer> postgreSQLContainer = DbContainer.getContainer();
    private static final String BASE_URI = "http://localhost:";
    private static final String TRAINEES_URI = "/api/v1/trainees";
    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private JwtIssuer jwtIssuer;
    @LocalServerPort
    private int port;

    private static Stream<Arguments> unauthorizedAccessSource() {
        return Stream.of(
            Arguments.of(
                "/" + RandomStringUtils.randomAlphabetic(10),
                "GET"
            ), Arguments.of(
                "/" + RandomStringUtils.randomAlphabetic(10),
                "PUT"
            ), Arguments.of(
                "/" + RandomStringUtils.randomAlphabetic(10),
                "DELETE"
            ), Arguments.of(
                "/trainers/" + RandomStringUtils.randomAlphabetic(10),
                "PUT"
            ), Arguments.of(
                "/active-status/" + RandomStringUtils.randomAlphabetic(10),
                "PATCH"
            )
        );
    }

    @Test
    void getTraineeIT() {
        //arrange
        UserEntity user = new UserEntity();
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setIsActive(true);
        user.setRole(UserRole.TRAINEE);
        TraineeEntity trainee = new TraineeEntity();
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setUser(user);
        trainee = traineeRepository.save(trainee);

        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().toString()));
        claims.put("username", user.getUsername());
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(trainee.getUser().getId(), claims);

        TokenEntity token = new TokenEntity();
        token.setData(jwt);
        tokenRepository.save(token);

        TraineeResponseDto responseDto =
            //act
            given()
                .header("Authorization", "Bearer " + jwt)
                .when()
                .pathParam("username", trainee.getUser().getUsername())
                .get(BASE_URI + port + TRAINEES_URI + "/{username}")
                .then()
                .statusCode(200)
                .extract().as(TraineeResponseDto.class);
        //assert
        assertThat(responseDto.getFirstName()).isEqualTo(trainee.getUser().getFirstName());
        assertThat(responseDto.getLastName()).isEqualTo(trainee.getUser().getLastName());
        assertThat(responseDto.getIsActive()).isEqualTo(trainee.getUser().getIsActive());
        assertThat(responseDto.getAddress()).isEqualTo(trainee.getAddress());
        assertThat(responseDto.getDateOfBirth()).isEqualTo(trainee.getDateOfBirth());
    }

    @Test
    void getNonExistentTraineeIT() {
        //arrange
        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(UserRole.TRAINEE.toString()));
        claims.put("username", RandomStringUtils.randomAlphabetic(10));
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(Long.parseLong(RandomStringUtils.randomNumeric(5)), claims);
        TokenEntity token = new TokenEntity();
        token.setData(jwt);
        tokenRepository.save(token);

        String username = RandomStringUtils.randomAlphabetic(10);
        //act
        Response response =
            given()
                .header("Authorization", "Bearer " + jwt)
                .when()
                .pathParam("username", username)
                .get(BASE_URI + port + TRAINEES_URI + "/{username}")
                .then()
                .statusCode(404)
                .extract().response();
        //assert
        assertThat(response.getBody().asString())
            .isEqualTo("Trainee with username " + username + " doesn't exist");
    }

    @Test
    void updateTraineeIT() {
        //arrange
        UserEntity user = new UserEntity();
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setIsActive(true);
        user.setRole(UserRole.TRAINEE);
        TraineeEntity trainee = new TraineeEntity();
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setUser(user);
        trainee = traineeRepository.save(trainee);

        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().toString()));
        claims.put("username", user.getUsername());
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(trainee.getUser().getId(), claims);

        TokenEntity token = new TokenEntity();
        token.setData(jwt);
        tokenRepository.save(token);

        TraineeUpdateRequestDto requestDto = new TraineeUpdateRequestDto(
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            LocalDate.now(),
            RandomStringUtils.randomAlphabetic(5),
            true);
        //act
        TraineeUpdateResponseDto responseDto =
            given()
                .header("Authorization", "Bearer " + jwt)
                .pathParam("username", trainee.getUser().getUsername())
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .put(BASE_URI + port + TRAINEES_URI + "/{username}")
                .then()
                .statusCode(200)
                .extract().as(TraineeUpdateResponseDto.class);
        //assert
        assertThat(responseDto.getAddress()).isEqualTo(requestDto.getAddress());
        assertThat(responseDto.getUsername()).isEqualTo(trainee.getUser().getUsername());
        assertThat(responseDto.getIsActive()).isEqualTo(requestDto.getIsActive());
        assertThat(responseDto.getDateOfBirth()).isEqualTo(requestDto.getDateOfBirth());
        assertThat(responseDto.getFirstName()).isEqualTo(requestDto.getFirstName());
        assertThat(responseDto.getLastName()).isEqualTo(requestDto.getLastName());
    }

    @Test
    void invalidUpdateTraineeIT() {
        //arrange
        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(UserRole.TRAINEE.toString()));
        claims.put("username", RandomStringUtils.randomAlphabetic(10));
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(Long.parseLong(RandomStringUtils.randomNumeric(5)), claims);

        TokenEntity token = new TokenEntity();
        token.setData(jwt);
        tokenRepository.save(token);

        TraineeUpdateRequestDto requestDto = new TraineeUpdateRequestDto(
            " ",
            " ",
            " ",
            null,
            " ",
            null);
        //act
        Response response =
            given()
                .header("Authorization", "Bearer " + jwt)
                .pathParam("username", RandomStringUtils.randomAlphabetic(10))
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .put(BASE_URI + port + TRAINEES_URI + "/{username}")
                .then()
                .statusCode(400)
                .extract().response();
        //assert
        assertThat(response.getBody().asString()).isEqualTo(
            "[Username must be at least 4 characters long,"
                + " First name must be at least two characters long,"
                + " Last name must be at least two characters long,"
                + " Active status must not be null]"
        );
    }

    @Test
    void registerTraineeIT() {
        //arrange
        TraineeRegistrationRequestDto requestDto = new TraineeRegistrationRequestDto(
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            LocalDate.now(),
            RandomStringUtils.randomAlphabetic(5));
        //act
        RegistrationResponseDto responseDto =
            given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .post(BASE_URI + port + TRAINEES_URI)
                .then()
                .statusCode(200)
                .extract().as(RegistrationResponseDto.class);
        //assert
        assertThat(responseDto.getUsername())
            .isEqualTo(requestDto.getFirstName() + "." + requestDto.getLastName());
        assertThat(responseDto.getPassword()).hasSize(10);
    }

    @Test
    void invalidRegisterTraineeIT() {
        //arrange
        TraineeRegistrationRequestDto requestDto = new TraineeRegistrationRequestDto(
            " ",
            " ",
            null,
            "");
        //act
        Response response =
            given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .post(BASE_URI + port + TRAINEES_URI)
                .then()
                .statusCode(400)
                .extract().response();
        //assert
        assertThat(response.getBody().asString()).isEqualTo(
            "[First name must be at least two characters long,"
                + " Last name must be at least two characters long]"
        );
    }

    @Test
    void deleteTraineeIT() {
        //arrange
        UserEntity user = new UserEntity();
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setIsActive(true);
        user.setRole(UserRole.TRAINEE);
        TraineeEntity trainee = new TraineeEntity();
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setUser(user);
        trainee = traineeRepository.save(trainee);

        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().toString()));
        claims.put("username", user.getUsername());
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(trainee.getUser().getId(), claims);

        TokenEntity token = new TokenEntity();
        token.setData(jwt);
        tokenRepository.save(token);

        given()
            .header("Authorization", "Bearer " + jwt)
            .pathParam("username", trainee.getUser().getUsername())
            .when()
            .delete(BASE_URI + port + TRAINEES_URI + "/{username}")
            .then()
            .statusCode(200);
        assertThat(userRepository.findByUsername(trainee.getUser().getUsername())).isEmpty();
    }

    @Test
    void deleteNonExistentTraineeIT() {
        //arrange
        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(UserRole.TRAINEE.toString()));
        claims.put("username", RandomStringUtils.randomAlphabetic(10));
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(Long.parseLong(RandomStringUtils.randomNumeric(5)), claims);
        TokenEntity token = new TokenEntity();
        token.setData(jwt);
        tokenRepository.save(token);
        String username = RandomStringUtils.randomAlphabetic(10);
        //act
        Response response =
            given()
                .header("Authorization", "Bearer " + jwt)
                .pathParam("username", username)
                .when()
                .delete(BASE_URI + port + TRAINEES_URI + "/{username}")
                .then()
                .statusCode(404)
                .extract()
                .response();
        //assert
        assertThat(response.getBody().asString())
            .isEqualTo("Trainee with username " + username + " doesn't exist");
    }

    @Test
    void updateTraineesTrainersIT() {
        //arrange
        UserEntity user = new UserEntity();
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setIsActive(true);
        user.setRole(UserRole.TRAINEE);
        TraineeEntity trainee = new TraineeEntity();
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setUser(user);
        trainee = traineeRepository.save(trainee);

        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().toString()));
        claims.put("username", user.getUsername());
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(trainee.getUser().getId(), claims);

        TokenEntity token = new TokenEntity();
        token.setData(jwt);
        tokenRepository.save(token);

        UserEntity trainerUser = new UserEntity();
        trainerUser.setFirstName(RandomStringUtils.randomAlphabetic(5));
        trainerUser.setLastName(RandomStringUtils.randomAlphabetic(5));
        trainerUser.setUsername(RandomStringUtils.randomAlphabetic(10));
        trainerUser.setPassword(RandomStringUtils.randomAlphabetic(10));
        trainerUser.setIsActive(true);
        trainerUser.setRole(UserRole.TRAINER);
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(trainerUser);
        trainer = trainerRepository.save(trainer);
        //act
        List<TraineeTrainerResponseDto> responseDtos =
            given()
                .contentType("application/json")
                .body(List.of(new TraineeTrainerUpdateRequestDto(trainer.getUser().getUsername())))
                .header("Authorization", "Bearer " + jwt)
                .pathParam("username", trainee.getUser().getUsername())
                .when()
                .put(BASE_URI + port + TRAINEES_URI + "/trainers/{username}")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("", TraineeTrainerResponseDto.class);
        //assert
        assertThat(responseDtos).hasSize(1);
        assertThat(responseDtos.get(0).getUsername()).isEqualTo(trainerUser.getUsername());
        assertThat(responseDtos.get(0).getFirstName()).isEqualTo(trainerUser.getFirstName());
        assertThat(responseDtos.get(0).getLastName()).isEqualTo(trainerUser.getLastName());
    }

    @Test
    void updateTraineesWithNonExistentTrainerIT() {
        //arrange
        UserEntity user = new UserEntity();
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setIsActive(true);
        user.setRole(UserRole.TRAINEE);
        TraineeEntity trainee = new TraineeEntity();
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setUser(user);
        trainee = traineeRepository.save(trainee);

        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().toString()));
        claims.put("username", user.getUsername());
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(trainee.getUser().getId(), claims);
        TokenEntity token = new TokenEntity();
        token.setData(jwt);
        tokenRepository.save(token);

        String username = RandomStringUtils.randomAlphabetic(10);
        //act
        Response response =
            given()
                .contentType("application/json")
                .body(List.of(new TraineeTrainerUpdateRequestDto(username)))
                .header("Authorization", "Bearer " + jwt)
                .pathParam("username", trainee.getUser().getUsername())
                .when()
                .put(BASE_URI + port + TRAINEES_URI + "/trainers/{username}")
                .then()
                .statusCode(404)
                .extract().response();
        //assert
        assertThat(response.getBody().asString())
            .isEqualTo("Trainer with username " + username + " doesn't exist");
    }

    @Test
    void changeActiveStatusIt() {
        //arrange
        UserEntity user = new UserEntity();
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setIsActive(true);
        user.setRole(UserRole.TRAINEE);
        TraineeEntity trainee = new TraineeEntity();
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setUser(user);
        trainee = traineeRepository.save(trainee);

        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().toString()));
        claims.put("username", user.getUsername());
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(trainee.getUser().getId(), claims);

        TokenEntity token = new TokenEntity();
        token.setData(jwt);
        tokenRepository.save(token);
        //act
        given()
            .header("Authorization", "Bearer " + jwt)
            .pathParam("username", trainee.getUser().getUsername())
            .param("activeStatus", true)
            .when()
            .patch(BASE_URI + port + TRAINEES_URI + "/active-status/{username}")
            .then()
            .statusCode(200);
        //assert
        assertThat(userRepository.findByUsername(trainee.getUser().getUsername()).get().getIsActive()).isTrue();
    }

    @ParameterizedTest
    @MethodSource("unauthorizedAccessSource")
    void unauthorizedAccessIT(String uri, String method) {
        RequestSpecification requestSpecification = given().contentType("application/json");
        ValidatableResponse response;
        switch (method) {
            case "GET" -> response = requestSpecification.when().get(BASE_URI + port + TRAINEES_URI + uri).then();
            case "PUT" -> response = requestSpecification.when().put(BASE_URI + port + TRAINEES_URI + uri).then();
            case "DELETE" -> response = requestSpecification.when()
                .delete(BASE_URI + port + TRAINEES_URI + uri).then();
            default -> response = requestSpecification.when().patch(BASE_URI + port + TRAINEES_URI + uri).then();
        }
        response.statusCode(401);
    }

    @ParameterizedTest
    @MethodSource("unauthorizedAccessSource")
    void unauthorizedRoleIT(String uri, String method) {
        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(UserRole.TRAINER.toString()));
        claims.put("username", RandomStringUtils.randomAlphabetic(10));
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(Long.parseLong(RandomStringUtils.randomNumeric(5)), claims);
        TokenEntity token = new TokenEntity();
        token.setData(jwt);
        tokenRepository.save(token);

        RequestSpecification requestSpecification = given().contentType("application/json")
            .header("Authorization", "Bearer " + jwt);
        ValidatableResponse response;
        switch (method) {
            case "GET" -> response = requestSpecification.when().get(BASE_URI + port + TRAINEES_URI + uri).then();
            case "PUT" -> response = requestSpecification.when().put(BASE_URI + port + TRAINEES_URI + uri).then();
            case "DELETE" -> response = requestSpecification.when()
                .delete(BASE_URI + port + TRAINEES_URI + uri).then();
            default -> response = requestSpecification.when().patch(BASE_URI + port + TRAINEES_URI + uri).then();
        }
        response.statusCode(403);
    }

}