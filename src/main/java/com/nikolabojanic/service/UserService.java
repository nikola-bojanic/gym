package com.nikolabojanic.service;

import com.nikolabojanic.dao.UserDAO;
import com.nikolabojanic.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private UserDAO userDAO;

    public List<User> profile(List<User> users) {
        List<User> withCredentials = new ArrayList<>();
        Map<String, Integer> usernames = new HashMap<>();
        for (User user : users) {
            String password = generateRandomPassword();
            user.setPassword(password);
            String username = user.getFirstName() + "." + user.getLastName();
            if (usernames.containsKey(username)) {
                Integer count = usernames.get(username);
                String newUsername = username + "." + count++;
                usernames.put(username, count);
                user.setUsername(newUsername);
            } else {
                usernames.put(username, 1);
                user.setUsername(username);
            }
            withCredentials.add(user);
        }
        return withCredentials;
    }

    private String generateRandomPassword() {
        SecureRandom secureRandom = new SecureRandom();
        int[] codePoints = secureRandom.ints(10, 33, 126).toArray();
        return new String(codePoints, 0, codePoints.length);
    }
}
