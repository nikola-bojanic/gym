package com.nikolabojanic.config;

import com.nikolabojanic.model.Trainer;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Slf4j
@Repository
public class TrainerFileStorage {
    @Value("${trainers.file}")
    private String trainersPath;
    private Long maxId = 1L;
    private Map<Long, Trainer> trainers = new HashMap<>();
    private UserFileStorage userFileStorage;

    public TrainerFileStorage(UserFileStorage userFileStorage) {
        this.userFileStorage = userFileStorage;
    }

    public void writeTrainers() throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        for (Trainer trainer : trainers.values()) {
            line = String.format(
                    "%d,%d,%d",
                    trainer.getId(),
                    trainer.getUser().getId(),
                    trainer.getSpecialization().getId()
            );
            lines.add(line);
        }
        log.info("Saving trainers to .csv file at {}", trainersPath);
        Files.write(Path.of(trainersPath), lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public void readTrainers() throws IOException {
        List<String> lines = Files.readAllLines(Path.of(trainersPath));
        for (String line : lines) {
            maxId++;
            String[] tokens = line.split(",");
            if (tokens.length == 3) {
                try {
                    Long id = Long.parseLong(tokens[0]);
                    Long typeId = Long.parseLong(tokens[2]);
                    Trainer trainer = new Trainer(id, userFileStorage.getUsers().get(Long.parseLong(tokens[1])), new TrainingType(typeId));
                    trainers.put(id, trainer);
                } catch (NumberFormatException e) {
                    log.error("Error parsing line {}", line, e);
                }
            } else {
                log.error("Error reading trainers from file");
                throw new IOException("Invalid line format: " + line);
            }
        }
    }
}
