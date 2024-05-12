package com.nikolabojanic.dao.impl.memory;

import com.nikolabojanic.config.UserFileStorage;
import com.nikolabojanic.dao.UserDAO;
import com.nikolabojanic.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Repository
public class InMemoryUserDAO implements UserDAO {
    private UserFileStorage userFileStorage;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userFileStorage.getUsers().values());
    }
}
