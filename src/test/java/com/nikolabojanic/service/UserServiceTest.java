package com.nikolabojanic.service;

import com.nikolabojanic.dao.UserDAO;
import com.nikolabojanic.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserDAO userDAO;
    @InjectMocks
    private UserService userService;


    @Test
    void profileTest() {
        List<User> inputList = new ArrayList<>();
        User user1 = new User(Long.parseLong(RandomStringUtils.randomNumeric(2, 4)), "first", "name", false);
        User user2 = new User(Long.parseLong(RandomStringUtils.randomNumeric(2, 4)), "first", "name", false);
        User user3 = new User(Long.parseLong(RandomStringUtils.randomNumeric(2, 4)), "name", "first", false);
        inputList.add(user1);
        inputList.add(user2);
        inputList.add(user3);
        String username1 = "first.name";
        String username2 = "first.name.1";
        String username3 = "name.first";

        List<User> outputList = userService.profile(inputList);

        assertEquals(inputList.size(), outputList.size());
        assertEquals(username1, outputList.get(0).getUsername());
        assertEquals(username2, outputList.get(1).getUsername());
        assertEquals(username3, outputList.get(2).getUsername());
        assertNotNull(outputList.get(0).getPassword());
        assertNotNull(outputList.get(1).getPassword());
        assertNotNull(outputList.get(2).getPassword());
    }
}
