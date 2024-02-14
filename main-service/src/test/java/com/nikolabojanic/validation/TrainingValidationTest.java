package com.nikolabojanic.validation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.nikolabojanic.dto.TrainingRequestDto;
import com.nikolabojanic.exception.ScValidationException;
import java.time.LocalDate;
import java.util.List;
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
    void validateNullUsernameTest() {
        //given
        String username = null;
        //when
        assertThatThrownBy(() -> trainingValidation.validateUsername(username))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateBlankUsernameTest() {
        //given
        String username = " ";
        //when
        assertThatThrownBy(() -> trainingValidation.validateUsername(username))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateShortUsernameTest() {
        //given
        String username = null;
        //when
        assertThatThrownBy(() -> trainingValidation.validateUsername(username))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateUsernameTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(10);
        //then
        assertDoesNotThrow(() -> trainingValidation.validateUsername(username));
    }

    @Test
    void validateNullId() {
        //act
        assertThatThrownBy(() -> trainingValidation.validateId(null))
            //assert
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Id must not be null");

    }

    @Test
    void validateId() {
        //arrange
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(5));
        //assert
        assertDoesNotThrow(() -> trainingValidation.validateId(id));
    }

    @Test
    void validateNullNameNullDateNullDurationNullTraineeUsernameNullTrainerUsernameCreateTrainingRequestTest() {
        //given
        List<String> message = List.of(
            "Cannot create training without a name",
            "Cannot create training without a date",
            "Cannot create training without a duration",
            "Cannot create training without a trainee username",
            "Cannot create training without a trainer username"
        );
        TrainingRequestDto training = new TrainingRequestDto();
        //when
        assertThatThrownBy(() -> trainingValidation.validateCreateTrainingRequest(training))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
    }

    @Test
    void validateBlankNameCreateTrainingRequestTest() {
        //given
        List<String> message = List.of("Cannot create training without a name");
        TrainingRequestDto training = new TrainingRequestDto(
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            " ",
            LocalDate.now(),
            Double.parseDouble(RandomStringUtils.randomNumeric(5))
        );
        //when
        assertThatThrownBy(() -> trainingValidation.validateCreateTrainingRequest(training))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
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
