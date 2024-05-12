package com.nikolabojanic.controller;

import com.nikolabojanic.converter.TrainingTypeConverter;
import com.nikolabojanic.dto.TrainingTypeRequestDto;
import com.nikolabojanic.dto.TrainingTypeResponseDto;
import com.nikolabojanic.entity.TrainingTypeEntity;
import com.nikolabojanic.service.TrainingTypeService;
import com.nikolabojanic.validation.TrainingTypeValidation;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "api/v1/training-types")
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;
    private final TrainingTypeConverter trainingTypeConverter;
    private final TrainingTypeValidation trainingTypeValidation;
    private final Counter trainingTypeEndpointsHitCounter;

    /**
     * Constructor for the TrainingTypeController class, initializing dependencies and components.
     *
     * @param trainingTypeService             The service responsible for handling training type-related business logic.
     * @param trainingTypeConverter           The converter responsible for converting between DTOs and domain entities for training types.
     * @param trainingTypeEndpointsHitCounter The counter component to track and monitor the hit count of training type endpoints.
     */
    public TrainingTypeController(
        TrainingTypeService trainingTypeService,
        TrainingTypeConverter trainingTypeConverter,
        TrainingTypeValidation trainingTypeValidation,
        @Qualifier("trainingTypeEndpointsHitCounter") Counter trainingTypeEndpointsHitCounter) {
        this.trainingTypeService = trainingTypeService;
        this.trainingTypeConverter = trainingTypeConverter;
        this.trainingTypeValidation = trainingTypeValidation;
        this.trainingTypeEndpointsHitCounter = trainingTypeEndpointsHitCounter;
    }

    /**
     * Controller method to fetch all training types.
     *
     * @return ResponseEntity containing the list of TrainingTypeResponseDto and HTTP status.
     */
    @GetMapping(produces = "application/json")
    @Operation(summary = "fetch Training Types")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully fetched Training Types", content = @Content),
        @ApiResponse(responseCode = "401", description = "Application failed to authenticate user"),
        @ApiResponse(responseCode = "400", description = "Application failed to process the request")
    })
    public ResponseEntity<List<TrainingTypeResponseDto>> getAll() {
        trainingTypeEndpointsHitCounter.increment();
        List<TrainingTypeEntity> trainingTypes = trainingTypeService.getAll();
        List<TrainingTypeResponseDto> responseDto = trainingTypes.stream()
            .map(trainingTypeConverter::convertModelToResponse).toList();
        log.info("Successfully retrieved training types. Status: {}", HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Controller method to create a training type.
     *
     * @param requestDto The data representing the trainee registration request.
     * @return ResponseEntity containing the list of TrainingTypeResponseDto and HTTP status.
     */
    @PostMapping(produces = "application/json", consumes = "application/json")
    @Operation(summary = "create a Training Type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created a  Training Type", content = @Content),
        @ApiResponse(responseCode = "401", description = "Application failed to authenticate user"),
        @ApiResponse(responseCode = "400", description = "Application failed to process the request")
    })
    public ResponseEntity<TrainingTypeResponseDto> createType(
        @RequestBody TrainingTypeRequestDto requestDto) {
        trainingTypeEndpointsHitCounter.increment();
        trainingTypeValidation.validateUsername(requestDto.getName());
        TrainingTypeEntity trainingType = trainingTypeConverter.convertDtoToModel(requestDto);
        TrainingTypeEntity savedTrainingType = trainingTypeService.create(trainingType);
        TrainingTypeResponseDto responseDto = trainingTypeConverter.convertModelToResponse(savedTrainingType);
        log.info("Successfully retrieved training types. Status: {}", HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}