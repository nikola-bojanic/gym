package com.nikolabojanic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TraineeTrainingResponseDTO {
    private String name;
    private LocalDate date;
    private Long trainingTypeId;
    private Double duration;
    private String trainerName;
}
