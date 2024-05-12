package com.nikolabojanic.controller;

import com.nikolabojanic.converter.TraineeConverter;
import com.nikolabojanic.converter.TrainerConverter;
import com.nikolabojanic.dto.*;
import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.service.TraineeService;
import com.nikolabojanic.validation.TraineeValidation;
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
@RequestMapping(value = "api/v1/trainees")
public class TraineeController {
    private final TraineeValidation traineeValidation;
    private final TraineeConverter traineeConverter;
    private final TraineeService traineeService;
    private final TrainerConverter trainerConverter;

    @GetMapping(value = "/{username}", produces = "application/json")
    @Operation(summary = "fetch Trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully fetched a trainee", content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Application failed to authenticate user"),
            @ApiResponse(responseCode = "404",
                    description = "Application failed to find a trainee with given username"),
            @ApiResponse(responseCode = "500",
                    description = "Application failed to process the request")
    })
    public ResponseEntity<TraineeResponseDTO> getTrainee(
            @PathVariable("username") String username,
            @RequestHeader("Auth-Username") String authUsername,
            @RequestHeader("Auth-Password") String authPassword
    ) {
        traineeValidation.validateUsernameNotNull(username);
        TraineeEntity trainee = traineeService.findByUsername(new AuthDTO(authUsername, authPassword), username);
        TraineeResponseDTO responseDTO = traineeConverter.convertModelToResponse(trainee);
        log.info("Successfully retrieved trainee with username {}. Status: {}", username, HttpStatus.OK.value());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/{username}", consumes = "application/json", produces = "application/json")
    @Operation(summary = "update Trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully updated a trainee", content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Application failed to authenticate user"),
            @ApiResponse(responseCode = "404",
                    description = "Application failed to find a trainee with given username"),
            @ApiResponse(responseCode = "500",
                    description = "Application failed to process the request")
    })
    public ResponseEntity<TraineeUpdateResponseDTO> updateTrainee(
            @PathVariable("username") String username,
            @RequestHeader("Auth-Username") String authUsername,
            @RequestHeader("Auth-Password") String authPassword,
            @RequestBody TraineeUpdateRequestDTO requestDTO) {
        traineeValidation.validateUsernameNotNull(username);
        traineeValidation.validateUpdateTraineeRequest(requestDTO);
        TraineeEntity trainee = traineeConverter.convertUpdateRequestToModel(requestDTO);
        TraineeEntity updated = traineeService.updateTraineeProfile(new AuthDTO(
                authUsername, authPassword), username, trainee);
        TraineeUpdateResponseDTO responseDTO = traineeConverter.convertModelToUpdateResponse(updated);
        log.info("Successfully updated trainee with username {}. Status: {}", username, HttpStatus.OK.value());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(summary = "register Trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered a trainee", content = @Content),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    public ResponseEntity<RegistrationResponseDTO> registerTrainee(
            @RequestBody TraineeRegistrationRequestDTO requestDTO) {
        traineeValidation.validateRegisterRequest(requestDTO);
        TraineeEntity model = traineeConverter.convertRegistrationRequestToModel(requestDTO);
        TraineeEntity registered = traineeService.createTraineeProfile(model);
        RegistrationResponseDTO responseDTO = traineeConverter.convertModelToRegistrationResponse(registered);
        log.info("Successfully registered a trainee with username {}. " +
                "Status: {}", registered.getUser().getUsername(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{username}")
    @Operation(summary = "delete Trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully deleted a trainee", content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Application failed to authenticate user"),
            @ApiResponse(responseCode = "404",
                    description = "Application failed to find a trainee with given username"),
            @ApiResponse(responseCode = "500",
                    description = "Application failed to process the request")
    })
    public ResponseEntity<Void> deleteTrainee(
            @PathVariable("username") String username,
            @RequestHeader("Auth-Username") String authUsername,
            @RequestHeader("Auth-Password") String authPassword) {
        traineeValidation.validateUsernameNotNull(username);
        traineeService.deleteByUsername(new AuthDTO(authUsername, authPassword), username);
        log.info("Successfully deleted trainee with username {}. Status: {}", username, HttpStatus.OK.value());
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/trainers/{username}", consumes = "application/json")
    @Operation(summary = "update Trainee's trainer list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully updated trainee's trainer list", content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Application failed to authenticate user"),
            @ApiResponse(responseCode = "404",
                    description = "Application failed to find a trainee with given username"),
            @ApiResponse(responseCode = "500",
                    description = "Application failed to process the request")
    })
    public ResponseEntity<List<TraineeTrainerResponseDTO>> updateTraineesTrainers(
            @PathVariable("username") String username,
            @RequestHeader("Auth-Username") String authUsername,
            @RequestHeader("Auth-Password") String authPassword,
            @RequestBody List<TraineeTrainerUpdateRequestDTO> requestDTO) {
        traineeValidation.validateUsernameNotNull(username);
        traineeValidation.validateUpdateTrainersRequest(requestDTO);
        List<TrainerEntity> updated = traineeService.updateTraineeTrainers(new AuthDTO(
                authUsername, authPassword), username, requestDTO);
        List<TraineeTrainerResponseDTO> responseDTO = updated.stream()
                .map(trainerConverter::convertModelToTraineeTrainer).toList();
        log.info("Successfully updated trainee's trainer list. Trainee username {}. " +
                "Status: {}", username, HttpStatus.OK.value());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PatchMapping(value = "/active-status/{username}")
    @Operation(summary = "update Trainee active status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully updated trainee active status", content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Application failed to authenticate user"),
            @ApiResponse(responseCode = "404",
                    description = "Application failed to find a trainee with given username"),
            @ApiResponse(responseCode = "500",
                    description = "Application failed to process the request")
    })
    public ResponseEntity<Void> changeActiveStatus(@PathVariable("username") String username,
                                                   @RequestHeader("Auth-Username") String authUsername,
                                                   @RequestHeader("Auth-Password") String authPassword,
                                                   @RequestParam("activeStatus") Boolean activeStatus
    ) {
        traineeValidation.validateActiveStatusRequest(username, activeStatus);
        traineeService.changeActiveStatus(new AuthDTO(authUsername, authPassword), username, activeStatus);
        log.info("Successfully changed trainee active status. Trainee username {}. " +
                "Status: {}", username, HttpStatus.OK.value());
        return ResponseEntity.ok().build();
    }
}
