package com.nikolabojanic.controller.it;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.nikolabojanic.config.security.JwtIssuer;
import com.nikolabojanic.dto.ActiveTrainerResponseDto;
import com.nikolabojanic.dto.RegistrationResponseDto;
import com.nikolabojanic.dto.TrainerRegistrationRequestDto;
import com.nikolabojanic.dto.TrainerResponseDto;
import com.nikolabojanic.dto.TrainerUpdateRequestDto;
import com.nikolabojanic.dto.TrainerUpdateResponseDto;
import com.nikolabojanic.entity.TokenEntity;
import com.nikolabojanic.entity.TraineeEntity;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.entity.TrainingTypeEntity;
import com.nikolabojanic.entity.UserEntity;
import com.nikolabojanic.enumeration.UserRole;
import com.nikolabojanic.repository.TokenRepository;
import com.nikolabojanic.repository.TraineeRepository;
import com.nikolabojanic.repository.TrainerRepository;
import com.nikolabojanic.repository.TrainingTypeRepository;
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
public class TrainerControllerIT {
    @Container
    private static final PostgreSQLContainer<DbContainer> postgreSQLContainer = DbContainer.getContainer();
    private static final String BASE_URI = "http://localhost:";
    private static final String TRAINERS_URI = "/api/v1/trainers";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;
    @Autowired
    private TraineeRepository traineeRepository;
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
                "/active/trainee/" + RandomStringUtils.randomAlphabetic(10),
                "GET"
            ), Arguments.of(
                "/active-status/" + RandomStringUtils.randomAlphabetic(10),
                "PATCH"
            )
        );
    }

    @Test
    void getTrainerIT() {
        //arrange
        UserEntity user = new UserEntity();
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setIsActive(false);
        user.setRole(UserRole.TRAINER);
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        trainer = trainerRepository.save(trainer);

        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().toString()));
        claims.put("username", user.getUsername());
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(trainer.getUser().getId(), claims);
        TokenEntity token = new TokenEntity();
        token.setData(jwt);
        tokenRepository.save(token);
        //act
        TrainerResponseDto responseDto =
            given()
                .header("Authorization", "Bearer " + jwt).when()
                .pathParam("username", trainer.getUser().getUsername())
                .get(BASE_URI + port + TRAINERS_URI + "/{username}")
                .then()
                .statusCode(200)
                .extract().as(TrainerResponseDto.class);
        //assert
        assertThat(responseDto.getFirstName()).isEqualTo(trainer.getUser().getFirstName());
        assertThat(responseDto.getLastName()).isEqualTo(trainer.getUser().getLastName());
        assertThat(responseDto.getIsActive()).isEqualTo(trainer.getUser().getIsActive());
    }

    @Test
    void getNonExistentTrainerIT() {
        //arrange
        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(UserRole.TRAINER.toString()));
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
                .header("Authorization", "Bearer " + jwt).when()
                .pathParam("username", username)
                .get(BASE_URI + port + TRAINERS_URI + "/{username}")
                .then()
                .statusCode(404)
                .extract().response();
        //assert
        assertThat(response.getBody().asString())
            .isEqualTo("Trainer with username " + username + " doesn't exist");
    }

    @Test
    void updateTrainerIT() {
        //arrange
        UserEntity user = new UserEntity();
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setIsActive(false);
        user.setRole(UserRole.TRAINER);
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        trainer = trainerRepository.save(trainer);

        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().toString()));
        claims.put("username", user.getUsername());
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(trainer.getUser().getId(), claims);
        TokenEntity token = new TokenEntity();
        token.setData(jwt);
        tokenRepository.save(token);

        TrainerUpdateRequestDto requestDto = new TrainerUpdateRequestDto(
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            null,
            false
        );
        //act
        TrainerUpdateResponseDto responseDto =
            given()
                .header("Authorization", "Bearer " + jwt)
                .pathParam("username", trainer.getUser().getUsername())
                .contentType(ContentType.JSON).body(requestDto)
                .when()
                .put(BASE_URI + port + TRAINERS_URI + "/{username}")
                .then()
                .statusCode(200)
                .extract().as(TrainerUpdateResponseDto.class);
        //assert
        assertThat(responseDto.getFirstName()).isEqualTo(requestDto.getFirstName());
        assertThat(responseDto.getLastName()).isEqualTo(requestDto.getLastName());
        assertThat(responseDto.getIsActive()).isEqualTo(requestDto.getIsActive());
        assertThat(responseDto.getUsername()).isEqualTo(trainer.getUser().getUsername());
    }

    @Test
    void invalidUpdateTrainerIT() {
        //arrange
        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(UserRole.TRAINER.toString()));
        claims.put("username", RandomStringUtils.randomAlphabetic(10));
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(Long.parseLong(RandomStringUtils.randomNumeric(5)), claims);
        TokenEntity token = new TokenEntity();
        token.setData(jwt);
        tokenRepository.save(token);

        TrainerUpdateRequestDto requestDto = new TrainerUpdateRequestDto(
            " ",
            " ",
            " ",
            null,
            null
        );
        //act
        Response response =
            given()
                .header("Authorization", "Bearer " + jwt)
                .pathParam("username", RandomStringUtils.randomAlphabetic(10))
                .contentType(ContentType.JSON).body(requestDto)
                .when()
                .put(BASE_URI + port + TRAINERS_URI + "/{username}")
                .then()
                .statusCode(400)
                .extract()
                .response();
        //assert
        assertThat(response.getBody().asString()).isEqualTo(
            "[Username must be at least 4 characters long,"
                + " First name must be at least two characters long,"
                + " Last name must be at least two characters long,"
                + " Active status must not be null]");
    }

    @Test
    void registerTrainerIT() {
        //arrange
        TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity();
        trainingTypeEntity.setName(RandomStringUtils.randomAlphabetic(5));
        trainingTypeEntity = trainingTypeRepository.save(trainingTypeEntity);

        TrainerRegistrationRequestDto requestDto = new TrainerRegistrationRequestDto(
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            trainingTypeEntity.getId());
        //act
        RegistrationResponseDto responseDto =
            given()
                .contentType(ContentType.JSON).body(requestDto)
                .when()
                .post(BASE_URI + port + TRAINERS_URI)
                .then()
                .statusCode(200)
                .extract().as(RegistrationResponseDto.class);
        //assert
        assertThat(responseDto.getUsername()).isEqualTo(
            requestDto.getFirstName() + "." + requestDto.getLastName());
        assertThat(responseDto.getPassword()).hasSize(10);
    }

    @Test
    void invalidRegisterTrainerIT() {
        //arrange
        TrainerRegistrationRequestDto requestDto = new TrainerRegistrationRequestDto(
            " ",
            " ",
            null);
        //act
        Response response =
            given()
                .contentType(ContentType.JSON).body(requestDto)
                .when()
                .post(BASE_URI + port + TRAINERS_URI)
                .then()
                .statusCode(400)
                .extract().response();
        //assert
        assertThat(response.getBody().asString()).isEqualTo(
            "[First name must be at least two characters long,"
                + " Last name must be at least two characters long,"
                + " Cannot create trainer profile with null specialization id]"
        );
    }

    @Test
    void getActiveTrainersIT() {
        //arrange
        UserEntity user = new UserEntity();
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setIsActive(true);
        user.setRole(UserRole.TRAINER);
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        trainer = trainerRepository.save(trainer);

        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().toString()));
        claims.put("username", user.getUsername());
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(trainer.getUser().getId(), claims);
        TokenEntity token = new TokenEntity();
        token.setData(jwt);
        tokenRepository.save(token);
        UserEntity traineeUser = new UserEntity();
        traineeUser.setFirstName(RandomStringUtils.randomAlphabetic(5));
        traineeUser.setLastName(RandomStringUtils.randomAlphabetic(5));
        traineeUser.setUsername(RandomStringUtils.randomAlphabetic(10));
        traineeUser.setPassword(RandomStringUtils.randomAlphabetic(10));
        traineeUser.setIsActive(true);
        traineeUser.setRole(UserRole.TRAINEE);
        TraineeEntity trainee = new TraineeEntity();
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setUser(traineeUser);
        trainee = traineeRepository.save(trainee);
        //act
        List<ActiveTrainerResponseDto> responseDtos =
            given()
                .header("Authorization", "Bearer " + jwt)
                .pathParam("username", trainee.getUser().getUsername())
                .when()
                .get(BASE_URI + port + TRAINERS_URI + "/active/trainee/{username}")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("", ActiveTrainerResponseDto.class);
        //assert
        assertThat(responseDtos).isNotEmpty();
    }

    @Test
    void getActiveTrainersForNonExistentTraineeIT() {
        //arrange
        UserEntity user = new UserEntity();
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setIsActive(false);
        user.setRole(UserRole.TRAINER);
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        trainer = trainerRepository.save(trainer);

        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().toString()));
        claims.put("username", user.getUsername());
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(trainer.getUser().getId(), claims);
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
                .get(BASE_URI + port + TRAINERS_URI + "/active/trainee/{username}")
                .then()
                .statusCode(404)
                .extract().response();
        //assert
        assertThat(response.getBody().asString())
            .isEqualTo("Trainee with username " + username + " doesn't exist");
    }

    @Test
    void changeActiveStatusIT() {
        //arrange
        UserEntity user = new UserEntity();
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setIsActive(false);
        user.setRole(UserRole.TRAINER);
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        trainer = trainerRepository.save(trainer);

        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().toString()));
        claims.put("username", user.getUsername());
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(trainer.getUser().getId(), claims);
        TokenEntity token = new TokenEntity();
        token.setData(jwt);
        tokenRepository.save(token);
        //act
        given()
            .header("Authorization", "Bearer " + jwt)
            .pathParam("username", trainer.getUser().getUsername())
            .param("activeStatus", true)
            .when()
            .patch(BASE_URI + port + TRAINERS_URI + "/active-status/{username}")
            .then()
            .statusCode(200);
        //assert
        assertThat(userRepository.findByUsername(trainer.getUser().getUsername()).get().getIsActive())
            .isTrue();
    }

    @ParameterizedTest
    @MethodSource("unauthorizedAccessSource")
    void unauthorizedAccessIT(String uri, String method) {
        RequestSpecification requestSpecification = given().contentType("application/json");
        ValidatableResponse response;
        switch (method) {
            case "GET" -> response = requestSpecification.when().get(BASE_URI + port + TRAINERS_URI + uri).then();
            case "PUT" -> response = requestSpecification.when().put(BASE_URI + port + TRAINERS_URI + uri).then();
            default -> response = requestSpecification.when().patch(BASE_URI + port + TRAINERS_URI + uri).then();
        }
        response.statusCode(401);
    }

    @ParameterizedTest
    @MethodSource("unauthorizedAccessSource")
    void unauthorizedRoleIT(String uri, String method) {
        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(UserRole.TRAINEE.toString()));
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
            case "GET" -> response = requestSpecification.when().get(BASE_URI + port + TRAINERS_URI + uri).then();
            case "PUT" -> response = requestSpecification.when().put(BASE_URI + port + TRAINERS_URI + uri).then();
            case "DELETE" -> response = requestSpecification.when()
                .delete(BASE_URI + port + TRAINERS_URI + uri).then();
            default -> response = requestSpecification.when().patch(BASE_URI + port + TRAINERS_URI + uri).then();
        }
        response.statusCode(403);
    }
}
