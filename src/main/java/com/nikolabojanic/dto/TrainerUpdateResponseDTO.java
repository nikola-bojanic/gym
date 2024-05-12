package com.nikolabojanic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerUpdateResponseDTO {
    private String username;
    private String firstName;
    private String lastName;
    private Long specializationId;
    private Boolean isActive;
    private List<TrainerTraineeResponseDTO> trainees;
}
