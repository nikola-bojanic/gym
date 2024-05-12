package com.nikolabojanic.dao;

import com.nikolabojanic.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerDAO {
    List<Trainer> getAll();

    Optional<Trainer> findById(Long id);

    Trainer save(Trainer trainer);
}
