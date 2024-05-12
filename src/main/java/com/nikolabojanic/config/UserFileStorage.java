package com.nikolabojanic.config;

import com.nikolabojanic.model.User;
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
public class UserFileStorage {
    @Value("${users.file}")
    private String usersPath;
    private Long maxId = 1L;
    private Map<Long, User> users = new HashMap<>();

    public void writeUsers() throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        for (User user : users.values()) {
            line = String.format(
                    "%d,%s,%s,%s",
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getIsActive()
            );
            lines.add(line);
        }
        Files.write(Path.of(usersPath), lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }


    public void readUsers() throws IOException {
        List<String> lines = Files.readAllLines(Path.of(usersPath));
        for (String line : lines) {
            maxId++;
            String[] tokens = line.split(",");
            if (tokens.length == 4) {
                try {
                    Long id = Long.parseLong(tokens[0]);
                    String firstName = tokens[1];
                    String lastName = tokens[2];
                    Boolean isActive = Boolean.parseBoolean(tokens[3]);
                    User user = new User(id, firstName, lastName, isActive);
                    users.put(id, user);
                } catch (NumberFormatException e) {
                    log.error("Error parsing line {}", line, e);
                }
            } else {
                log.error("Error reading users from file");
                throw new IOException("Invalid line format: " + line);
            }
        }
    }
}