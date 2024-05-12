package com.nikolabojanic.config;

import com.nikolabojanic.model.Trainee;
import com.nikolabojanic.model.Trainer;
import com.nikolabojanic.model.Training;
import com.nikolabojanic.model.TrainingType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Slf4j
@Repository
public class TrainingFileStorage {
    @Value("${trainings.file}")
    @Setter
    private String trainingsPath;
    @Setter
    private Map<Long, Training> trainings = new HashMap<>();
    @Setter
    private Long maxId = 1L;
    private final TrainerFileStorage trainerFileStorage;
    private final TraineeFileStorage traineeFileStorage;

    public TrainingFileStorage(TrainerFileStorage trainerFileStorage, TraineeFileStorage traineeFileStorage) {
        this.trainerFileStorage = trainerFileStorage;
        this.traineeFileStorage = traineeFileStorage;
    }

    public void readTrainings() throws IOException {
        Map<Long, Trainer> trainers = trainerFileStorage.getTrainers();
        Map<Long, Trainee> trainees = traineeFileStorage.getTrainees();
        List<String> lines = Files.readAllLines(Path.of(trainingsPath));
        for (String line : lines) {
            maxId++; //increment key whenever a new line is found, always keeping the key 1 point above ID. Could have saved the key to file, but I thought it was redundant
            String[] tokens = line.split(",");
            if (tokens.length == 6) {
                try {
                    Long id = Long.parseLong(tokens[0]);
                    Long trainerId = Long.parseLong(tokens[1]);
                    Long traineeId = Long.parseLong(tokens[2]);
                    String name = tokens[3];
                    TrainingType type = new TrainingType();
                    LocalDate date = LocalDate.parse(tokens[4]);
                    Double duration = Double.parseDouble(tokens[5]);
                    Training training = new Training(
                            id,
                            trainees.get(traineeId),
                            trainers.get(trainerId),
                            name,
                            type,
                            date,
                            duration);
                    trainings.put(id, training);
                    trainees.get(traineeId).getTrainings().add(training); //add the training to the trainer's training set because of bidirectional one-to-many relationship
                    trainers.get(trainerId).getTrainings().add(training); //add the training to the trainee's training set -----||-----
                } catch (NumberFormatException | DateTimeParseException | NullPointerException e) {
                    log.error("Error parsing line {}", line, e);

                }
            } else {
                log.error("Error reading trainings from file");
                throw new IOException("Invalid line format: " + line);
            }
        }
        trainerFileStorage.setTrainers(trainers);
        traineeFileStorage.setTrainees(trainees);
    }

    // write to file mapping an object to a single line
    public void writeTrainings() throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        for (Training training : trainings.values()) {
            line = String.format(
                    "%d,%d,%d,%s,%s,%.2f",
                    training.getId(),
                    training.getTrainer().getId(),
                    training.getTrainee().getId(),
                    training.getName(),
                    training.getDate(),
                    training.getDuration());
            lines.add(line);
        }
        Files.write(Path.of(trainingsPath), lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
