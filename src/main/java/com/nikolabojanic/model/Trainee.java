package com.nikolabojanic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Trainee {

    private Long id;
    private LocalDate dateOfBirth;
    private String address;
    private User user;
    private Set<Trainer> trainers = new LinkedHashSet<>();
    private Set<Training> trainings = new LinkedHashSet<>();

    public Trainee(Long id) {
        this.id = id;
    }

    public Trainee(Long id, LocalDate dateOfBirth, String address, User user) {
        this.id = id;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.user = user;
    }
}
