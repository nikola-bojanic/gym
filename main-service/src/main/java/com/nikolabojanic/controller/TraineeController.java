package com.nikolabojanic.controller;

import com.nikolabojanic.converter.TraineeConverter;
import com.nikolabojanic.converter.TrainerConverter;
import com.nikolabojanic.domain.TraineeDomain;
import com.nikolabojanic.dto.RegistrationResponseDto;
import com.nikolabojanic.dto.TraineeRegistrationRequestDto;
import com.nikolabojanic.dto.TraineeResponseDto;
import com.nikolabojanic.dto.TraineeTrainerResponseDto;
import com.nikolabojanic.dto.TraineeTrainerUpdateRequestDto;
import com.nikolabojanic.dto.TraineeUpdateRequestDto;
import com.nikolabojanic.dto.TraineeUpdateResponseDto;
import com.nikolabojanic.entity.TraineeEntity;
import com.nikolabojanic.service.TraineeService;
import com.nikolabojanic.validation.TraineeValidation;
import io.micrometer.core.instrument.Counter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "api/v1/trainees")
public class TraineeController {
    private final TraineeService traineeService;
    private final TraineeConverter traineeConverter;
    private final TrainerConverter trainerConverter;
    private final TraineeValidation traineeValidation;
    private final Counter traineeEndpointsHitCounter;

    /**
     * Constructs a new {@link TraineeController} instance.
     *
     * @param traineeService             The {@link TraineeService} responsible for handling trainee-related business logic.
     * @param traineeConverter           The {@link TraineeConverter} used for converting between trainee entities and DTOs.
     * @param trainerConverter           The {@link TrainerConverter} used for converting between trainer entities and DTOs.
     * @param traineeValidation          The {@link TraineeValidation} for validating trainee-related data.
     * @param traineeEndpointsHitCounter The {@link Counter} for tracking hits to trainee-related endpoints.
     */
    public TraineeController(
        TraineeService traineeService,
        TraineeConverter traineeConverter,
        TrainerConverter trainerConverter,
        TraineeValidation traineeValidation,
        @Qualifier("traineeEndpointsHitCounter") Counter traineeEndpointsHitCounter) {
        this.traineeService = traineeService;
        this.traineeConverter = traineeConverter;
        this.trainerConverter = trainerConverter;
        this.traineeValidation = traineeValidation;
        this.traineeEndpointsHitCounter = traineeEndpointsHitCounter;
    }

