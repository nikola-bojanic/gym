package com.nikolabojanic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "trainer")
public class TrainerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private TrainingTypeEntity specialization;
    @ManyToMany(mappedBy = "trainers")
    private List<TraineeEntity> trainees;
    @OneToMany(mappedBy = "trainer")
    private List<TrainingEntity> trainings;
}
