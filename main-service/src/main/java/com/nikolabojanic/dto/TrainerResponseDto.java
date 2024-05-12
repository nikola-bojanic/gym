package com.nikolabojanic.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrainerResponseDto {
    private String firstName;
    private String lastName;
    private Long specializationId;
    private Boolean isActive;
    private List<TrainerTraineeResponseDto> trainees;
}