    /**
     * Controller method to fetch information about a trainee by username.
     *
     * @param username The unique identifier of the trainee.
     * @return ResponseEntity containing the TraineeResponseDto and HTTP status.
     */
    @GetMapping(value = "/{username}", produces = "application/json")
    @Operation(summary = "fetch Trainee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Successfully fetched a trainee", content = @Content),
        @ApiResponse(responseCode = "401",
            description = "Application failed to authenticate user"),
        @ApiResponse(responseCode = "404",
            description = "Application failed to find a trainee with given username"),
        @ApiResponse(responseCode = "400",
            description = "Application failed to process the request")
    })
    public ResponseEntity<TraineeResponseDto> getTrainee(@PathVariable("username") String username) {
        traineeEndpointsHitCounter.increment();
        traineeValidation.validateUsername(username);
        TraineeEntity trainee = traineeService.findByUsername(username);
        TraineeResponseDto responseDto = traineeConverter.convertModelToResponse(trainee);
        log.info("Successfully retrieved trainee with username {}. Status: {}", username, HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Controller method to update the profile of a trainee by username.
     *
     * @param username   The unique identifier of the trainee whose profile is to be updated.
     * @param requestDto The data representing the updates to be applied to the trainee profile.
     * @return ResponseEntity containing the TraineeUpdateResponseDto and HTTP status.
     */
    @PutMapping(value = "/{username}", consumes = "application/json", produces = "application/json")
    @Operation(summary = "update Trainee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Successfully updated a trainee", content = @Content),
        @ApiResponse(responseCode = "401",
            description = "Application failed to authenticate user"),
        @ApiResponse(responseCode = "404",
            description = "Application failed to find a trainee with given username"),
        @ApiResponse(responseCode = "400",
            description = "Application failed to process the request")
    })
    public ResponseEntity<TraineeUpdateResponseDto> updateTrainee(
        @PathVariable("username") String username,
        @RequestBody TraineeUpdateRequestDto requestDto) {
        traineeEndpointsHitCounter.increment();
        traineeValidation.validateUsername(username);
        traineeValidation.validateUpdateTraineeRequest(requestDto);
        TraineeEntity trainee = traineeConverter.convertUpdateRequestToModel(requestDto);
        TraineeEntity updated = traineeService.updateTraineeProfile(username, trainee);
        TraineeUpdateResponseDto responseDto = traineeConverter.convertModelToUpdateResponse(updated);
        log.info("Successfully updated trainee with username {}. Status: {}", username, HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Controller method to register a new trainee.
     *
     * @param requestDto The data representing the trainee registration request.
     * @return ResponseEntity containing the RegistrationResponseDto and HTTP status.
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(summary = "register Trainee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully registered a trainee", content = @Content),
        @ApiResponse(responseCode = "400", description = "Application failed to process the request")
    })
    public ResponseEntity<RegistrationResponseDto> registerTrainee(
        @RequestBody TraineeRegistrationRequestDto requestDto) {
        traineeEndpointsHitCounter.increment();
        traineeValidation.validateRegisterRequest(requestDto);
        TraineeEntity model = traineeConverter.convertRegistrationRequestToModel(requestDto);
        TraineeDomain registered = traineeService.createTraineeProfile(model);
        RegistrationResponseDto responseDto = traineeConverter.convertModelToRegistrationResponse(registered);
        log.info("Successfully registered a trainee with username {}. "
            + "Status: {}", registered.getUsername(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Controller method to delete the profile of a trainee by username.
     *
     * @param username The unique identifier of the trainee whose profile is to be deleted.
     * @return ResponseEntity with HTTP status indicating the success of the deletion.
     */
    @DeleteMapping(value = "/{username}")
    @Operation(summary = "delete Trainee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Successfully deleted a trainee", content = @Content),
        @ApiResponse(responseCode = "401",
            description = "Application failed to authenticate user"),
        @ApiResponse(responseCode = "404",
            description = "Application failed to find a trainee with given username"),
        @ApiResponse(responseCode = "400",
            description = "Application failed to process the request")
    })
    public ResponseEntity<Void> deleteTrainee(
        @PathVariable("username") String username) {
        traineeEndpointsHitCounter.increment();
        traineeValidation.validateUsername(username);
        traineeService.deleteByUsername(username);
        log.info("Successfully deleted trainee with username {}. Status: {}", username, HttpStatus.OK.value());
        return ResponseEntity.ok().build();
    }

    /**
     * Controller method to update the list of trainers associated with a trainee.
     *
     * @param username   The unique identifier of the trainee whose trainers list is to be updated.
     * @param requestDto The list of TraineeTrainerUpdateRequestDto representing the updates to the trainers list.
     * @return ResponseEntity containing the updated list of TraineeTrainerResponseDto and HTTP status.
     */
    @PutMapping(value = "/trainers/{username}", consumes = "application/json")
    @Operation(summary = "update Trainee's trainer list")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Successfully updated trainee's trainer list", content = @Content),
        @ApiResponse(responseCode = "401",
            description = "Application failed to authenticate user"),
        @ApiResponse(responseCode = "404",
            description = "Application failed to find a trainee with given username"),
        @ApiResponse(responseCode = "400",
            description = "Application failed to process the request")
    })
    public ResponseEntity<List<TraineeTrainerResponseDto>> updateTraineesTrainers(
        @PathVariable("username") String username,
        @RequestBody List<TraineeTrainerUpdateRequestDto> requestDto) {
        traineeEndpointsHitCounter.increment();
        traineeValidation.validateUsername(username);
        traineeValidation.validateUpdateTrainersRequest(requestDto);
        TraineeEntity updated = traineeService.updateTraineeTrainers(username, requestDto);
        List<TraineeTrainerResponseDto> responseDto = updated.getTrainers().stream()
            .map(trainerConverter::convertModelToTraineeTrainer).toList();
        log.info("Successfully updated trainee's trainer list. Trainee username {}. "
            + "Status: {}", username, HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Controller method to update the active status of a trainee.
     *
     * @param username     The unique identifier of the trainee whose active status is to be updated.
     * @param activeStatus The new active status for the trainee.
     * @return ResponseEntity with HTTP status indicating the success of the active status update.
     */
    @PatchMapping(value = "/active-status/{username}")
    @Operation(summary = "update Trainee active status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Successfully updated trainee active status", content = @Content),
        @ApiResponse(responseCode = "401",
            description = "Application failed to authenticate user"),
        @ApiResponse(responseCode = "404",
            description = "Application failed to find a trainee with given username"),
        @ApiResponse(responseCode = "400",
            description = "Application failed to process the request")
    })
    public ResponseEntity<Void> changeActiveStatus(
        @PathVariable("username") String username,
        @RequestParam("activeStatus") Boolean activeStatus) {
        traineeEndpointsHitCounter.increment();
        traineeValidation.validateUsername(username);
        traineeService.changeActiveStatus(username, activeStatus);
        log.info("Successfully changed trainee active status. Trainee username {}. "
            + "Status: {}", username, HttpStatus.OK.value());
        return ResponseEntity.ok().build();
    }
}
