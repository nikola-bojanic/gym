package com.nikolabojanic.controller;

import com.nikolabojanic.converter.TrainerConverter;
import com.nikolabojanic.dto.TrainerWorkloadResponseDto;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.service.TrainerService;
import com.nikolabojanic.validation.TrainerValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/trainers")
@Slf4j
@RequiredArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;
    private final TrainerConverter trainerConverter;
    private final TrainerValidation trainerValidation;

    /**
     * Retrieves the trainer information based on the provided username.
     *
     * @param username The {@link TrainerEntity} username by which the trainer information is retrieved.
     * @return A {@link ResponseEntity} that contains the trainer object inside its body.
     */
    @Operation(summary = "get Trainer's Training summary")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Successfully fetched trainer's training summary", content = @Content),
        @ApiResponse(responseCode = "401", description = "Application failed to authorize user"),
        @ApiResponse(responseCode = "404", description = "Application failed to fetch trainer information"),
        @ApiResponse(responseCode = "400", description = "Application failed to process the request")
    })
    @GetMapping(value = "/{username}", produces = "application/json")
    public ResponseEntity<TrainerWorkloadResponseDto> getTrainingSummary(
        @PathVariable("username") String username) {
        trainerValidation.validateUsernameNotNull(username);
        TrainerEntity trainerEntity = trainerService.findByUsername(username);
        TrainerWorkloadResponseDto responseDto = trainerConverter.convertToResponseDto(trainerEntity);
        log.info("Successfully retrieved trainer information.");
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Retrieves a list of trainers based on their first and last names.
     *
     * @param firstName The first name of the trainers to be retrieved.
     * @param lastName  The last name of the trainers to be retrieved.
     * @return A ResponseEntity containing a list of TrainerWorkloadResponseDto objects representing the trainers.
     */
    @Operation(summary = "get Trainer information by first name and last name")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Successfully fetched trainers by their name", content = @Content),
        @ApiResponse(responseCode = "401", description = "Application failed to authorize user"),
        @ApiResponse(responseCode = "404", description = "Application failed to fetch trainer information"),
        @ApiResponse(responseCode = "400", description = "Application failed to process the request")
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<TrainerWorkloadResponseDto>> findTrainersByName(
        @RequestParam("firstName") String firstName,
        @RequestParam("lastName") String lastName
    ) {
        trainerValidation.validateFirstAndLastName(firstName, lastName);
        List<TrainerEntity> trainers = trainerService.findByFirstAndLastName(firstName, lastName);
        List<TrainerWorkloadResponseDto> responseDto = trainers.stream()
            .map(trainerConverter::convertToResponseDto)
            .toList();
        log.info("Successfully retrieved trainers by their first and last name.");
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}