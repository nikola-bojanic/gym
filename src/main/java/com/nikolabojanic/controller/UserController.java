package com.nikolabojanic.controller;

import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.dto.UserPasswordChangeRequestDTO;
import com.nikolabojanic.service.UserService;
import com.nikolabojanic.validation.UserValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserValidation userValidation;

    @PostMapping(value = "/auth", consumes = "application/json")
    @Operation(summary = "Login User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in", content = @Content),
            @ApiResponse(responseCode = "401", description = "Application failed to authenticate user"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    public ResponseEntity<Void> login(@RequestBody AuthDTO authDTO) {
        userService.authentication(authDTO);
        log.info("Successfully authenticated. Status: {}", HttpStatus.OK.value());
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/password/{username}", consumes = "application/json", produces = "application/json")
    @Operation(summary = "change User Password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully changed user password", content = @Content),
            @ApiResponse(responseCode = "401", description = "Application failed to authenticate user"),
            @ApiResponse(responseCode = "403", description = "Failed to change other user's password"),
            @ApiResponse(responseCode = "404", description = "Application failed to find a user with given username"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    public ResponseEntity<Void> changePassword(@PathVariable("username") String username,
                                               @RequestHeader("Auth-Username") String authUsername,
                                               @RequestHeader("Auth-Password") String authPassword,
                                               @RequestBody UserPasswordChangeRequestDTO passwordChangeDTO) {
        userValidation.validatePasswordRequestDTO(username, passwordChangeDTO);
        userService.changeUserPassword(new AuthDTO(authUsername, authPassword), username, passwordChangeDTO);
        log.info("Successfully changed user's password. Username {}, Status {}", username, HttpStatus.OK);
        return ResponseEntity.ok().build();
    }
}

