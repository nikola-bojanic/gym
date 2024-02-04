package com.nikolabojanic.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerUpdateResponseDto {
    private String username;
    private String firstName;
    private String lastName;
    private Long specializationId;
    private Boolean isActive;
    private List<TrainerTraineeResponseDto> trainees;
}
