package com.nikolabojanic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.nikolabojanic.converter.YearConverter;
import com.nikolabojanic.dto.TrainerWorkloadResponseDto;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.entity.TrainingEntity;
import com.nikolabojanic.exception.TsEntityNotFoundException;
import com.nikolabojanic.repository.TrainerRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private YearConverter yearConverter;
    @Mock
    private TrainingService trainingService;
    @InjectMocks
    private TrainerService trainerService;

    @Test
    void addExistingTrainerTest() {
        //arrange
        TrainerEntity trainer = TrainerEntity.builder()
            .username(RandomStringUtils.randomAlphabetic(5))
            .build();
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(new TrainerEntity()));
        //act
        TrainerEntity addedTrainer = trainerService.addTrainer(trainer);
        //assert
        assertThat(addedTrainer).isNotNull();
    }

    @Test
    void addTrainerTest() {
        //arrange
        TrainerEntity trainer = TrainerEntity.builder()
            .username(RandomStringUtils.randomAlphabetic(5))
            .build();
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(trainerRepository.save(any(TrainerEntity.class))).thenReturn(new TrainerEntity());
        //act
        TrainerEntity addedTrainer = trainerService.addTrainer(trainer);
        //assert
        assertThat(addedTrainer).isNotNull();
    }

    @Test
    void getWorkloadTest() {
        //arrange
        String username = RandomStringUtils.randomAlphabetic(5);
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(new TrainerEntity()));
        TrainingEntity training = new TrainingEntity(
            Long.parseLong(RandomStringUtils.randomNumeric(5)),
            new TrainerEntity(),
            LocalDate.now(),
            Double.parseDouble(RandomStringUtils.randomNumeric(5))
        );
        when(trainingService.findByTrainer(anyString())).thenReturn(List.of(training));
        //act
        TrainerWorkloadResponseDto responseDto = trainerService.getWorkload(username);
        //assert
        assertThat(responseDto).isNotNull();
    }

    @Test
    void getWorkloadForNonExistentTrainerTest() {
        //arrange
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        String username = RandomStringUtils.randomAlphabetic(5);
        //act
        assertThatThrownBy(() -> trainerService.getWorkload(username))
            //assert
            .isInstanceOf(TsEntityNotFoundException.class)
            .hasMessage("Trainer " + username + " doesn't exist.");
    }
}