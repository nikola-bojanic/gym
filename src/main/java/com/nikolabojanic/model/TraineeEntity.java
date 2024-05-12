package com.nikolabojanic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "trainee")
public class TraineeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    private String address;
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToMany
    @JoinTable(name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id"))
    private List<TrainerEntity> trainers;
    @OneToMany(mappedBy = "trainee", cascade = CascadeType.REMOVE)
    private List<TrainingEntity> trainings;
}