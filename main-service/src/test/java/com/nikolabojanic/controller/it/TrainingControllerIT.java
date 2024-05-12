package com.nikolabojanic.controller.it;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.nikolabojanic.config.jms.JmsConfig;
import com.nikolabojanic.config.security.JwtIssuer;
import com.nikolabojanic.dto.TraineeTrainingResponseDto;
import com.nikolabojanic.dto.TrainerTrainingResponseDto;
import com.nikolabojanic.dto.TrainerWorkloadRequestDto;
import com.nikolabojanic.dto.TrainingRequestDto;
import com.nikolabojanic.dto.TrainingResponseDto;
import com.nikolabojanic.entity.TokenEntity;
import com.nikolabojanic.entity.TokenType;
import com.nikolabojanic.entity.TraineeEntity;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.entity.TrainingEntity;
import com.nikolabojanic.entity.TrainingTypeEntity;
import com.nikolabojanic.entity.UserEntity;
import com.nikolabojanic.enumeration.Action;
import com.nikolabojanic.enumeration.UserRole;
import com.nikolabojanic.repository.TokenRepository;
import com.nikolabojanic.repository.TraineeRepository;
import com.nikolabojanic.repository.TrainerRepository;
import com.nikolabojanic.repository.TrainingRepository;
import com.nikolabojanic.repository.TrainingTypeRepository;
import com.nikolabojanic.repository.UserRepository;
import com.nikolabojanic.testcontainers.psql.DbContainer;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = JmsConfig.class)
public class TrainingControllerIT {
    @Container
    private static final PostgreSQLContainer<DbContainer> postgreSQLContainer = DbContainer.getContainer();
    private static final String BASE_URI = "http://localhost:";
    private static final String TRAININGS_URI = "/api/v1/trainings";
    @Container
    static GenericContainer<?> activeMq = new GenericContainer<>(
        DockerImageName.parse("rmohr/activemq:latest"))
        .withExposedPorts(61616);
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private JwtIssuer jwtIssuer;
    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;
    @Autowired
    private BlockingQueue<Object> blockingQueue;
    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void testContainerProps(DynamicPropertyRegistry registry) {
        registry.add("spring.activemq.broker-url", () ->
            "tcp://%s:%d".formatted(activeMq.getHost(), activeMq.getMappedPort(61616)));
    }

    private static Stream<Arguments> unauthorizedCreateTrainingSource() {
        return Stream.of(
            Arguments.of(
                "",
                "POST"
            ), Arguments.of(
                "/" + RandomStringUtils.randomNumeric(10),
                "DELETE"
            ), Arguments.of(
                "/trainer/" + RandomStringUtils.randomAlphabetic(10),
                "GET"
            ), Arguments.of(
                "/trainee/" + RandomStringUtils.randomAlphabetic(10),
                "GET"
            )
        );
    }

    @Test
    void createTrainingIT() throws InterruptedException {
        //arrange
        UserEntity trainerUser = UserEntity.builder()
            .firstName(RandomStringUtils.randomAlphabetic(5))
            .lastName(RandomStringUtils.randomAlphabetic(5))
            .username(RandomStringUtils.randomAlphabetic(10))
            .password(RandomStringUtils.randomAlphabetic(5))
            .isActive(true)
            .role(UserRole.TRAINER)
            .build();
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(trainerUser);
        trainer = trainerRepository.save(trainer);

        UserEntity traineeUser = UserEntity.builder()
            .firstName(RandomStringUtils.randomAlphabetic(5))
            .lastName(RandomStringUtils.randomAlphabetic(5))
            .username(RandomStringUtils.randomAlphabetic(10))
            .password(RandomStringUtils.randomAlphabetic(5))
            .isActive(true)
            .role(UserRole.TRAINEE)
            .build();
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(traineeUser);
        trainee.setDateOfBirth(LocalDate.now());
        trainee = traineeRepository.save(trainee);

        TrainingEntity training = TrainingEntity.builder()
            .name(RandomStringUtils.randomAlphabetic(5))
            .date(LocalDate.now())
            .trainer(trainer)
            .trainee(trainee)
            .duration(Double.parseDouble(RandomStringUtils.randomNumeric(5)))
            .build();
        training = trainingRepository.save(training);

        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(UserRole.TRAINER.toString()));
        claims.put("username", RandomStringUtils.randomAlphabetic(5));
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(Long.parseLong(RandomStringUtils.randomNumeric(3)), claims);
        TokenEntity token = new TokenEntity(jwt, TokenType.BEARER, false, false, null);
        tokenRepository.save(token);

