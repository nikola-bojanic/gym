package com.nikolabojanic.controller;

import com.nikolabojanic.converter.TrainingConverter;
import com.nikolabojanic.dto.TraineeTrainingResponseDto;
import com.nikolabojanic.dto.TrainerTrainingResponseDto;
import com.nikolabojanic.dto.TrainingRequestDto;
import com.nikolabojanic.dto.TrainingResponseDto;
import com.nikolabojanic.entity.TrainingEntity;
import com.nikolabojanic.service.TrainingService;
import com.nikolabojanic.validation.TrainingValidation;
import io.micrometer.core.instrument.Counter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/trainings")
public class TrainingController {
    private final TrainingService trainingService;
    private final TrainingConverter trainingConverter;
    private final TrainingValidation trainingValidation;
    private final Counter trainingEndpointsHitCounter;

    /**
     * Constructor for the TrainingController class, initializing dependencies and components.
     *
     * @param trainingService             The service responsible for handling training-related business logic.
     * @param trainingConverter           The converter responsible for converting between DTOs and domain entities for training.
     * @param trainingValidation          The validation component responsible for validating training-related requests.
     * @param trainingEndpointsHitCounter The counter component to track and monitor the hit count of training endpoints.
     */
    public TrainingController(
        TrainingService trainingService,
        TrainingConverter trainingConverter,
        TrainingValidation trainingValidation,
        @Qualifier("trainingEndpointsHitCounter") Counter trainingEndpointsHitCounter) {
        this.trainingService = trainingService;
        this.trainingConverter = trainingConverter;
        this.trainingValidation = trainingValidation;
        this.trainingEndpointsHitCounter = trainingEndpointsHitCounter;
    }

    /**
     * Controller method to create a new training.
     *
     * @param requestDto The data representing the training creation request.
     * @return ResponseEntity containing the TrainingResponseDto and HTTP status.
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(summary = "create Training")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created a training", content = @Content),
        @ApiResponse(responseCode = "401", description = "Application failed to authenticate user"),
        @ApiResponse(responseCode = "400", description = "Application failed to process the request")
    })
    public ResponseEntity<TrainingResponseDto> createTraining(
        @RequestBody TrainingRequestDto requestDto) {
        trainingEndpointsHitCounter.increment();
        trainingValidation.validateCreateTrainingRequest(requestDto);
        TrainingEntity domainModel = trainingConverter.convertToEntity(requestDto);
        TrainingEntity created = trainingService.create(domainModel);
        TrainingResponseDto responseDto = trainingConverter.convertToDto(created);
        log.info("Successfully created a training. Training id: {} Status: {}", created.getId(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Controller method to delete a training by its ID.
     *
     * @param id The unique identifier of the training to be deleted.
     * @return ResponseEntity with HTTP status indicating the success of the training deletion.
     */
    @DeleteMapping(produces = "application/json", value = "/{id}")
    @Operation(summary = "delete Training")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created a training", content = @Content),
        @ApiResponse(responseCode = "401", description = "Application failed to authenticate user"),
        @ApiResponse(responseCode = "400", description = "Application failed to process the request")
    })
    public ResponseEntity<Void> deleteTraining(
        @PathVariable("id") Long id) {
        trainingEndpointsHitCounter.increment();
        trainingValidation.validateId(id);
        trainingService.deleteTraining(id);
        log.info("Successfully deleted a training.Status: {}", HttpStatus.OK.value());
        return ResponseEntity.noContent().build();
    }

    /**
     * Controller method to fetch a list of trainer's trainings with optional filtering.
     *
     * @param username    The unique identifier of the trainer for whom trainings are to be fetched.
     * @param from        The start date for filtering trainings (default: 1900-01-01).
     * @param to          The end date for filtering trainings (default: 9999-12-31).
     * @param traineeName The name of the trainee for additional filtering (default: empty string).
     * @return ResponseEntity containing the list of TrainerTrainingResponseDto and HTTP status.
     */
    @GetMapping(value = "/trainer/{username}", produces = "application/json")
    @Operation(summary = "fetch Trainer's trainings and filter")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Successfully fetched trainer's trainings with filter", content = @Content),
        @ApiResponse(responseCode = "401",
            description = "Application failed to authenticate user"),
        @ApiResponse(responseCode = "404",
            description = "Application failed to find a trainer with given username"),
        @ApiResponse(responseCode = "400",
            description = "Application failed to process the request")
    })
    public ResponseEntity<List<TrainerTrainingResponseDto>> getTrainingsByTrainerAndFilter(
        @PathVariable("username") String username,
        @RequestParam(value = "dateFrom", defaultValue = "1900-01-01") LocalDate from,
        @RequestParam(value = "dateTo", defaultValue = "9999-12-31") LocalDate to,
        @RequestParam(value = "traineeName", defaultValue = "") String traineeName) {
        trainingEndpointsHitCounter.increment();
        trainingValidation.validateUsername(username);
        List<TrainingEntity> trainings = trainingService.findByTrainerAndFilter(username, from, to, traineeName);
        List<TrainerTrainingResponseDto> responseDto = trainings.stream()
            .map(trainingConverter::convertModelToTrainerTraining).toList();
        log.info("Successfully retrieved trainer's trainings and filtered them. Trainer username {}. "
            + "Status: {}", username, HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Controller method to fetch a list of trainee's trainings with optional filtering.
     *
     * @param username    The unique identifier of the trainee for whom trainings are to be fetched.
     * @param from        The start date for filtering trainings (default: 0000-01-01).
     * @param to          The end date for filtering trainings (default: 9999-12-31).
     * @param trainerName The name of the trainer for additional filtering (default: empty string).
     * @param typeId      The type identifier for filtering by training type (default: null).
     * @return ResponseEntity containing the list of TraineeTrainingResponseDto and HTTP status.
     */
    @GetMapping(value = "/trainee/{username}", produces = "application/json")
    @Operation(summary = "fetch Trainer's trainings and filter")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Successfully fetched trainer's trainings with filter", content = @Content),
        @ApiResponse(responseCode = "401",
            description = "Application failed to authenticate user"),
        @ApiResponse(responseCode = "404",
            description = "Application failed to find a trainer with given username"),
        @ApiResponse(responseCode = "400",
            description = "Application failed to process the request")
    })
    public ResponseEntity<List<TraineeTrainingResponseDto>> getTrainingsByTraineeAndFilter(
        @PathVariable("username") String username,
        @RequestParam(value = "dateFrom", defaultValue = "0000-01-01") LocalDate from,
        @RequestParam(value = "dateTo", defaultValue = "9999-12-31") LocalDate to,
        @RequestParam(value = "trainerName", defaultValue = "") String trainerName,
        @RequestParam(value = "typeId", required = false) Long typeId) {
        trainingEndpointsHitCounter.increment();
        trainingValidation.validateUsername(username);
        List<TrainingEntity> trainings = trainingService.findByTraineeAndFilter(
            username, from, to, trainerName, typeId);
        List<TraineeTrainingResponseDto> responseDto = trainings.stream()
            .map(trainingConverter::convertModelToTraineeTraining).toList();
        log.info("Successfully retrieved trainee's trainings and filtered them. Trainee username {}. "
            + "Status: {}", username, HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
