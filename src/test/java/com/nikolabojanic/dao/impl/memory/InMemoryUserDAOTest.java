package com.nikolabojanic.dao.impl.memory;

import com.nikolabojanic.config.UserFileStorage;
import com.nikolabojanic.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InMemoryUserDAOTest {
    @Mock
    private UserFileStorage userFileStorage;
    @InjectMocks
    private InMemoryUserDAO inMemoryUserDAO;


    @Test
    void getAllTest() {
        User user1 = new User(Long.parseLong(RandomStringUtils.randomNumeric(2, 4)));
        User user2 = new User(Long.parseLong(RandomStringUtils.randomNumeric(2, 4)));
        Map<Long, User> testUsers = new HashMap<>();
        testUsers.put(2L, user1);
        testUsers.put(1L, user2);
        when(userFileStorage.getUsers()).thenReturn(testUsers);

        List<User> readUsers = inMemoryUserDAO.getAll();

        assertEquals(testUsers.size(), readUsers.size());
        assertEquals(testUsers.get(1L), readUsers.get(0));
        assertEquals(testUsers.get(2L), readUsers.get(1));
    }

}