        TrainingRequestDto trainingRequestDto = new TrainingRequestDto(
            training.getTrainee().getUser().getUsername(),
            training.getTrainer().getUser().getUsername(),
            RandomStringUtils.randomAlphabetic(5),
            LocalDate.now(),
            Double.parseDouble(RandomStringUtils.randomNumeric(5))
        );
        //act
        TrainingResponseDto responseDto =
            given()
                .header("Authorization", "Bearer " + jwt)
                .contentType("application/json")
                .body(trainingRequestDto)
                .when()
                .post(BASE_URI + port + TRAININGS_URI)
                .then()
                .statusCode(200)
                .extract().as(TrainingResponseDto.class);
        //assert
        assertThat(responseDto.getName()).isEqualTo(trainingRequestDto.getName());
        assertThat(responseDto.getDate()).isEqualTo(trainingRequestDto.getDate());
        assertThat(responseDto.getDuration()).isEqualTo(trainingRequestDto.getDuration());
        assertThat(responseDto.getTraineeUsername()).isEqualTo(trainingRequestDto.getTraineeUsername());
        assertThat(responseDto.getTrainerUsername()).isEqualTo(trainingRequestDto.getTrainerUsername());


        TrainerWorkloadRequestDto trainerWorkloadRequestDto = (TrainerWorkloadRequestDto)
            blockingQueue.poll(100L, TimeUnit.MILLISECONDS);

