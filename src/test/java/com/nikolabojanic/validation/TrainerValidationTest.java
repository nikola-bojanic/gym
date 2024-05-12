package com.nikolabojanic.validation;

import com.nikolabojanic.dto.TrainerRegistrationRequestDTO;
import com.nikolabojanic.dto.TrainerUpdateRequestDTO;
import com.nikolabojanic.exception.SCValidationException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class TrainerValidationTest {

    @InjectMocks
    private TrainerValidation trainerValidation;

    @Test
    void validateNullRegisterRequestTest() {
        //given
        TrainerRegistrationRequestDTO requestDTO = null;
        //when
        assertThatThrownBy(() -> trainerValidation.validateRegisterRequest(requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Cannot create trainer profile with null value");
    }

    @Test
    void validateNoFirstNameRegisterRequestTest() {
        //given
        TrainerRegistrationRequestDTO requestDTO = new TrainerRegistrationRequestDTO();
        //when
        assertThatThrownBy(() -> trainerValidation.validateRegisterRequest(requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("First name must be at least two characters long");
    }

    @Test
    void validateNoLastNameRegisterRequestTest() {
        //given
        TrainerRegistrationRequestDTO requestDTO = new TrainerRegistrationRequestDTO();
        requestDTO.setFirstName(RandomStringUtils.randomAlphabetic(5));
        //when
        assertThatThrownBy(() -> trainerValidation.validateRegisterRequest(requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Last name must be at least two characters long");
    }

    @Test
    void validateNullSpecializationRegisterRequestTest() {
        //given
        TrainerRegistrationRequestDTO requestDTO = new TrainerRegistrationRequestDTO();
        requestDTO.setFirstName(RandomStringUtils.randomAlphabetic(5));
        requestDTO.setLastName(RandomStringUtils.randomAlphabetic(5));
        //then
        assertThatThrownBy(() -> trainerValidation.validateRegisterRequest(requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Cannot create trainer profile with null specialization id");
    }

    @Test
    void validateRegisterRequestTest() {
        //given
        TrainerRegistrationRequestDTO requestDTO = new TrainerRegistrationRequestDTO();
        requestDTO.setFirstName(RandomStringUtils.randomAlphabetic(5));
        requestDTO.setLastName(RandomStringUtils.randomAlphabetic(5));
        requestDTO.setSpecializationId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        //then
        assertDoesNotThrow(() -> trainerValidation.validateRegisterRequest(requestDTO));
    }

    @Test
    void validateNullUpdateTraineeRequestTest() {
        //given
        TrainerUpdateRequestDTO requestDTO = null;
        //when
        assertThatThrownBy(() -> trainerValidation.validateUpdateTrainerRequest(requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Cannot update trainer profile with null request");
    }

    @Test
    void validateNullUsernameUpdateTraineeRequestTest() {
        //given
        TrainerUpdateRequestDTO requestDTO = new TrainerUpdateRequestDTO();
        //when
        assertThatThrownBy(() -> trainerValidation.validateUpdateTrainerRequest(requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateNullFirstNameUpdateTraineeRequestTest() {
        //given
        TrainerUpdateRequestDTO requestDTO = new TrainerUpdateRequestDTO();
        requestDTO.setUsername(RandomStringUtils.randomAlphabetic(10));
        //when
        assertThatThrownBy(() -> trainerValidation.validateUpdateTrainerRequest(requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("First name must be at least two characters long");
    }

    @Test
    void validateNullLastNameUpdateTraineeRequestTest() {
        //given
        TrainerUpdateRequestDTO requestDTO = new TrainerUpdateRequestDTO();
        requestDTO.setUsername(RandomStringUtils.randomAlphabetic(10));
        requestDTO.setFirstName(RandomStringUtils.randomAlphabetic(5));
        //when
        assertThatThrownBy(() -> trainerValidation.validateUpdateTrainerRequest(requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Last name must be at least two characters long");
    }

    @Test
    void validateNullIsActiveUpdateTraineeRequestTest() {
        //given
        TrainerUpdateRequestDTO requestDTO = new TrainerUpdateRequestDTO();
        requestDTO.setUsername(RandomStringUtils.randomAlphabetic(10));
        requestDTO.setFirstName(RandomStringUtils.randomAlphabetic(5));
        requestDTO.setLastName(RandomStringUtils.randomAlphabetic(5));
        //when
        assertThatThrownBy(() -> trainerValidation.validateUpdateTrainerRequest(requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Active status must not be null");
    }

    @Test
    void validateUpdateTraineeRequestTest() {
        //given
        TrainerUpdateRequestDTO requestDTO = new TrainerUpdateRequestDTO();
        requestDTO.setUsername(RandomStringUtils.randomAlphabetic(10));
        requestDTO.setFirstName(RandomStringUtils.randomAlphabetic(5));
        requestDTO.setLastName(RandomStringUtils.randomAlphabetic(5));
        requestDTO.setIsActive(true);
        //then
        assertDoesNotThrow(() -> trainerValidation.validateUpdateTrainerRequest(requestDTO));
    }

    @Test
    void validateNullUsernameNotNullTest() {
        //given
        String username = null;
        //when
        assertThatThrownBy(() -> trainerValidation.validateUsernameNotNull(username))
                //then
                .isInstanceOf(SCValidationException.class)
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
                .isInstanceOf(SCValidationException.class)
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
