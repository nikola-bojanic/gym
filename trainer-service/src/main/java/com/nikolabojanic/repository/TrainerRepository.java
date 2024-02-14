package com.nikolabojanic.repository;

import com.nikolabojanic.entity.TrainerEntity;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends MongoRepository<TrainerEntity, Long> {
    Optional<TrainerEntity> findByUsername(String username);

    List<TrainerEntity> findByFirstNameIgnoreCaseLikeAndLastNameIgnoreCaseLike(String firstName, String lastName);

    Optional<TrainerEntity> findByUsernameAndYearsYearAndYearsMonthsMonth(String username, int year, Month month);
}
