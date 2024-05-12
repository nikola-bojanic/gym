package com.nikolabojanic.controller;

import com.nikolabojanic.config.security.UserPrincipal;
import com.nikolabojanic.dto.UserPasswordChangeRequestDTO;
import com.nikolabojanic.service.UserService;
import com.nikolabojanic.validation.UserValidation;
import io.micrometer.core.instrument.Counter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserValidation userValidation;
    private final Counter userEndpointsHitCounter;

    @Autowired
    public UserController(
            UserService userService,
            UserValidation userValidation,
            @Qualifier("userEndpointsHitCounter") Counter authenticationHitCounter) {
        this.userService = userService;
        this.userValidation = userValidation;
        this.userEndpointsHitCounter = authenticationHitCounter;
    }

    @PostMapping(value = "/password", consumes = "application/json", produces = "application/json")
    @Operation(summary = "change User Password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully changed user password", content = @Content),
            @ApiResponse(responseCode = "401", description = "Application failed to authenticate user"),
            @ApiResponse(responseCode = "403", description = "Failed to change other user's password"),
            @ApiResponse(responseCode = "404", description = "Application failed to find a user with given username"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @RequestBody UserPasswordChangeRequestDTO passwordChangeDTO) {
        userEndpointsHitCounter.increment();
        userValidation.validatePasswordRequestDTO(passwordChangeDTO.getUsername(), passwordChangeDTO);
        userValidation.validateUserPermissionToEdit(passwordChangeDTO.getUsername(), userPrincipal.getUsername());
        userService.changeUserPassword(passwordChangeDTO.getUsername(), passwordChangeDTO);
        log.info("Successfully changed user's password. Username {}, Status {}", passwordChangeDTO.getUsername(), HttpStatus.OK);
        return ResponseEntity.ok().build();
    }
}

