package com.nikolabojanic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "training")
public class TrainingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private TraineeEntity trainee;
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private TrainerEntity trainer;
    @Column(nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private TrainingTypeEntity type;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private Double duration;


}
