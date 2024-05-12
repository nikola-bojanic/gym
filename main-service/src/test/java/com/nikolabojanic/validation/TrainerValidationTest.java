package com.nikolabojanic.validation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.nikolabojanic.dto.TrainerRegistrationRequestDto;
import com.nikolabojanic.dto.TrainerUpdateRequestDto;
import com.nikolabojanic.exception.ScValidationException;
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
    void validateNoFirstNameRegisterRequestTest() {
        //given
        TrainerRegistrationRequestDto requestDto = new TrainerRegistrationRequestDto();
        //when
        assertThatThrownBy(() -> trainerValidation.validateRegisterRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("First name must be at least two characters long");
    }

    @Test
    void validateNoLastNameRegisterRequestTest() {
        //given
        TrainerRegistrationRequestDto requestDto = new TrainerRegistrationRequestDto();
        requestDto.setFirstName(RandomStringUtils.randomAlphabetic(5));
        //when
        assertThatThrownBy(() -> trainerValidation.validateRegisterRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Last name must be at least two characters long");
    }

    @Test
    void validateNullSpecializationRegisterRequestTest() {
        //given
        TrainerRegistrationRequestDto requestDto = new TrainerRegistrationRequestDto();
        requestDto.setFirstName(RandomStringUtils.randomAlphabetic(5));
        requestDto.setLastName(RandomStringUtils.randomAlphabetic(5));
        //then
        assertThatThrownBy(() -> trainerValidation.validateRegisterRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Cannot create trainer profile with null specialization id");
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
    void validateNullUsernameUpdateTraineeRequestTest() {
        //given
        TrainerUpdateRequestDto requestDto = new TrainerUpdateRequestDto();
        //when
        assertThatThrownBy(() -> trainerValidation.validateUpdateTrainerRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateNullFirstNameUpdateTraineeRequestTest() {
        //given
        TrainerUpdateRequestDto requestDto = new TrainerUpdateRequestDto();
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(10));
        //when
        assertThatThrownBy(() -> trainerValidation.validateUpdateTrainerRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("First name must be at least two characters long");
    }

    @Test
    void validateNullLastNameUpdateTraineeRequestTest() {
        //given
        TrainerUpdateRequestDto requestDto = new TrainerUpdateRequestDto();
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(10));
        requestDto.setFirstName(RandomStringUtils.randomAlphabetic(5));
        //when
        assertThatThrownBy(() -> trainerValidation.validateUpdateTrainerRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Last name must be at least two characters long");
    }

    @Test
    void validateNullIsActiveUpdateTraineeRequestTest() {
        //given
        TrainerUpdateRequestDto requestDto = new TrainerUpdateRequestDto();
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(10));
        requestDto.setFirstName(RandomStringUtils.randomAlphabetic(5));
        requestDto.setLastName(RandomStringUtils.randomAlphabetic(5));
        //when
        assertThatThrownBy(() -> trainerValidation.validateUpdateTrainerRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Active status must not be null");
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
    void validateNullUsernameNotNullTest() {
        //given
        String username = null;
        //when
        assertThatThrownBy(() -> trainerValidation.validateUsernameNotNull(username))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateUsernameNotNullTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(10);
        //then
        assertDoesNotThrow(() -> trainerValidation.validateUsernameNotNull(username));
    }

    @Test
    void validateNullActiveStatusRequest() {
        String username = RandomStringUtils.randomAlphabetic(10);
        Boolean isActive = null;
        //when
        assertThatThrownBy(() -> trainerValidation.validateActiveStatusRequest(username, isActive))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Active status must not be null");
    }

    @Test
    void validateActiveStatusRequest() {
        String username = RandomStringUtils.randomAlphabetic(10);
        Boolean isActive = true;
        //then
        assertDoesNotThrow(() -> trainerValidation.validateActiveStatusRequest(username, isActive));
    }
}
