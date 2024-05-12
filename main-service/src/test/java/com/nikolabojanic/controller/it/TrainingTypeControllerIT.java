package com.nikolabojanic.controller.it;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.nikolabojanic.config.security.JwtIssuer;
import com.nikolabojanic.dto.TrainingTypeRequestDto;
import com.nikolabojanic.dto.TrainingTypeResponseDto;
import com.nikolabojanic.entity.TokenEntity;
import com.nikolabojanic.entity.TokenType;
import com.nikolabojanic.entity.TrainingTypeEntity;
import com.nikolabojanic.enumeration.UserRole;
import com.nikolabojanic.repository.TokenRepository;
import com.nikolabojanic.repository.TrainingTypeRepository;
import com.nikolabojanic.testcontainers.psql.DbContainer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
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
public class TrainingTypeControllerIT {
    private static final String BASE_URI = "http://localhost:";
    private static final String TRAINING_TYPES_URI = "/api/v1/training-types";
    @Container
    private static final PostgreSQLContainer<DbContainer> postgreSQLContainer = DbContainer.getContainer();
    @Autowired
    private JwtIssuer jwtIssuer;
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @LocalServerPort
    private int port;

    @Test
    void getAllIT() {
        //arrange
        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(UserRole.TRAINER.toString()));
        claims.put("username", RandomStringUtils.randomAlphabetic(5));
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(Long.parseLong(RandomStringUtils.randomNumeric(3)), claims);
        TokenEntity token = new TokenEntity(jwt, TokenType.BEARER, false, false, null);
        tokenRepository.save(token);

        TrainingTypeEntity trainingType = new TrainingTypeEntity();
        trainingType.setName(RandomStringUtils.randomAlphabetic(5));
        trainingTypeRepository.save(trainingType);
        //act
        List<TrainingTypeResponseDto> responseDtos =
            given()
                .header("Authorization", "Bearer " + jwt)
                .when()
                .get(BASE_URI + port + TRAINING_TYPES_URI)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("", TrainingTypeResponseDto.class);
        //assert
        assertThat(responseDtos).isNotEmpty();
    }

    @Test
    void createTypeIT() {
        //arrange
        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(UserRole.TRAINER.toString()));
        claims.put("username", RandomStringUtils.randomAlphabetic(5));
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(Long.parseLong(RandomStringUtils.randomNumeric(3)), claims);
        TokenEntity token = new TokenEntity(jwt, TokenType.BEARER, false, false, null);
        tokenRepository.save(token);

        TrainingTypeRequestDto requestDto = new TrainingTypeRequestDto();
        requestDto.setName(RandomStringUtils.randomAlphabetic(5));
        //act
        TrainingTypeResponseDto responseDto =
            given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwt)
                .body(requestDto)
                .when()
                .post(BASE_URI + port + TRAINING_TYPES_URI)
                .then()
                .statusCode(200)
                .extract().as(TrainingTypeResponseDto.class);
        //assert
        assertThat(responseDto.getName()).isEqualTo(requestDto.getName());
    }

    @Test
    void unauthorizedGetAllIT() {
        //act
        given()
            .when()
            .get(BASE_URI + port + TRAINING_TYPES_URI)
            .then()
            //assert
            .statusCode(401);
    }
}