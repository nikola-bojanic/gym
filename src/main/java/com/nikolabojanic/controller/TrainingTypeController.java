package com.nikolabojanic.controller;

import com.nikolabojanic.converter.TrainingTypeConverter;
import com.nikolabojanic.dto.TrainingTypeResponseDTO;
import com.nikolabojanic.model.TrainingTypeEntity;
import com.nikolabojanic.service.TrainingTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/training-types")
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;
    private final TrainingTypeConverter trainingTypeConverter;

    @GetMapping(produces = "application/json")
    @Operation(summary = "fetch Training Types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched Training Types", content = @Content),
            @ApiResponse(responseCode = "401", description = "Application failed to authenticate user"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    public ResponseEntity<List<TrainingTypeResponseDTO>> getAll(@RequestHeader("Auth-Username") String authUsername,
                                                                @RequestHeader("Auth-Password") String authPassword
    ) {
        List<TrainingTypeEntity> trainingTypes = trainingTypeService.getAll();
        List<TrainingTypeResponseDTO> responseDTO = trainingTypes.stream()
                .map(trainingTypeConverter::convertModelToResponse).toList();
        log.info("Successfully retrieved training types. Status: {}", HttpStatus.OK.value());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }
}
