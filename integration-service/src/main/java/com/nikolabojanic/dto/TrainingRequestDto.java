package com.nikolabojanic.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainingRequestDto {
    private String traineeUsername;
    private String trainerUsername;
    private String name;
    private LocalDate date;
    private Double duration;
}
