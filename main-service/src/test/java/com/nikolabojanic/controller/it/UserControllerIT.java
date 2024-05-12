package com.nikolabojanic.controller.it;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.nikolabojanic.config.security.JwtIssuer;
import com.nikolabojanic.dto.UserPasswordChangeRequestDto;
import com.nikolabojanic.entity.TokenEntity;
import com.nikolabojanic.entity.UserEntity;
import com.nikolabojanic.enumeration.UserRole;
import com.nikolabojanic.repository.TokenRepository;
import com.nikolabojanic.repository.UserRepository;
import com.nikolabojanic.testcontainers.psql.DbContainer;
import io.restassured.response.Response;
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
public class UserControllerIT {
    @Container
    private static final PostgreSQLContainer<DbContainer> postgreSQLContainer = DbContainer.getContainer();
    private static final String BASE_URI = "http://localhost:";
    private static final String USERS_URI = "/api/v1/users";
    @Autowired
    private JwtIssuer jwtIssuer;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;
    @LocalServerPort
    private int port;

    @Test
    void changePasswordIT() {
        //arrange
        UserEntity user = UserEntity.builder()
            .firstName(RandomStringUtils.randomAlphabetic(5))
            .lastName(RandomStringUtils.randomAlphabetic(5))
            .password(RandomStringUtils.randomAlphabetic(10))
            .username(RandomStringUtils.randomAlphabetic(10))
            .isActive(true)
            .role(UserRole.TRAINEE)
            .build();
        user = userRepository.save(user);

        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().toString()));
        claims.put("username", user.getUsername());
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(user.getId(), claims);
        TokenEntity token = new TokenEntity();
        token.setData(jwt);
        tokenRepository.save(token);

        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto(
            user.getUsername(), user.getPassword(), RandomStringUtils.randomAlphabetic(10));
        //act
        given()
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .body(requestDto)
            .when()
            .post(BASE_URI + port + USERS_URI + "/password")
            .then()
            //assert
            .statusCode(200);
    }

    @Test
    void changeOthersPasswordIT() {
        //arrange
        UserEntity user = UserEntity.builder()
            .firstName(RandomStringUtils.randomAlphabetic(5))
            .lastName(RandomStringUtils.randomAlphabetic(5))
            .password(RandomStringUtils.randomAlphabetic(10))
            .username(RandomStringUtils.randomAlphabetic(10))
            .isActive(true)
            .role(UserRole.TRAINEE)
            .build();
        user = userRepository.save(user);

        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().toString()));
        claims.put("username", user.getUsername());
        claims.put("roles", roles);
        String jwt = jwtIssuer.generateJwt(user.getId(), claims);
        TokenEntity token = new TokenEntity();
        token.setData(jwt);
        tokenRepository.save(token);

        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto(
            RandomStringUtils.randomAlphabetic(5),
            user.getPassword(),
            RandomStringUtils.randomAlphabetic(10)
        );
        //act
        Response response =
            given()
                .header("Authorization", "Bearer " + jwt)
                .contentType("application/json")
                .body(requestDto)
                .when()
                .post(BASE_URI + port + USERS_URI + "/password")
                .then()
                .statusCode(403)
                .extract().response();
        //assert
        assertThat(response.body().asString()).isEqualTo("Cannot change other user's password");
    }

    @Test
    void unauthorizedChangePasswordIT() {
        //act
        given()
            .contentType("application/json")
            .when()
            .post(BASE_URI + port + USERS_URI + "/password")
            .then()
            //assert
            .statusCode(401);
    }
}