        assertThat(trainerWorkloadRequestDto.getAction()).isEqualTo(Action.ADD);
        assertThat(trainerWorkloadRequestDto.getDate()).isEqualTo(trainingRequestDto.getDate());
        assertThat(trainerWorkloadRequestDto.getDuration()).isEqualTo(trainingRequestDto.getDuration());
        assertThat(trainerWorkloadRequestDto.getUsername()).isEqualTo(trainingRequestDto.getTrainerUsername());
    }

    @Test
    void deleteTrainingIT() throws InterruptedException {
        //arrange
        UserEntity trainerUser = UserEntity.builder()
            .firstName(RandomStringUtils.randomAlphabetic(5))
            .lastName(RandomStringUtils.randomAlphabetic(5))
            .username(RandomStringUtils.randomAlphabetic(10))
            .password(RandomStringUtils.randomAlphabetic(5))
            .isActive(true)
            .role(UserRole.TRAINER)
            .build();
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(trainerUser);
        trainer = trainerRepository.save(trainer);

        TrainingEntity training = TrainingEntity.builder()
            .name(RandomStringUtils.randomAlphabetic(5))
            .date(LocalDate.now())
            .trainer(trainer)
            .duration(Double.parseDouble(RandomStringUtils.randomNumeric(5)))
            .build();
        training = trainingRepository.save(training);

        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(UserRole.TRAINER.toString()));
        claims.put("username", RandomStringUtils.randomAlphabetic(5));
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(Long.parseLong(RandomStringUtils.randomNumeric(3)), claims);
        TokenEntity token = new TokenEntity(jwt, TokenType.BEARER, false, false, null);
        tokenRepository.save(token);
        //act
        given()
            .header("Authorization", "Bearer " + jwt)
            .pathParam("id", training.getId())
            .when()
            .delete(BASE_URI + port + TRAININGS_URI + "/{id}")
            .then()
            //assert
            .statusCode(204);
        assertThat(trainingRepository.findById(training.getId())).isEmpty();

        TrainerWorkloadRequestDto trainerWorkloadRequestDto =
            (TrainerWorkloadRequestDto) blockingQueue.poll(100L, TimeUnit.MILLISECONDS);

        assertThat(trainerWorkloadRequestDto.getAction()).isEqualTo(Action.DELETE);
        assertThat(trainerWorkloadRequestDto.getDate()).isEqualTo(training.getDate());
        assertThat(trainerWorkloadRequestDto.getDuration()).isEqualTo(training.getDuration());
        assertThat(trainerWorkloadRequestDto.getUsername()).isEqualTo(training.getTrainer().getUser().getUsername());
    }

    @Test
    void deleteNonExistingTrainingIT() {
        //arrange
        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(UserRole.TRAINER.toString()));
        claims.put("username", RandomStringUtils.randomAlphabetic(5));
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(Long.parseLong(RandomStringUtils.randomNumeric(3)), claims);
        TokenEntity token = new TokenEntity(jwt, TokenType.BEARER, false, false, null);
        tokenRepository.save(token);
        //act
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        Response response =
            given()
                .header("Authorization", "Bearer " + jwt)
                .pathParam("id", id)
                .when()
                .delete(BASE_URI + port + TRAININGS_URI + "/{id}")
                .then()
                .statusCode(404)
                .extract().response();
        //assert
        assertThat(response.getBody().asString()).isEqualTo("Training with id " + id + " doesn't exist");
    }

    @Test
    void getTrainingsByTrainerAndFilterIT() {
        //arrange
        UserEntity trainerUser = UserEntity.builder()
            .firstName(RandomStringUtils.randomAlphabetic(5))
            .lastName(RandomStringUtils.randomAlphabetic(5))
            .username(RandomStringUtils.randomAlphabetic(10))
            .password(RandomStringUtils.randomAlphabetic(5))
            .isActive(true)
            .role(UserRole.TRAINER)
            .build();
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(trainerUser);
        trainer = trainerRepository.save(trainer);

        UserEntity traineeUser = UserEntity.builder()
            .firstName(RandomStringUtils.randomAlphabetic(5))
            .lastName(RandomStringUtils.randomAlphabetic(5))
            .username(RandomStringUtils.randomAlphabetic(10))
            .password(RandomStringUtils.randomAlphabetic(5))
            .isActive(true)
            .role(UserRole.TRAINEE)
            .build();
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(traineeUser);
        trainee.setDateOfBirth(LocalDate.now());
        trainee = traineeRepository.save(trainee);

        TrainingEntity training = TrainingEntity.builder()
            .name(RandomStringUtils.randomAlphabetic(5))
            .date(LocalDate.now())
            .trainer(trainer)
            .trainee(trainee)
            .duration(Double.parseDouble(RandomStringUtils.randomNumeric(5)))
            .build();
        training = trainingRepository.save(training);

        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(UserRole.TRAINER.toString()));
        claims.put("username", RandomStringUtils.randomAlphabetic(5));
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(Long.parseLong(RandomStringUtils.randomNumeric(3)), claims);
        TokenEntity token = new TokenEntity(jwt, TokenType.BEARER, false, false, null);
        tokenRepository.save(token);

        DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE;
        //act
        List<TrainerTrainingResponseDto> responseDtos =
            given()
                .header("Authorization", "Bearer " + jwt)
                .pathParam("username", training.getTrainer().getUser().getUsername())
                .param("dateFrom", LocalDate.now().minusDays(1).format(dtf))
                .param("dateTo", LocalDate.now().plusDays(1).format(dtf))
                .param("traineeName", training.getTrainee().getUser().getFirstName())
                .when()
                .get(BASE_URI + port + TRAININGS_URI + "/trainer/{username}")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("", TrainerTrainingResponseDto.class);
        //assert
        assertThat(responseDtos).hasSize(1);
    }

    @Test
    void getTrainingsByNonExistentTrainerAndFilterIT() {
        //arrange
        UserEntity trainerUser = UserEntity.builder()
            .firstName(RandomStringUtils.randomAlphabetic(5))
            .lastName(RandomStringUtils.randomAlphabetic(5))
            .username(RandomStringUtils.randomAlphabetic(10))
            .password(RandomStringUtils.randomAlphabetic(5))
            .isActive(true)
            .role(UserRole.TRAINER)
            .build();
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(trainerUser);
        trainer = trainerRepository.save(trainer);

        TrainingEntity training = TrainingEntity.builder()
            .name(RandomStringUtils.randomAlphabetic(5))
            .date(LocalDate.now())
            .trainer(trainer)
            .duration(Double.parseDouble(RandomStringUtils.randomNumeric(5)))
            .build();
        trainingRepository.save(training);

        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(UserRole.TRAINER.toString()));
        claims.put("username", RandomStringUtils.randomAlphabetic(5));
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(Long.parseLong(RandomStringUtils.randomNumeric(3)), claims);
        TokenEntity token = new TokenEntity(jwt, TokenType.BEARER, false, false, null);
        tokenRepository.save(token);

        DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE;
        //act
        List<TrainerTrainingResponseDto> responseDtos =
            given()
                .header("Authorization", "Bearer " + jwt)
                .pathParam("username", RandomStringUtils.randomAlphabetic(10))
                .param("dateFrom", LocalDate.now().minusDays(1).format(dtf))
                .param("dateTo", LocalDate.now().plusDays(1).format(dtf))
                .when()
                .get(BASE_URI + port + TRAININGS_URI + "/trainer/{username}")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("", TrainerTrainingResponseDto.class);
        //assert
        assertThat(responseDtos).isEmpty();
    }

    @Test
    void getTrainingsByTraineeAndFilterIT() {
        //arrange
        UserEntity trainerUser = UserEntity.builder()
            .firstName(RandomStringUtils.randomAlphabetic(5))
            .lastName(RandomStringUtils.randomAlphabetic(5))
            .username(RandomStringUtils.randomAlphabetic(10))
            .password(RandomStringUtils.randomAlphabetic(5))
            .isActive(true)
            .role(UserRole.TRAINER)
            .build();
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(trainerUser);
        trainer = trainerRepository.save(trainer);

        UserEntity traineeUser = UserEntity.builder()
            .firstName(RandomStringUtils.randomAlphabetic(5))
            .lastName(RandomStringUtils.randomAlphabetic(5))
            .username(RandomStringUtils.randomAlphabetic(10))
            .password(RandomStringUtils.randomAlphabetic(5))
            .isActive(true)
            .role(UserRole.TRAINEE)
            .build();
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(traineeUser);
        trainee.setDateOfBirth(LocalDate.now());
        trainee = traineeRepository.save(trainee);

        TrainingTypeEntity type = new TrainingTypeEntity();
        type.setName(RandomStringUtils.randomAlphabetic(10));
        trainingTypeRepository.save(type);

        TrainingEntity training = TrainingEntity.builder()
            .name(RandomStringUtils.randomAlphabetic(5))
            .date(LocalDate.now())
            .trainer(trainer)
            .trainee(trainee)
            .type(type)
            .duration(Double.parseDouble(RandomStringUtils.randomNumeric(5)))
            .build();
        training = trainingRepository.save(training);

        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(UserRole.TRAINER.toString()));
        claims.put("username", RandomStringUtils.randomAlphabetic(5));
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(Long.parseLong(RandomStringUtils.randomNumeric(3)), claims);
        TokenEntity token = new TokenEntity(jwt, TokenType.BEARER, false, false, null);
        tokenRepository.save(token);

        DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE;
        //act
        List<TraineeTrainingResponseDto> responseDtos =
            given()
                .header("Authorization", "Bearer " + jwt)
                .pathParam("username", training.getTrainee().getUser().getUsername())
                .param("dateFrom", LocalDate.now().minusDays(1).format(dtf))
                .param("dateTo", LocalDate.now().plusDays(1).format(dtf))
                .param("trainerName", training.getTrainer().getUser().getFirstName())
                .param("typeId", training.getType().getId())
                .when()
                .get(BASE_URI + port + TRAININGS_URI + "/trainee/{username}")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("", TraineeTrainingResponseDto.class);
        assertThat(responseDtos).hasSize(1);
    }

    @Test
    void getTrainingsByNonExistentTraineeAndFilterIT() {
        //arrange
        UserEntity traineeUser = UserEntity.builder()
            .firstName(RandomStringUtils.randomAlphabetic(5))
            .lastName(RandomStringUtils.randomAlphabetic(5))
            .username(RandomStringUtils.randomAlphabetic(10))
            .password(RandomStringUtils.randomAlphabetic(5))
            .isActive(true)
            .role(UserRole.TRAINEE)
            .build();
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(traineeUser);
        trainee.setDateOfBirth(LocalDate.now());
        trainee = traineeRepository.save(trainee);

        TrainingEntity training = TrainingEntity.builder()
            .name(RandomStringUtils.randomAlphabetic(5))
            .date(LocalDate.now())
            .trainee(trainee)
            .duration(Double.parseDouble(RandomStringUtils.randomNumeric(5)))
            .build();
        trainingRepository.save(training);

        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(UserRole.TRAINER.toString()));
        claims.put("username", RandomStringUtils.randomAlphabetic(5));
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(Long.parseLong(RandomStringUtils.randomNumeric(3)), claims);
        TokenEntity token = new TokenEntity(jwt, TokenType.BEARER, false, false, null);
        tokenRepository.save(token);

        //act
        DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE;
        List<TraineeTrainingResponseDto> responseDtos =
            given()
                .header("Authorization", "Bearer " + jwt)
                .pathParam("username", RandomStringUtils.randomAlphabetic(10))
                .param("dateFrom", LocalDate.now().minusDays(1).format(dtf))
                .param("dateTo", LocalDate.now().plusDays(1).format(dtf))
                .when()
                .get(BASE_URI + port + TRAININGS_URI + "/trainee/{username}")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("", TraineeTrainingResponseDto.class);
        //assert
        assertThat(responseDtos).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("unauthorizedCreateTrainingSource")
    void unauthorizedAccessIT(String uri, String method) {
        //act
        RequestSpecification requestSpecification = given()
            .contentType("application/json")
            .when();
        Response response;
        switch (method) {
            case "POST" -> response = requestSpecification.post(BASE_URI + port + TRAININGS_URI + uri);
            case "DELETE" -> response = requestSpecification.delete(BASE_URI + port + TRAININGS_URI + uri);
            default -> response = requestSpecification.get(BASE_URI + port + TRAININGS_URI + uri);
        }
        response.then()
            //assert
            .statusCode(401);
    }
}