package com.nikolabojanic.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class DataInitializer {
    private TraineeFileStorage traineeFileStorage;
    private TrainerFileStorage trainerFileStorage;
    private UserFileStorage userFileStorage;
    private TrainingFileStorage trainingFileStorage;

    @PostConstruct
    private void init() {
        //First assign trainers and trainers
        //Then load their primary_key pairs from files to simulate a many-to-many relationship
        //At the end(because of referential integrity) load trainings and assign a trainer and a trainee to training
        try {
            log.info("Populating users.");
            userFileStorage.readUsers();
            log.info("Populating trainers.");
            trainerFileStorage.readTrainers();
            log.info("Populating trainees.");
            traineeFileStorage.readTrainees();
            traineeFileStorage.readTraineesTrainers();
            log.info("Populating trainings.");
            trainingFileStorage.readTrainings();
        } catch (IOException e) {
            log.error("Error loading instances into memory", e);
        }
    }
}