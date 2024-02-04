package com.nikolabojanic.validation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.nikolabojanic.dto.TrainingRequestDto;
import com.nikolabojanic.exception.ScValidationException;
import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

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
        TrainingRequestDto training = null;
        //when
        assertThatThrownBy(() -> trainingValidation.validateCreateTrainingRequest(training))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Cannot create training with null");
    }

    @Test
    void validateNullNameCreateTrainingRequestTest() {
        //given
        TrainingRequestDto training = new TrainingRequestDto();
        //when
        assertThatThrownBy(() -> trainingValidation.validateCreateTrainingRequest(training))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Cannot create training without a name");
    }

    @Test
    void validateNullDateCreateTrainingRequestTest() {
        //given
        TrainingRequestDto training = new TrainingRequestDto();
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
        TrainingRequestDto training = new TrainingRequestDto();
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
        TrainingRequestDto training = new TrainingRequestDto();
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
        TrainingRequestDto training = new TrainingRequestDto();
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
        TrainingRequestDto training = new TrainingRequestDto();
        training.setName(RandomStringUtils.randomAlphabetic(5));
        training.setDate(LocalDate.now());
        training.setDuration(Double.parseDouble(RandomStringUtils.randomNumeric(5)));
        training.setTraineeUsername(RandomStringUtils.randomAlphabetic(10));
        training.setTrainerUsername(RandomStringUtils.randomAlphabetic(10));
        //then
        assertDoesNotThrow(() -> trainingValidation.validateCreateTrainingRequest(training));
    }
}
