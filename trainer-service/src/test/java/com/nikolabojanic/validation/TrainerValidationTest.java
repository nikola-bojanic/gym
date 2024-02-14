package com.nikolabojanic.validation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.nikolabojanic.exception.TsValidationException;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerValidationTest {
    @InjectMocks
    private TrainerValidation trainerValidation;

    @Test
    void validateNullUsernameTest() {
        //arrange
        String username = null;
        //act
        assertThatThrownBy(() -> trainerValidation.validateUsernameNotNull(username))
            //assert
            .isInstanceOf(TsValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateShortUsernameTest() {
        //arrange
        String username = RandomStringUtils.randomAlphabetic(3);
        assertThatThrownBy(() -> trainerValidation.validateUsernameNotNull(username))
            //assert
            .isInstanceOf(TsValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateBlankUsernameTest() {
        //arrange
        String username = "    ";
        assertThatThrownBy(() -> trainerValidation.validateUsernameNotNull(username))
            //assert
            .isInstanceOf(TsValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateUsernameTest() {
        //arrange
        String username = RandomStringUtils.randomAlphabetic(10);
        //act
        //assert
        assertDoesNotThrow(() -> trainerValidation.validateUsernameNotNull(username));
    }

    @Test
    void validateNullFirstAndNullLastNameTest() {
        //arrange
        String firstName = null;
        String lastName = null;
        List<String> message = List.of(
            "First name must be at least 2 characters long",
            "Last name must be at least 2 characters long");
        //act
        assertThatThrownBy(() -> trainerValidation.validateFirstAndLastName(firstName, lastName))
            //assert
            .isInstanceOf(TsValidationException.class)
            .hasMessage(message.toString());
    }

    @Test
    void validateBlankFirstAndBlankLastNameTest() {
        //arrange
        String firstName = " ";
        String lastName = " ";
        List<String> message = List.of(
            "First name must be at least 2 characters long",
            "Last name must be at least 2 characters long");
        //act
        assertThatThrownBy(() -> trainerValidation.validateFirstAndLastName(firstName, lastName))
            //assert
            .isInstanceOf(TsValidationException.class)
            .hasMessage(message.toString());
    }

    @Test
    void validateShortFirstAndShortLastNameTest() {
        //arrange
        String firstName = RandomStringUtils.randomAlphabetic(1);
        String lastName = RandomStringUtils.randomAlphabetic(1);
        List<String> message = List.of(
            "First name must be at least 2 characters long",
            "Last name must be at least 2 characters long");
        //act
        assertThatThrownBy(() -> trainerValidation.validateFirstAndLastName(firstName, lastName))
            //assert
            .isInstanceOf(TsValidationException.class)
            .hasMessage(message.toString());
    }

    @Test
    void validateFirstAndLastNameTest() {
        //arrange
        String firstName = RandomStringUtils.randomAlphabetic(2);
        String lastName = RandomStringUtils.randomAlphabetic(2);
        //assert
        assertDoesNotThrow(() -> trainerValidation.validateFirstAndLastName(firstName, lastName));
    }
}