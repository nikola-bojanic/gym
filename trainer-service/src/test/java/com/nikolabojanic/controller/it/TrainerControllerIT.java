package com.nikolabojanic.controller.it;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.nikolabojanic.config.jms.MessageSender;
import com.nikolabojanic.config.security.JwtProperties;
import com.nikolabojanic.dto.TrainerWorkloadRequestDto;
import com.nikolabojanic.dto.TrainerWorkloadResponseDto;
import com.nikolabojanic.entity.MonthEntity;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.entity.YearEntity;
import com.nikolabojanic.enumeration.Action;
import com.nikolabojanic.repository.TrainerRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TrainerControllerIT {
    @Container
    private static final MongoDBContainer mongo = new MongoDBContainer(
        DockerImageName.parse("mongo:7.0.5")).withExposedPorts(27017);
    @Container
    private static final GenericContainer<?> activeMq = new GenericContainer<>(
        DockerImageName.parse("rmohr/activemq:latest")).withExposedPorts(61616);
    private static final String BASE_URI = "http://localhost:";
    private static final String TRAINERS_URI = "/api/v1/trainers";
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private MessageSender messageSender;
    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void testContainerProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", mongo::getHost);
        registry.add("spring.data.mongodb.port", () -> mongo.getMappedPort(27017));
        registry.add("spring.activemq.broker-url", () ->
            "tcp://%s:%d".formatted(activeMq.getHost(), activeMq.getMappedPort(61616)));
    }

    private static Stream<Arguments> unauthorizedAccessSource() {
        return Stream.of(
            Arguments.of("/{username}", RandomStringUtils.randomAlphabetic(10)),
            Arguments.of("", null));
    }

    @Test
    void getTrainingSummaryIT() throws JOSEException, InterruptedException {
        //arrange
        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("TRAINER"));
        claims.put("username", RandomStringUtils.randomAlphabetic(5));
        claims.put("roles", roles);
        String jwt = generateMockJwt(Long.parseLong(RandomStringUtils.randomNumeric(3)), claims);

        TrainerWorkloadRequestDto trainerWorkloadRequestDto = new TrainerWorkloadRequestDto(
            RandomStringUtils.randomAlphabetic(10),
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            true,
            LocalDate.now(),
            Double.parseDouble(RandomStringUtils.randomNumeric(10)),
            Action.ADD
        );

        messageSender.send(trainerWorkloadRequestDto);

        await().atMost(10_000, TimeUnit.MILLISECONDS).untilAsserted(() -> {
            TrainerWorkloadResponseDto responseDto =
                given()
                    .header("Authorization", "Bearer " + jwt)
                    .pathParam("username", trainerWorkloadRequestDto.getUsername())
                    .when()
                    .get(BASE_URI + port + TRAINERS_URI + "/{username}")
                    .then()
                    .statusCode(200)
                    .extract().as(TrainerWorkloadResponseDto.class);
            //assert
            assertThat(responseDto.getFirstName()).isEqualTo(trainerWorkloadRequestDto.getFirstName());
            assertThat(responseDto.getLastName()).isEqualTo(trainerWorkloadRequestDto.getLastName());
            assertThat(responseDto.getUsername()).isEqualTo(trainerWorkloadRequestDto.getUsername());
            assertThat(responseDto.getIsActive()).isEqualTo(trainerWorkloadRequestDto.getIsActive());
            assertThat(responseDto.getYears().get(0).getYear()).isEqualTo(
                trainerWorkloadRequestDto.getDate().getYear());
            assertThat(responseDto.getYears().get(0).getMonths().get(0).getMonth())
                .isEqualTo(trainerWorkloadRequestDto.getDate().getMonth());
        });
    }

    @Test
    void getTrainingSummaryWithInvalidUsernameIT() throws JOSEException {
        //arrange
        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("TRAINER"));
        claims.put("username", RandomStringUtils.randomAlphabetic(5));
        claims.put("roles", roles);
        String jwt = generateMockJwt(Long.parseLong(RandomStringUtils.randomNumeric(3)), claims);
        //act
        Response response =
            given()
                .header("Authorization", "Bearer " + jwt)
                .pathParam("username", " ")
                .when()
                .get(BASE_URI + port + TRAINERS_URI + "/{username}")
                .then()
                .statusCode(400)
                .extract().response();
        //assert
        assertThat(response.getBody().asString()).isEqualTo("Username must be at least 4 characters long");
    }

    @Test
    void findTrainersByNameIT() throws JOSEException {
        //arrange
        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("TRAINER"));
        claims.put("username", RandomStringUtils.randomAlphabetic(5));
        claims.put("roles", roles);

        MonthEntity monthEntity = new MonthEntity();
        monthEntity.setMonth(Month.APRIL);
        monthEntity.setTrainingSummary(Double.parseDouble(RandomStringUtils.randomNumeric(5)));

        YearEntity yearEntity = new YearEntity();
        yearEntity.setYear(2024);
        yearEntity.setMonths(List.of(monthEntity));

        TrainerEntity trainer = new TrainerEntity();
        trainer.setUsername(RandomStringUtils.randomAlphabetic(10));
        trainer.setFirstName(RandomStringUtils.randomAlphabetic(5));
        trainer.setLastName(RandomStringUtils.randomAlphabetic(5));
        trainer.setIsActive(true);
        trainer.setYears(List.of(yearEntity));
        trainer = trainerRepository.save(trainer);
        String jwt = generateMockJwt(Long.parseLong(RandomStringUtils.randomNumeric(3)), claims);
        //act
        List<TrainerWorkloadResponseDto> responseDtos =
            given()
                .header("Authorization", "Bearer " + jwt)
                .param("firstName", trainer.getFirstName())
                .param("lastName", trainer.getLastName())
                .when()
                .get(BASE_URI + port + TRAINERS_URI)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("", TrainerWorkloadResponseDto.class);
        //assert
        assertThat(responseDtos.get(0).getFirstName()).isEqualTo(trainer.getFirstName());
        assertThat(responseDtos.get(0).getLastName()).isEqualTo(trainer.getLastName());
        assertThat(responseDtos.get(0).getUsername()).isEqualTo(trainer.getUsername());
        assertThat(responseDtos.get(0).getIsActive()).isEqualTo(trainer.getIsActive());
        assertThat(responseDtos.get(0).getYears()).hasSameSizeAs(trainer.getYears());
    }

    @Test
    void findTrainersByInvalidNameIT() throws JOSEException {
        //arrange
        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("TRAINER"));
        claims.put("username", RandomStringUtils.randomAlphabetic(5));
        claims.put("roles", roles);
        String jwt = generateMockJwt(Long.parseLong(RandomStringUtils.randomNumeric(3)), claims);
        //act
        Response response =
            given()
                .header("Authorization", "Bearer " + jwt)
                .param("firstName", " ")
                .param("lastName", " ")
                .when()
                .get(BASE_URI + port + TRAINERS_URI)
                .then()
                .statusCode(400)
                .extract().response();
        //assert
        assertThat(response.getBody().asString()).isEqualTo(
            "[First name must be at least 2 characters long,"
                + " Last name must be at least 2 characters long]");
    }

    @ParameterizedTest
    @MethodSource("unauthorizedAccessSource")
    void unauthorizedAccessIT(String uri, String pathParam) {
        //act
        RequestSpecification requestSpecification = given();
        if (pathParam != null) {
            requestSpecification = requestSpecification.pathParam("username", pathParam);
        }
        requestSpecification
            .when()
            .get(BASE_URI + port + TRAINERS_URI + uri)
            .then()
            //assert
            .statusCode(401);
    }

    private String generateMockJwt(Long id, Map<String, Object> claims) throws JOSEException {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 3_600_000);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .subject(id.toString())
            .claim("username", claims.get("username"))
            .claim("roles", claims.get("roles"))
            .issueTime(now)
            .expirationTime(expiration)
            .build();
        JWSHeader header = new JWSHeader(jwtProperties.getAlgorithm());
        SignedJWT jwt = new SignedJWT(header, claimsSet);
        MACSigner signer = new MACSigner(jwtProperties.getKey());
        jwt.sign(signer);
        return jwt.serialize();
    }
}