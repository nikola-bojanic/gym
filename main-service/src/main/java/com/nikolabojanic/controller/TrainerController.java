package com.nikolabojanic.controller;

import com.nikolabojanic.converter.TrainerConverter;
import com.nikolabojanic.domain.TrainerDomain;
import com.nikolabojanic.dto.ActiveTrainerResponseDto;
import com.nikolabojanic.dto.RegistrationResponseDto;
import com.nikolabojanic.dto.TrainerRegistrationRequestDto;
import com.nikolabojanic.dto.TrainerResponseDto;
import com.nikolabojanic.dto.TrainerUpdateRequestDto;
import com.nikolabojanic.dto.TrainerUpdateResponseDto;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.service.TrainerService;
import com.nikolabojanic.validation.TrainerValidation;
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
@RequestMapping(value = "api/v1/trainers")
public class TrainerController {
    private final TrainerService trainerService;
    private final TrainerConverter trainerConverter;
    private final TrainerValidation trainerValidation;
    private final Counter trainerEndpointsHitCounter;

    /**
     * Constructor for the TrainerController class, initializing dependencies and components.
     *
     * @param trainerService             The service responsible for handling trainer-related business logic.
     * @param trainerConverter           The converter responsible for converting between DTOs and domain entities for trainers.
     * @param trainerValidation          The validation component responsible for validating trainer-related requests.
     * @param trainerEndpointsHitCounter The counter component to track and monitor the hit count of trainer endpoints.
     */
    public TrainerController(
        TrainerService trainerService,
        TrainerConverter trainerConverter,
        TrainerValidation trainerValidation,
        @Qualifier("trainerEndpointsHitCounter") Counter trainerEndpointsHitCounter) {
        this.trainerService = trainerService;
        this.trainerConverter = trainerConverter;
        this.trainerValidation = trainerValidation;
        this.trainerEndpointsHitCounter = trainerEndpointsHitCounter;
    }

