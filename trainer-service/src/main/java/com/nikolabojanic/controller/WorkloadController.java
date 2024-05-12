package com.nikolabojanic.controller;

import com.nikolabojanic.dto.TrainerWorkloadRequestDto;
import com.nikolabojanic.dto.TrainerWorkloadResponseDto;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.service.TrainerService;
import com.nikolabojanic.validation.WorkloadValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/workload")
@Slf4j
@RequiredArgsConstructor
public class WorkloadController {
    private final TrainerService trainerService;
    private final WorkloadValidation workloadValidation;


    /**
     * Updates the workload based on the provided {@link TrainerWorkloadRequestDto}.
     *
     * @param username The {@link TrainerEntity} username by which the workload is retrieved.
     * @return A {@link ResponseEntity} that contains the workload object inside its body.
     */
    @Operation(summary = "get Workload")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully fetched a trainer workload", content = @Content),
        @ApiResponse(responseCode = "401", description = "Application failed to authorize user"),
        @ApiResponse(responseCode = "404", description = "Application failed to fetch trainer workload"),
        @ApiResponse(responseCode = "400", description = "Application failed to process the request")
    })
    @GetMapping(value = "/{username}", produces = "application/json")
    public ResponseEntity<TrainerWorkloadResponseDto> getTrainerWorkload(
        @PathVariable("username") String username) {
        workloadValidation.validateUsernameNotNull(username);
        TrainerWorkloadResponseDto responseDto = trainerService.getWorkload(username);
        log.info("Successfully retrieved trainer workload.");
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
