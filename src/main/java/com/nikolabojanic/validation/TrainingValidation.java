package com.nikolabojanic.validation;

import com.nikolabojanic.model.TrainingEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class TrainingValidation {
    public void validateCreateTrainingRequest(TrainingEntity training) {
        if (training == null) {
            log.error("Attempted to create training with null");
            throw new IllegalArgumentException("Cannot create training with null");
        } else if (training.getId() != null) {
            log.error("Attempted to create training with an ID");
            throw new IllegalArgumentException("Cannot create training with an id");
        } else if (training.getName() == null || training.getName().isBlank()) {
            log.error("Attempted to create training with missing name");
            throw new IllegalArgumentException("Cannot create training without a name");
        } else if (training.getDate() == null) {
            log.error("Attempted to create training with missing date");
            throw new IllegalArgumentException("Cannot create training without a date");
        } else if (training.getDuration() == null) {
            log.error("Attempted to create training with missing duration");
            throw new IllegalArgumentException("Cannot create training without a duration");
        }
    }

    public void validateDate(LocalDate begin, LocalDate end) {
        if (begin == null || end == null) {
            log.error("Attempted to fetch trainees' trainings with null date values");
            throw new IllegalArgumentException("Date values must not be null");
        }
    }
}