    /**
     * Controller method to fetch information about a trainer by username.
     *
     * @param username The unique identifier of the trainer.
     * @return ResponseEntity containing the TrainerResponseDto and HTTP status.
     */
    @GetMapping(value = "/{username}", produces = "application/json")
    @Operation(summary = "fetch Trainer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Successfully fetched a trainer", content = @Content),
        @ApiResponse(responseCode = "401",
            description = "Application failed to authenticate user"),
        @ApiResponse(responseCode = "404",
            description = "Application failed to find a trainer with given username"),
        @ApiResponse(responseCode = "400",
            description = "Application failed to process the request")
    })
    public ResponseEntity<TrainerResponseDto> getTrainer(
        @PathVariable("username") String username) {
        trainerEndpointsHitCounter.increment();
        trainerValidation.validateUsername(username);
        TrainerEntity trainer = trainerService.findByUsername(username);
        TrainerResponseDto responseDto = trainerConverter.convertModelToResponseDto(trainer);
        log.info("Successfully retrieved trainer with username {}. Status: {}", username, HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Controller method to update the profile of a trainer by username.
     *
     * @param username   The unique identifier of the trainer whose profile is to be updated.
     * @param requestDto The data representing the updates to be applied to the trainer profile.
     * @return ResponseEntity containing the TrainerUpdateResponseDto and HTTP status.
     */
    @PutMapping(value = "/{username}", consumes = "application/json", produces = "application/json")
    @Operation(summary = "update Trainer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Successfully updated a trainer", content = @Content),
        @ApiResponse(responseCode = "401",
            description = "Application failed to authenticate user"),
        @ApiResponse(responseCode = "404",
            description = "Application failed to find a trainer with given username"),
        @ApiResponse(responseCode = "400",
            description = "Application failed to process the request")
    })
    public ResponseEntity<TrainerUpdateResponseDto> updateTrainer(
        @PathVariable("username") String username,
        @RequestBody TrainerUpdateRequestDto requestDto) {
        trainerEndpointsHitCounter.increment();
        trainerValidation.validateUsername(username);
        trainerValidation.validateUpdateTrainerRequest(requestDto);
        TrainerEntity trainer = trainerConverter.convertUpdateRequestToModel(requestDto);
        TrainerEntity updated = trainerService.updateTrainerProfile(username, trainer);
        TrainerUpdateResponseDto responseDto = trainerConverter.convertModelToUpdateResponse(updated);
        log.info("Successfully updated trainer with username {}. Status: {}", username, HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Controller method to register a new trainer.
     *
     * @param requestDto The data representing the trainer registration request.
     * @return ResponseEntity containing the RegistrationResponseDto and HTTP status.
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(summary = "register Trainee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully registered a trainer", content = @Content),
        @ApiResponse(responseCode = "400", description = "Application failed to process the request")
    })
    public ResponseEntity<RegistrationResponseDto> registerTrainer(
        @RequestBody TrainerRegistrationRequestDto requestDto) {
        trainerEndpointsHitCounter.increment();
        trainerValidation.validateRegisterRequest(requestDto);
        TrainerEntity model = trainerConverter.convertRegistrationRequestToModel(requestDto);
        TrainerDomain registered = trainerService.createTrainerProfile(model);
        RegistrationResponseDto responseDto = trainerConverter.convertModelToRegistrationResponse(registered);
        log.info("Successfully registered a trainer with username {}. "
            + "Status: {}", registered.getUsername(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Controller method to fetch a list of active trainers associated with other trainees.
     *
     * @param username The unique identifier of the trainee for whom active trainers are to be fetched.
     * @return ResponseEntity containing the list of ActiveTrainerResponseDto and HTTP status.
     */
    @GetMapping(value = "/active/trainee/{username}", produces = "application/json")
    @Operation(summary = "fetch active Trainers for other trainees")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Successfully fetched active trainers for other trainees", content = @Content),
        @ApiResponse(responseCode = "401",
            description = "Application failed to authenticate user"),
        @ApiResponse(responseCode = "404",
            description = "Application failed to find a trainer with given username"),
        @ApiResponse(responseCode = "400",
            description = "Application failed to process the request")
    })
    public ResponseEntity<List<ActiveTrainerResponseDto>> getActiveTrainers(
        @PathVariable("username") String username) {
        trainerEndpointsHitCounter.increment();
        trainerValidation.validateUsername(username);
        List<TrainerEntity> trainers = trainerService.findActiveForOtherTrainees(username);
        List<ActiveTrainerResponseDto> responseDto = trainers.stream()
            .map(trainerConverter::convertModelToActiveTrainerResponse)
            .toList();
        log.info("Successfully retrieved active trainers for other trainees. Status: {}", HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Controller method to update the active status of a trainer.
     *
     * @param username     The unique identifier of the trainer whose active status is to be updated.
     * @param activeStatus The new active status for the trainer.
     * @return ResponseEntity with HTTP status indicating the success of the active status update.
     */
    @PatchMapping(value = "/active-status/{username}")
    @Operation(summary = "update Trainer active status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Successfully changed trainer's active status", content = @Content),
        @ApiResponse(responseCode = "401",
            description = "Application failed to authenticate user"),
        @ApiResponse(responseCode = "404",
            description = "Application failed to find a trainer with given username"),
        @ApiResponse(responseCode = "400",
            description = "Application failed to process the request")
    })
    public ResponseEntity<Void> changeActiveStatus(
        @PathVariable("username") String username,
        @RequestParam("activeStatus") Boolean activeStatus
    ) {
        trainerEndpointsHitCounter.increment();
        trainerValidation.validateUsername(username);
        trainerService.changeActiveStatus(username, activeStatus);
        log.info("Successfully changed trainer active status. Trainee username {}. "
            + "Status: {}", username, HttpStatus.OK.value());
        return ResponseEntity.ok().build();
    }
}
