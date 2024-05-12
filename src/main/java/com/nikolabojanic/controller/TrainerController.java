package com.nikolabojanic.controller;

import com.nikolabojanic.converter.TrainerConverter;
import com.nikolabojanic.dto.*;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.service.TrainerService;
import com.nikolabojanic.validation.TrainerValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "api/v1/trainers")
public class TrainerController {
    private final TrainerConverter trainerConverter;
    private final TrainerValidation trainerValidation;
    private final TrainerService trainerService;

    @GetMapping(value = "/{username}", produces = "application/json")
    @Operation(summary = "fetch Trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully fetched a trainer", content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Application failed to authenticate user"),
            @ApiResponse(responseCode = "404",
                    description = "Application failed to find a trainer with given username"),
            @ApiResponse(responseCode = "500",
                    description = "Application failed to process the request")
    })
    public ResponseEntity<TrainerResponseDTO> getTrainer(
            @PathVariable("username") String username,
            @RequestHeader(name = "Auth-Username") String authUsername,
            @RequestHeader(name = "Auth-Password") String authPassword) {
        trainerValidation.validateUsernameNotNull(username);
        TrainerEntity trainer = trainerService.findByUsername(new AuthDTO(authUsername, authPassword), username);
        TrainerResponseDTO responseDTO = trainerConverter.convertModelToResponseDTO(trainer);
        log.info("Successfully retrieved trainer with username {}. Status: {}", username, HttpStatus.OK.value());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/{username}", consumes = "application/json", produces = "application/json")
    @Operation(summary = "update Trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully updated a trainer", content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Application failed to authenticate user"),
            @ApiResponse(responseCode = "404",
                    description = "Application failed to find a trainer with given username"),
            @ApiResponse(responseCode = "500",
                    description = "Application failed to process the request")
    })
    public ResponseEntity<TrainerUpdateResponseDTO> updateTrainer(
            @PathVariable("username") String username,
            @RequestHeader("Auth-Username") String authUsername,
            @RequestHeader("Auth-Password") String authPassword,
            @RequestBody TrainerUpdateRequestDTO requestDTO) {
        trainerValidation.validateUsernameNotNull(username);
        trainerValidation.validateUpdateTrainerRequest(requestDTO);
        TrainerEntity trainer = trainerConverter.convertUpdateRequestToModel(requestDTO);
        TrainerEntity updated = trainerService.updateTrainerProfile(new AuthDTO(
                authUsername, authPassword), username, trainer);
        TrainerUpdateResponseDTO responseDTO = trainerConverter.convertModelToUpdateResponse(updated);
        log.info("Successfully updated trainer with username {}. Status: {}", username, HttpStatus.OK.value());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(summary = "register Trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered a trainer", content = @Content),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    public ResponseEntity<RegistrationResponseDTO> registerTrainer(
            @RequestBody TrainerRegistrationRequestDTO requestDTO) {
        trainerValidation.validateRegisterRequest(requestDTO);
        TrainerEntity model = trainerConverter.convertRegistrationRequestToModel(requestDTO);
        TrainerEntity registered = trainerService.createTrainerProfile(model);
        RegistrationResponseDTO responseDTO = trainerConverter.convertModelToRegistrationResponse(registered);
        log.info("Successfully registered a trainer with username {}. " +
                "Status: {}", registered.getUser().getUsername(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/active/trainee/{username}", produces = "application/json")
    @Operation(summary = "fetch active Trainers for other trainees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully fetched active trainers for other trainees", content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Application failed to authenticate user"),
            @ApiResponse(responseCode = "404",
                    description = "Application failed to find a trainer with given username"),
            @ApiResponse(responseCode = "500",
                    description = "Application failed to process the request")
    })
    public ResponseEntity<List<ActiveTrainerResponseDTO>> getActiveTrainers(
            @PathVariable("username") String username,
            @RequestHeader("Auth-Username") String authUsername,
            @RequestHeader("Auth-Password") String authPassword) {
        trainerValidation.validateUsernameNotNull(username);
        List<TrainerEntity> trainers = trainerService.findActiveForOtherTrainees(new AuthDTO(
                authUsername, authPassword), username);
        List<ActiveTrainerResponseDTO> responseDTO = trainers.stream()
                .map(trainerConverter::convertModelToActiveTrainerResponse)
                .toList();
        log.info("Successfully retrieved active trainers for other trainees. Status: {}", HttpStatus.OK.value());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PatchMapping(value = "/active-status/{username}")
    @Operation(summary = "update Trainer active status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully changed trainer's active status", content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Application failed to authenticate user"),
            @ApiResponse(responseCode = "404",
                    description = "Application failed to find a trainer with given username"),
            @ApiResponse(responseCode = "500",
                    description = "Application failed to process the request")
    })
    public ResponseEntity<Void> changeActiveStatus(
            @PathVariable("username") String username,
            @RequestHeader("Auth-Username") String authUsername,
            @RequestHeader("Auth-Password") String authPassword,
            @RequestParam("activeStatus") Boolean activeStatus
    ) {
        trainerValidation.validateActiveStatusRequest(username, activeStatus);
        trainerService.changeActiveStatus(new AuthDTO(authUsername, authPassword), username, activeStatus);
        log.info("Successfully changed trainer active status. Trainee username {}. " +
                "Status: {}", username, HttpStatus.OK.value());
        return ResponseEntity.ok().build();
    }
}
