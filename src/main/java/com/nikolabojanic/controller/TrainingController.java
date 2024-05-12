package com.nikolabojanic.controller;

import com.nikolabojanic.converter.TrainingConverter;
import com.nikolabojanic.dto.*;
import com.nikolabojanic.model.TrainingEntity;
import com.nikolabojanic.service.TrainingService;
import com.nikolabojanic.service.UserService;
import com.nikolabojanic.validation.TrainingValidation;
import io.micrometer.core.instrument.Counter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/trainings")
public class TrainingController {
    private final TrainingService trainingService;
    private final UserService userService;
    private final TrainingConverter trainingConverter;
    private final TrainingValidation trainingValidation;
    private final Counter trainingEndpointsHitCounter;

    public TrainingController(
            TrainingService trainingService,
            UserService userService,
            TrainingConverter trainingConverter,
            TrainingValidation trainingValidation,
            @Qualifier("trainingEndpointsHitCounter") Counter trainingEndpointsHitCounter) {
        this.trainingService = trainingService;
        this.userService = userService;
        this.trainingConverter = trainingConverter;
        this.trainingValidation = trainingValidation;
        this.trainingEndpointsHitCounter = trainingEndpointsHitCounter;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(summary = "create Training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created a training", content = @Content),
            @ApiResponse(responseCode = "401", description = "Application failed to authenticate user"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    public ResponseEntity<TrainingResponseDTO> createTraining(@RequestHeader("Auth-Username") String authUsername,
                                                              @RequestHeader("Auth-Password") String authPassword,
                                                              @RequestBody TrainingRequestDTO trainingDTO) {
        trainingEndpointsHitCounter.increment();
        userService.authentication(new AuthDTO(authUsername, authPassword));
        trainingValidation.validateCreateTrainingRequest(trainingDTO);
        TrainingEntity domainModel = trainingConverter.convertToEntity(trainingDTO);
        TrainingEntity created = trainingService.create(domainModel);
        TrainingResponseDTO responseDTO = trainingConverter.convertToDto(created);
        log.info("Successfully created a training. Training id: {} Status: {}", created.getId(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/trainer/{username}", produces = "application/json")
    @Operation(summary = "fetch Trainer's trainings and filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully fetched trainer's trainings with filter", content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Application failed to authenticate user"),
            @ApiResponse(responseCode = "404",
                    description = "Application failed to find a trainer with given username"),
            @ApiResponse(responseCode = "500",
                    description = "Application failed to process the request")
    })
    public ResponseEntity<List<TrainerTrainingResponseDTO>> getTrainingsByTrainerAndFilter(
            @PathVariable("username") String username,
            @RequestHeader("Auth-Username") String authUsername,
            @RequestHeader("Auth-Password") String authPassword,
            @RequestParam(value = "dateFrom", defaultValue = "1900-01-01") LocalDate from,
            @RequestParam(value = "dateTo", defaultValue = "9999-12-31") LocalDate to,
            @RequestParam(value = "traineeName", defaultValue = "") String traineeName) {
        trainingEndpointsHitCounter.increment();
        userService.authentication(new AuthDTO(authUsername, authPassword));
        trainingValidation.validateUsernameNotNull(username);
        List<TrainingEntity> trainings = trainingService.findByTrainerAndFilter(username, from, to, traineeName);
        List<TrainerTrainingResponseDTO> responseDTO = trainings.stream()
                .map(trainingConverter::convertModelToTrainerTraining).toList();
        log.info("Successfully retrieved trainer's trainings and filtered them. Trainer username {}. " +
                "Status: {}", username, HttpStatus.OK.value());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/trainee/{username}", produces = "application/json")
    @Operation(summary = "fetch Trainer's trainings and filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully fetched trainer's trainings with filter", content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Application failed to authenticate user"),
            @ApiResponse(responseCode = "404",
                    description = "Application failed to find a trainer with given username"),
            @ApiResponse(responseCode = "500",
                    description = "Application failed to process the request")
    })
    public ResponseEntity<List<TraineeTrainingResponseDTO>> getTrainingsByTraineeAndFilter(
            @PathVariable("username") String username,
            @RequestHeader("Auth-Username") String authUsername,
            @RequestHeader("Auth-Password") String authPassword,
            @RequestParam(value = "dateFrom", defaultValue = "0000-01-01") LocalDate from,
            @RequestParam(value = "dateTo", defaultValue = "9999-12-31") LocalDate to,
            @RequestParam(value = "trainerName", defaultValue = "") String trainerName,
            @RequestParam(value = "typeId", required = false) Long typeId) {
        trainingEndpointsHitCounter.increment();
        userService.authentication(new AuthDTO(authUsername, authPassword));
        trainingValidation.validateUsernameNotNull(username);
        List<TrainingEntity> trainings = trainingService.findByTraineeAndFilter(
                username, from, to, trainerName, typeId);
        List<TraineeTrainingResponseDTO> responseDTO = trainings.stream()
                .map(trainingConverter::convertModelToTraineeTraining).toList();
        log.info("Successfully retrieved trainee's trainings and filtered them. Trainee username {}. " +
                "Status: {}", username, HttpStatus.OK.value());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
