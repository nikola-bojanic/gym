package com.nikolabojanic.config;

import com.nikolabojanic.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserFileStorageTest {
    @InjectMocks
    private UserFileStorage userFileStorage;

    @Test
    void testReadAndWriteUsers() throws IOException {
        Map<Long, User> writeUsers = new HashMap<>();
        User user1 = new User(Long.parseLong(RandomStringUtils.randomNumeric(2, 4)), RandomStringUtils.randomAlphabetic(4, 7), RandomStringUtils.randomAlphabetic(3, 6), false);
        User user2 = new User(Long.parseLong(RandomStringUtils.randomNumeric(2, 4)), RandomStringUtils.randomAlphabetic(4, 7), RandomStringUtils.randomAlphabetic(3, 9), true);
        User user3 = new User(Long.parseLong(RandomStringUtils.randomNumeric(2, 4)), RandomStringUtils.randomAlphabetic(4, 7), RandomStringUtils.randomAlphabetic(3, 9), false);
        writeUsers.put(user1.getId(), user1);
        writeUsers.put(user2.getId(), user2);
        writeUsers.put(user3.getId(), user3);
        userFileStorage.setUsers(writeUsers);
        String usersPath = "build/users.csv";
        userFileStorage.setUsersPath(usersPath);

        userFileStorage.writeUsers();
        userFileStorage.readUsers();

        Map<Long, User> readUsers = userFileStorage.getUsers();
        assertEquals(writeUsers.size(), readUsers.size());
        assertEquals(writeUsers.get(user1.getId()), readUsers.get(user1.getId()));
        assertEquals(writeUsers.get(user2.getId()), readUsers.get(user2.getId()));
        assertEquals(writeUsers.get(user3.getId()), readUsers.get(user3.getId()));
    }
}
