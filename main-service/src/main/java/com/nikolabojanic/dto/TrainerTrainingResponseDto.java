package com.nikolabojanic.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrainerTrainingResponseDto {
    private String name;
    private LocalDate date;
    private Long trainingTypeId;
    private Double duration;
    private String traineeName;
}
