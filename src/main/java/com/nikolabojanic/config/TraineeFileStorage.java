package com.nikolabojanic.config;

import com.nikolabojanic.model.Trainee;
import com.nikolabojanic.model.Trainer;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Slf4j
@Repository
public class TraineeFileStorage {
    @Setter
    @Value("${trainees.file}")
    private String traineesPath;
    @Setter
    @Value("${traineesTrainers.file}")
    private String traineesTrainersPath;
    @Setter
    private Long maxId = 1L;
    @Setter
    private Map<Long, Trainee> trainees = new HashMap<>();
    private final UserFileStorage userFileStorage;
    private final TrainerFileStorage trainerFileStorage;

    public TraineeFileStorage(UserFileStorage userFileStorage, TrainerFileStorage trainerFileStorage) {
        this.userFileStorage = userFileStorage;
        this.trainerFileStorage = trainerFileStorage;
    }

    public void writeTrainees() throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        for (Trainee trainee : trainees.values()) {
            line = String.format(
                    "%d,%s,%s,%d",
                    trainee.getId(),
                    trainee.getDateOfBirth(),
                    trainee.getAddress(),
                    trainee.getUser().getId()
            );
            lines.add(line);
        }
        Files.write(Path.of(traineesPath), lines, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
    }

    public void readTrainees() throws IOException {
        List<String> lines = Files.readAllLines(Path.of(traineesPath));
        for (String line : lines) {
            maxId++;
            String[] tokens = line.split(",");
            if (tokens.length == 4) {
                try {
                    Long id = Long.parseLong(tokens[0]);
                    LocalDate date = LocalDate.parse(tokens[1]);
                    String address = tokens[2];
                    Trainee trainee = new Trainee(id, date, address, userFileStorage.getUsers().get(Long.parseLong(tokens[3])));
                    trainees.put(id, trainee);
                } catch (NumberFormatException e) {
                    log.error("Error parsing line {}", line, e);
                }
            } else {
                log.error("Error reading trainees from file");
                throw new IOException("Invalid line format: " + line);
            }
        }
    }

    public void writeTraineesTrainers() throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        for (Trainee trainee : trainees.values()) {
            for (Trainer trainer : trainee.getTrainers()) {
                line = String.format(
                        "%d,%d",
                        trainee.getId(),
                        trainer.getId()
                );
                lines.add(line);
            }
        }
        Files.write(Path.of(traineesTrainersPath), lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public void readTraineesTrainers() throws IOException {
        List<String> lines = Files.readAllLines(Path.of(traineesTrainersPath));
        Map<Long, Trainer> trainers = trainerFileStorage.getTrainers();
        for (String line : lines) {
            String[] tokens = line.split(",");
            if (tokens.length == 2) {
                try {
                    Trainee trainee = trainees.get(Long.parseLong(tokens[0]));
                    Trainer trainer = trainers.get(Long.parseLong(tokens[1]));
                    trainee.getTrainers().add(trainer); //add trainer to trainee's trainer set
                    trainer.getTrainees().add(trainee); //add trainee to trainer's trainee list
                } catch (NumberFormatException e) {
                    log.error("Error parsing line {}", line, e);
                }
            } else {
                log.error("Error reading trainee_trainer relationships from file.");
                throw new IOException("Invalid line format: " + line);
            }
        }
        trainerFileStorage.setTrainers(trainers); //set trainers
    }
}
