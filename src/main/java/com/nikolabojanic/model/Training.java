package com.nikolabojanic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Training {
    private Long id;
    private Trainee trainee;
    private Trainer trainer;
    private String name;
    private TrainingType type;
    private LocalDate date;
    private Double duration;

    public Training(Long id) {
        this.id = id;
    }

    public Training(Long id, Trainee trainee, Trainer trainer) {
        this.id = id;
        this.trainee = trainee;
        this.trainer = trainer;
    }

    public Training(Trainee trainee, Trainer trainer) {
        this.trainee = trainee;
        this.trainer = trainer;
    }
}
