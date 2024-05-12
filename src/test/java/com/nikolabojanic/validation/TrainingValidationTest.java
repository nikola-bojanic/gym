package com.nikolabojanic.validation;

import com.nikolabojanic.dto.TrainingRequestDTO;
import com.nikolabojanic.exception.ScValidationException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class TrainingValidationTest {

    @InjectMocks
    private TrainingValidation trainingValidation;

    @Test
    void validateNullUsernameNotNullTest() {
        //given
        String username = null;
        //when
        assertThatThrownBy(() -> trainingValidation.validateUsernameNotNull(username))
                //then
                .isInstanceOf(ScValidationException.class)
                .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateUsernameNotNullTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(10);
        //then
        assertDoesNotThrow(() -> trainingValidation.validateUsernameNotNull(username));
    }

    @Test
    void validateNullCreateTrainingRequestTest() {
        //given
        TrainingRequestDTO training = null;
        //when
        assertThatThrownBy(() -> trainingValidation.validateCreateTrainingRequest(training))
                //then
                .isInstanceOf(ScValidationException.class)
                .hasMessage("Cannot create training with null");
    }

    @Test
    void validateNullNameCreateTrainingRequestTest() {
        //given
        TrainingRequestDTO training = new TrainingRequestDTO();
        //when
        assertThatThrownBy(() -> trainingValidation.validateCreateTrainingRequest(training))
                //then
                .isInstanceOf(ScValidationException.class)
                .hasMessage("Cannot create training without a name");
    }

    @Test
    void validateNullDateCreateTrainingRequestTest() {
        //given
        TrainingRequestDTO training = new TrainingRequestDTO();
        training.setName(RandomStringUtils.randomAlphabetic(5));
        //when
        assertThatThrownBy(() -> trainingValidation.validateCreateTrainingRequest(training))
                //then
                .isInstanceOf(ScValidationException.class)
                .hasMessage("Cannot create training without a date");
    }

    @Test
    void validateNullDurationCreateTrainingRequestTest() {
        //given
        TrainingRequestDTO training = new TrainingRequestDTO();
        training.setName(RandomStringUtils.randomAlphabetic(5));
        training.setDate(LocalDate.now());
        //when
        assertThatThrownBy(() -> trainingValidation.validateCreateTrainingRequest(training))
                //then
                .isInstanceOf(ScValidationException.class)
                .hasMessage("Cannot create training without a duration");
    }

    @Test
    void validateNullTraineeUsernameCreateTrainingRequestTest() {
        //given
        TrainingRequestDTO training = new TrainingRequestDTO();
        training.setName(RandomStringUtils.randomAlphabetic(5));
        training.setDate(LocalDate.now());
        training.setDuration(Double.parseDouble(RandomStringUtils.randomNumeric(5)));
        //when
        assertThatThrownBy(() -> trainingValidation.validateCreateTrainingRequest(training))
                //then
                .isInstanceOf(ScValidationException.class)
                .hasMessage("Cannot create training without a trainee username");
    }

    @Test
    void validateNullTrainerUsernameCreateTrainingRequestTest() {
        //given
        TrainingRequestDTO training = new TrainingRequestDTO();
        training.setName(RandomStringUtils.randomAlphabetic(5));
        training.setDate(LocalDate.now());
        training.setDuration(Double.parseDouble(RandomStringUtils.randomNumeric(5)));
        training.setTraineeUsername(RandomStringUtils.randomAlphabetic(10));
        //when
        assertThatThrownBy(() -> trainingValidation.validateCreateTrainingRequest(training))
                //then
                .isInstanceOf(ScValidationException.class)
                .hasMessage("Cannot create training without a trainer username");
    }

    @Test
    void validateCreateTrainingRequestTest() {
        //given
        TrainingRequestDTO training = new TrainingRequestDTO();
        training.setName(RandomStringUtils.randomAlphabetic(5));
        training.setDate(LocalDate.now());
        training.setDuration(Double.parseDouble(RandomStringUtils.randomNumeric(5)));
        training.setTraineeUsername(RandomStringUtils.randomAlphabetic(10));
        training.setTrainerUsername(RandomStringUtils.randomAlphabetic(10));
        //then
        assertDoesNotThrow(() -> trainingValidation.validateCreateTrainingRequest(training));
    }
}
