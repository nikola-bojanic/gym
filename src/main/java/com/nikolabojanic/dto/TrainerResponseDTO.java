package com.nikolabojanic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrainerResponseDTO {
    private String firstName;
    private String lastName;
    private Long specializationId;
    private Boolean isActive;
    private List<TrainerTraineeResponseDTO> trainees;
}
