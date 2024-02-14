package com.nikolabojanic.service;

import com.nikolabojanic.converter.MonthConverter;
import com.nikolabojanic.converter.TrainerConverter;
import com.nikolabojanic.converter.YearConverter;
import com.nikolabojanic.dto.TrainerWorkloadRequestDto;
import com.nikolabojanic.entity.MonthEntity;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.entity.YearEntity;
import com.nikolabojanic.exception.TsEntityNotFoundException;
import com.nikolabojanic.exception.TsIllegalOperationException;
import com.nikolabojanic.repository.TrainerRepository;
import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainerConverter trainerConverter;
    private final YearConverter yearConverter;
    private final MonthConverter monthConverter;

    /**
     * Saves a trainer workload request to a database.
     *
     * @param requestDto {@link TrainerWorkloadRequestDto} Workload request to be saved to the database.
     * @return {@link TrainerEntity} Saved trainer.
     */
    public TrainerEntity addTraining(TrainerWorkloadRequestDto requestDto) {
        Optional<TrainerEntity> exists = trainerRepository.findByUsername(requestDto.getUsername());
        if (exists.isPresent()) {
            TrainerEntity existingTrainer = exists.get();
            int updateYear = requestDto.getDate().getYear();
            Month updateMonth = requestDto.getDate().getMonth();
            double updateDuration = requestDto.getDuration();
            YearEntity existingYear = getYear(existingTrainer.getYears(), updateYear);
            if (existingYear == null) {
                MonthEntity month = monthConverter.convertToMonthEntity(updateMonth, updateDuration);
                existingTrainer.getYears().add(yearConverter.convertToYearEntity(updateYear, month));
            } else {
                MonthEntity month = getMonth(existingYear.getMonths(), updateMonth);
                if (month == null) {
                    month = monthConverter.convertToMonthEntity(updateMonth, updateDuration);
                    existingYear.getMonths().add(month);
                    existingYear.setMonths(
                        existingYear.getMonths().stream().sorted(Comparator.comparing(MonthEntity::getMonth)).toList());
                } else {
                    month.setTrainingSummary(month.getTrainingSummary() + updateDuration);
                }
            }
            log.info("Updated trainer's training {}", requestDto.getUsername());
            existingTrainer.setYears(
                existingTrainer.getYears().stream().sorted(Comparator.comparingInt(YearEntity::getYear)).toList());
            return trainerRepository.save(existingTrainer);
        }
        log.info("Saving new trainer: {}", requestDto.getUsername());
        TrainerEntity trainer = trainerConverter.convertToTrainer(requestDto);
        return trainerRepository.save(trainer);
    }

    /**
     * Finds a TrainerEntity by the specified username.
     *
     * @param username The username of the trainer to be found.
     * @return The found TrainerEntity.
     * @throws TsEntityNotFoundException If no trainer with the given username is found.
     */

    public TrainerEntity findByUsername(String username) {
        Optional<TrainerEntity> exists = trainerRepository.findByUsername(username);
        if (exists.isEmpty()) {
            log.error("Attempted to fetch non-existent trainer by username: {}", username);
            throw new TsEntityNotFoundException("Trainer with username - " + username + " doesn't exist.");
        } else {
            log.info("Successfully fetched a trainer: {}", username);
            return exists.get();
        }
    }

    /**
     * Finds a list of TrainerEntities by the specified first and last name.
     *
     * @param firstName The first name of the trainers to be found.
     * @param lastName  The last name of the trainers to be found.
     * @return The list of found TrainerEntities.
     * @throws TsEntityNotFoundException If no trainers with the given names are found.
     */

    public List<TrainerEntity> findByFirstAndLastName(String firstName, String lastName) {
        List<TrainerEntity> trainers =
            trainerRepository.findByFirstNameIgnoreCaseLikeAndLastNameIgnoreCaseLike(firstName, lastName);
        if (trainers.isEmpty()) {
            log.error("Attempted to find non-existing trainers");
            throw new TsEntityNotFoundException("Trainers with name: " + firstName + " " + lastName + " do not exist.");
        }
        log.info("Successfully retrieved trainers");
        return trainers;
    }

    /**
     * Deletes a training entry for a trainer based on the specified username, date, and duration.
     *
     * @param username The username of the trainer for whom the training entry is to be deleted.
     * @param date     The date of the training entry to be deleted.
     * @param duration The duration of the training entry to be deleted.
     * @return The TrainerEntity after the deletion.
     * @throws TsEntityNotFoundException   If the training entry to be deleted is not found.
     * @throws TsIllegalOperationException If deleting the training entry would result in a negative total training summary duration.
     */

    public TrainerEntity deleteTraining(String username, LocalDate date, double duration) {
        Optional<TrainerEntity> exists =
            trainerRepository.findByUsernameAndYearsYearAndYearsMonthsMonth(username, date.getYear(), date.getMonth());
        if (exists.isEmpty()) {
            log.error("Attempted to delete training that doesn't exist. Username {}, year {}, month {}", username,
                date.getYear(), date.getMonth().getValue());
            throw new TsEntityNotFoundException(
                "Training for username: " + username + ", year: " + date.getYear() + " and month: "
                    + date.getMonth().getValue() + " doesn't exist.");
        } else {
            TrainerEntity trainer = exists.get();
            YearEntity year = getYear(trainer.getYears(), date.getYear());
            MonthEntity month = getMonth(year.getMonths(), date.getMonth());
            if (month.getTrainingSummary() - duration < 0) {
                log.error("Attempted to delete training resulting in negative total training summary duration");
                throw new TsIllegalOperationException(
                    "Cannot delete training because it would result in a negative total training summary duration");
            }
            if (month.getTrainingSummary() - duration == 0) {
                trainer.getYears().remove(year);
            } else {
                month.setTrainingSummary(month.getTrainingSummary() - duration);
            }
            log.info("Successfully deleted a training for trainer {}", username);
            return trainerRepository.save(trainer);
        }
    }

    private YearEntity getYear(List<YearEntity> years, int year) {
        return years.stream().filter(y -> y.getYear() == year).findFirst().orElse(null);
    }

    private MonthEntity getMonth(List<MonthEntity> months, Month month) {
        return months.stream().filter(m -> m.getMonth() == month).findFirst().orElse(null);
    }
}