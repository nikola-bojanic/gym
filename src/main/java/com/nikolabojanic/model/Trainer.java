package com.nikolabojanic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Trainer {
    private Long id;
    private User user;
    private Set<Trainee> trainees = new LinkedHashSet<>();
    private Set<Training> trainings = new LinkedHashSet<>();
    private TrainingType specialization;

    public Trainer(Long id) {
        this.id = id;
    }

    public Trainer(Long id, User user, TrainingType trainingType) {
        this.id = id;
        this.user = user;
        this.specialization = trainingType;
    }
}
