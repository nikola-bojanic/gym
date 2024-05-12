package com.nikolabojanic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainingRequestDTO {
    private String traineeUsername;
    private String trainerUsername;
    private String name;
    private LocalDate date;
    private Double duration;
}
