package com.nikolabojanic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainingType {
    private Long id;
    private String name;
    private List<Training> trainings;
    private List<Trainer> trainers;

    public TrainingType(Long id) {
        this.id = id;
    }
}
