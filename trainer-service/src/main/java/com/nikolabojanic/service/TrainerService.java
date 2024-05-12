package com.nikolabojanic.service;

import com.nikolabojanic.converter.YearConverter;
import com.nikolabojanic.dto.TrainerWorkloadResponseDto;
import com.nikolabojanic.dto.YearDto;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.entity.TrainingEntity;
import com.nikolabojanic.exception.TsEntityNotFoundException;
import com.nikolabojanic.repository.TrainerRepository;
import java.time.Month;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional("transactionManager")
@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final YearConverter yearConverter;
    private final TrainingService trainingService;

    /**
     * Saves a trainer to a database.
     *
     * @param trainer {@link TrainerEntity} Trainer to be saved to the database.
     * @return {@link TrainerEntity} Saved trainer.
     */
    public TrainerEntity addTrainer(TrainerEntity trainer) {
        Optional<TrainerEntity> exists = trainerRepository.findByUsername(trainer.getUsername());
        if (exists.isPresent()) {
            log.info("Attempted to save already existent trainer {}", trainer.getUsername());
            return exists.get();
        }
        log.info("Saving new trainer: {}", trainer.getUsername());
        return trainerRepository.save(trainer);
    }

    /**
     * Calculates workload for a trainer and sorts it by years and months.
     *
     * @param username {@link String} Trainer's username.
     * @return {@link TrainerWorkloadResponseDto} ResponseDto containing trainer's sorted workload.
     */
    public TrainerWorkloadResponseDto getWorkload(String username) {
        Optional<TrainerEntity> exists = trainerRepository.findByUsername(username);
        TrainerWorkloadResponseDto responseDto = new TrainerWorkloadResponseDto();
        if (exists.isEmpty()) {
            log.info("Attempted to fetch trainer with non-existent username: {}", username);
            throw new TsEntityNotFoundException("Trainer " + username + " doesn't exist.");
        }

        List<TrainingEntity> trainings = trainingService.findByTrainer(username);
        Map<Integer, Map<Month, Double>> totalWorkload = new HashMap<>();
        trainings.forEach(t -> {
            Integer year = t.getDate().getYear();
            Month month = t.getDate().getMonth();
            totalWorkload.computeIfAbsent(year, k -> new EnumMap<>(Month.class))
                .merge(month, t.getDuration(), Double::sum);
        });

        List<YearDto> yearlyWorkload = totalWorkload.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(entry -> yearConverter.convertToYear(entry.getKey(), entry.getValue()))
            .toList();

        TrainerEntity trainer = exists.get();
        responseDto.setUsername(trainer.getUsername());
        responseDto.setFirstName(trainer.getFirstName());
        responseDto.setLastName(trainer.getLastName());
        responseDto.setIsActive(trainer.getIsActive());
        responseDto.setWorkload(yearlyWorkload);

        return responseDto;
    }
}
