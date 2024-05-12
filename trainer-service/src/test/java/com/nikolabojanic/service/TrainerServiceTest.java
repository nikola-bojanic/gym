package com.nikolabojanic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
import java.util.ArrayList;
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
    private TrainerConverter trainerConverter;
    @Mock
    private YearConverter yearConverter;
    @Mock
    private MonthConverter monthConverter;
    @InjectMocks
    private TrainerService trainerService;

    @Test
    void addNonExistingTrainerTest() {
        //arrange
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        TrainerWorkloadRequestDto requestDto = new TrainerWorkloadRequestDto();
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(5));
        when(trainerConverter.convertToTrainer(any(TrainerWorkloadRequestDto.class))).thenReturn(new TrainerEntity());
        when(trainerRepository.save(any(TrainerEntity.class))).thenReturn(new TrainerEntity());
        //act
        TrainerEntity trainer = trainerService.addTraining(requestDto);
        //assert
        assertThat(trainer).isNotNull();
    }

    @Test
    void addNonExistingYearOfTrainingTest() {
        //arrange
        TrainerEntity savedTrainer = new TrainerEntity();
        savedTrainer.setYears(new ArrayList<>(List.of(new YearEntity())));
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(savedTrainer));
        TrainerWorkloadRequestDto requestDto = new TrainerWorkloadRequestDto();
        requestDto.setDate(LocalDate.now());
        requestDto.setDuration(Double.parseDouble(RandomStringUtils.randomNumeric(5)));
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(5));
        when(monthConverter.convertToMonthEntity(any(Month.class), anyDouble())).thenReturn(new MonthEntity());
        when(yearConverter.convertToYearEntity(anyInt(), any(MonthEntity.class))).thenReturn(new YearEntity());
        when(trainerRepository.save(any(TrainerEntity.class))).thenReturn(new TrainerEntity());
        //act
        TrainerEntity trainer = trainerService.addTraining(requestDto);
        //assert
        assertThat(trainer).isNotNull();
    }

    @Test
    void addNonExistingMonthOfTrainingTest() {
        //arrange
        TrainerEntity savedTrainer = new TrainerEntity();
        YearEntity year = new YearEntity();
        year.setYear(LocalDate.now().getYear());
        year.setMonths(new ArrayList<>());
        savedTrainer.setYears(new ArrayList<>(List.of(year)));
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(savedTrainer));
        TrainerWorkloadRequestDto requestDto = new TrainerWorkloadRequestDto();
        requestDto.setDate(LocalDate.now());
        requestDto.setDuration(Double.parseDouble(RandomStringUtils.randomNumeric(5)));
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(5));
        when(monthConverter.convertToMonthEntity(any(Month.class), anyDouble())).thenReturn(new MonthEntity());
        when(trainerRepository.save(any(TrainerEntity.class))).thenReturn(new TrainerEntity());
        //act
        TrainerEntity trainer = trainerService.addTraining(requestDto);
        //assert
        assertThat(trainer).isNotNull();
    }

    @Test
    void addExistingTrainerExistingDateTest() {
        //arrange
        TrainerEntity savedTrainer = new TrainerEntity();
        YearEntity year = new YearEntity();
        year.setYear(LocalDate.now().getYear());
        MonthEntity month = new MonthEntity();
        month.setMonth(LocalDate.now().getMonth());
        month.setTrainingSummary(Double.parseDouble(RandomStringUtils.randomNumeric(5)));
        year.setMonths(new ArrayList<>(List.of(month)));
        savedTrainer.setYears(new ArrayList<>(List.of(year)));
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(savedTrainer));
        TrainerWorkloadRequestDto requestDto = new TrainerWorkloadRequestDto();
        requestDto.setDate(LocalDate.now());
        requestDto.setDuration(Double.parseDouble(RandomStringUtils.randomNumeric(5)));
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(5));
        when(trainerRepository.save(any(TrainerEntity.class))).thenReturn(new TrainerEntity());
        //act
        TrainerEntity trainer = trainerService.addTraining(requestDto);
        //assert
        assertThat(trainer).isNotNull();
    }

    @Test
    void findByUsernameTest() {
        //arrange
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(new TrainerEntity()));
        //act
        TrainerEntity trainer = trainerService.findByUsername(RandomStringUtils.randomAlphabetic(5));
        //assert
        assertThat(trainer).isNotNull();
    }

    @Test
    void findByNonExistingUsernameTest() {
        //arrange
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        String username = RandomStringUtils.randomAlphabetic(5);
        //act
        assertThatThrownBy(() -> trainerService.findByUsername(username))
            //assert
            .isInstanceOf(TsEntityNotFoundException.class)
            .hasMessage("Trainer with username - " + username + " doesn't exist.");
    }

    @Test
    void findByFirstAndLastNameTest() {
        //arrange
        when(trainerRepository.findByFirstNameIgnoreCaseLikeAndLastNameIgnoreCaseLike(anyString(), anyString()))
            .thenReturn(List.of(new TrainerEntity()));
        //act
        List<TrainerEntity> trainers = trainerService.findByFirstAndLastName(
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5)
        );
        assertThat(trainers).isNotEmpty();
    }

    @Test
    void findByNonExistingFirstAndLastNameTest() {
        //arrange
        when(trainerRepository.findByFirstNameIgnoreCaseLikeAndLastNameIgnoreCaseLike(anyString(), anyString()))
            .thenReturn(new ArrayList<>());
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        //act
        assertThatThrownBy(() -> trainerService.findByFirstAndLastName(firstName, lastName))
            //assert
            .isInstanceOf(TsEntityNotFoundException.class)
            .hasMessage("Trainers with name: " + firstName + " " + lastName + " do not exist.");
    }

    @Test
    void deleteSubtractTrainingTest() {
        //arrange
        TrainerEntity trainer = new TrainerEntity();
        YearEntity year = new YearEntity();
        MonthEntity month = new MonthEntity();
        month.setTrainingSummary(50.0);
        month.setMonth(LocalDate.now().getMonth());
        year.setMonths(List.of(month));
        year.setYear(LocalDate.now().getYear());
        trainer.setYears(List.of(year));
        when(trainerRepository.findByUsernameAndYearsYearAndYearsMonthsMonth(anyString(), anyInt(), any(Month.class)))
            .thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(TrainerEntity.class))).thenReturn(new TrainerEntity());
        //act
        TrainerEntity updatedTrainer = trainerService.deleteTraining(
            RandomStringUtils.randomAlphabetic(5),
            LocalDate.now(),
            49.0
        );
        //assert
        assertThat(updatedTrainer).isNotNull();
    }

    @Test
    void deleteYearTrainingTest() {
        //arrange
        TrainerEntity trainer = new TrainerEntity();
        YearEntity year = new YearEntity();
        MonthEntity month = new MonthEntity();
        month.setTrainingSummary(50.0);
        month.setMonth(LocalDate.now().getMonth());
        year.setMonths(List.of(month));
        year.setYear(LocalDate.now().getYear());
        trainer.setYears(new ArrayList<>(List.of(year)));
        when(trainerRepository.findByUsernameAndYearsYearAndYearsMonthsMonth(anyString(), anyInt(), any(Month.class)))
            .thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(TrainerEntity.class))).thenReturn(new TrainerEntity());
        //act
        TrainerEntity updatedTrainer = trainerService.deleteTraining(
            RandomStringUtils.randomAlphabetic(5),
            LocalDate.now(),
            50.0
        );
        //assert
        assertThat(updatedTrainer).isNotNull();
    }

    @Test
    void illegalDeleteTrainingTest() {
        //arrange
        TrainerEntity trainer = new TrainerEntity();
        YearEntity year = new YearEntity();
        MonthEntity month = new MonthEntity();
        month.setTrainingSummary(50.0);
        month.setMonth(LocalDate.now().getMonth());
        year.setMonths(List.of(month));
        year.setYear(LocalDate.now().getYear());
        trainer.setYears(List.of(year));
        when(trainerRepository.findByUsernameAndYearsYearAndYearsMonthsMonth(anyString(), anyInt(), any(Month.class)))
            .thenReturn(Optional.of(trainer));
        //act
        assertThatThrownBy(() -> trainerService.deleteTraining(
            RandomStringUtils.randomAlphabetic(5), LocalDate.now(), 50.1))
            //assert
            .isInstanceOf(TsIllegalOperationException.class)
            .hasMessage("Cannot delete training because it would result in a negative total training summary duration");
    }

    @Test
    void deleteNonExistingTrainingTest() {
        //arrange
        when(trainerRepository.findByUsernameAndYearsYearAndYearsMonthsMonth(anyString(), anyInt(), any(Month.class)))
            .thenReturn(Optional.empty());
        String username = RandomStringUtils.randomAlphabetic(5);
        LocalDate date = LocalDate.now();
        //act
        assertThatThrownBy(() -> trainerService.deleteTraining(username, date, 50.1))
            //assert
            .isInstanceOf(TsEntityNotFoundException.class)
            .hasMessage("Training for username: " + username + ", year: " + date.getYear() + " and month: "
                + date.getMonth().getValue() + " doesn't exist.");
    }
}