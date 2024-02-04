package com.nikolabojanic.controller;

import com.nikolabojanic.converter.TrainingTypeConverter;
import com.nikolabojanic.dto.TrainingTypeResponseDto;
import com.nikolabojanic.entity.TrainingTypeEntity;
import com.nikolabojanic.service.TrainingTypeService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController

@RequestMapping("api/v1/training-types")
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;
    private final TrainingTypeConverter trainingTypeConverter;
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
        @Qualifier("trainingTypeEndpointsHitCounter") Counter trainingTypeEndpointsHitCounter) {
        this.trainingTypeService = trainingTypeService;
        this.trainingTypeConverter = trainingTypeConverter;
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
}
