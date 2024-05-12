package com.nikolabojanic.validation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.nikolabojanic.dto.TrainerRegistrationRequestDto;
import com.nikolabojanic.dto.TrainerUpdateRequestDto;
import com.nikolabojanic.exception.ScValidationException;
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
    void validateNullRegisterRequestTest() {
        //given
        TrainerRegistrationRequestDto requestDto = null;
        //when
        assertThatThrownBy(() -> trainerValidation.validateRegisterRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Cannot create trainer profile with null value");
    }

    @Test
    void validateNullFirstNameNullLastNameNullSpecializationRegisterRequestTest() {
        //given
        TrainerRegistrationRequestDto requestDto = new TrainerRegistrationRequestDto();
        List<String> message = List.of(
            "First name must be at least two characters long",
            "Last name must be at least two characters long",
            "Cannot create trainer profile with null specialization id");
        //when
        assertThatThrownBy(() -> trainerValidation.validateRegisterRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
    }

    @Test
    void validateBlankFirstNameBlankLastNameRegisterRequestTest() {
        //given
        TrainerRegistrationRequestDto requestDto = new TrainerRegistrationRequestDto(
            " ",
            " ",
            Long.parseLong(RandomStringUtils.randomNumeric(1))
        );
        List<String> message = List.of(
            "First name must be at least two characters long",
            "Last name must be at least two characters long");
        //when
        assertThatThrownBy(() -> trainerValidation.validateRegisterRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
    }

    @Test
    void validateShortFirstNameShortLastNameRegisterRequestTest() {
        //given
        TrainerRegistrationRequestDto requestDto = new TrainerRegistrationRequestDto(
            RandomStringUtils.randomAlphabetic(1),
            RandomStringUtils.randomAlphabetic(1),
            Long.parseLong(RandomStringUtils.randomNumeric(1))
        );
        List<String> message = List.of(
            "First name must be at least two characters long",
            "Last name must be at least two characters long");
        //when
        assertThatThrownBy(() -> trainerValidation.validateRegisterRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
    }


    @Test
    void validateRegisterRequestTest() {
        //given
        TrainerRegistrationRequestDto requestDto = new TrainerRegistrationRequestDto();
        requestDto.setFirstName(RandomStringUtils.randomAlphabetic(5));
        requestDto.setLastName(RandomStringUtils.randomAlphabetic(5));
        requestDto.setSpecializationId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        //then
        assertDoesNotThrow(() -> trainerValidation.validateRegisterRequest(requestDto));
    }

    @Test
    void validateNullUpdateTraineeRequestTest() {
        //given
        TrainerUpdateRequestDto requestDto = null;
        //when
        assertThatThrownBy(() -> trainerValidation.validateUpdateTrainerRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Cannot update trainer profile with null request");
    }

    @Test
    void validateNullUsernameNullFirstNameNullLastNameNullActiveStatusUpdateTraineeRequestTest() {
        //given
        List<String> message = List.of(
            "Username must be at least 4 characters long",
            "First name must be at least two characters long",
            "Last name must be at least two characters long",
            "Active status must not be null");
        TrainerUpdateRequestDto requestDto = new TrainerUpdateRequestDto();
        //when
        assertThatThrownBy(() -> trainerValidation.validateUpdateTrainerRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
    }

    @Test
    void validateBlankUsernameBlankFirstNameBlankLastNameUpdateTraineeRequestTest() {
        //given
        List<String> message = List.of(
            "Username must be at least 4 characters long",
            "First name must be at least two characters long",
            "Last name must be at least two characters long");
        TrainerUpdateRequestDto requestDto = new TrainerUpdateRequestDto(
            " ",
            " ",
            " ",
            Long.parseLong(RandomStringUtils.randomNumeric(3)),
            true
        );
        //when
        assertThatThrownBy(() -> trainerValidation.validateUpdateTrainerRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
    }

    @Test
    void validateShortUsernameShortFirstNameShortLastNameUpdateTraineeRequestTest() {
        //given
        List<String> message = List.of(
            "Username must be at least 4 characters long",
            "First name must be at least two characters long",
            "Last name must be at least two characters long");
        TrainerUpdateRequestDto requestDto = new TrainerUpdateRequestDto(
            RandomStringUtils.randomAlphabetic(3),
            RandomStringUtils.randomAlphabetic(1),
            RandomStringUtils.randomAlphabetic(1),
            Long.parseLong(RandomStringUtils.randomNumeric(3)),
            true
        );
        //when
        assertThatThrownBy(() -> trainerValidation.validateUpdateTrainerRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
    }

    @Test
    void validateUpdateTraineeRequestTest() {
        //given
        TrainerUpdateRequestDto requestDto = new TrainerUpdateRequestDto();
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(10));
        requestDto.setFirstName(RandomStringUtils.randomAlphabetic(5));
        requestDto.setLastName(RandomStringUtils.randomAlphabetic(5));
        requestDto.setIsActive(true);
        //then
        assertDoesNotThrow(() -> trainerValidation.validateUpdateTrainerRequest(requestDto));
    }

    @Test
    void validateNullUsernameTest() {
        //given
        String username = null;
        //when
        assertThatThrownBy(() -> trainerValidation.validateUsername(username))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateBlankUsernameTest() {
        //given
        String username = " ";
        //when
        assertThatThrownBy(() -> trainerValidation.validateUsername(username))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateShortUsernameTest() {
        //given
        String username = null;
        //when
        assertThatThrownBy(() -> trainerValidation.validateUsername(username))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateUsernameTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(10);
        //then
        assertDoesNotThrow(() -> trainerValidation.validateUsername(username));
    }

}
