package com.nikolabojanic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
