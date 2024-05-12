package com.nikolabojanic.validation;

import com.nikolabojanic.dto.TraineeRegistrationRequestDTO;
import com.nikolabojanic.dto.TraineeTrainerUpdateRequestDTO;
import com.nikolabojanic.dto.TraineeUpdateRequestDTO;
import com.nikolabojanic.exception.SCValidationException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class TraineeValidationTest {
    @InjectMocks
    private TraineeValidation traineeValidation;

    @Test
    void validateNullRegisterRequestTest() {
        //given
        TraineeRegistrationRequestDTO requestDTO = null;
        //when
        assertThatThrownBy(() -> traineeValidation.validateRegisterRequest(requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Cannot create trainee profile with null value");
    }

    @Test
    void validateNoFirstNameRegisterRequestTest() {
        //given
        TraineeRegistrationRequestDTO requestDTO = new TraineeRegistrationRequestDTO();
        //when
        assertThatThrownBy(() -> traineeValidation.validateRegisterRequest(requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("First name must be at least two characters long");

    }

    @Test
    void validateNoLastNameRegisterRequestTest() {
        //given
        TraineeRegistrationRequestDTO requestDTO = new TraineeRegistrationRequestDTO();
        requestDTO.setFirstName(RandomStringUtils.randomAlphabetic(5));
        //when
        assertThatThrownBy(() -> traineeValidation.validateRegisterRequest(requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Last name must be at least two characters long");
    }

    @Test
    void validateRegisterRequestTest() {
        //given
        TraineeRegistrationRequestDTO requestDTO = new TraineeRegistrationRequestDTO();
        requestDTO.setFirstName(RandomStringUtils.randomAlphabetic(5));
        requestDTO.setLastName(RandomStringUtils.randomAlphabetic(5));
        //then
        assertDoesNotThrow(() -> traineeValidation.validateRegisterRequest(requestDTO));
    }

    @Test
    void validateNullUpdateTraineeRequestTest() {
        //given
        TraineeUpdateRequestDTO requestDTO = null;
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTraineeRequest(requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Cannot update a trainee with null value.");
    }

    @Test
    void validateNullUsernameUpdateTraineeRequestTest() {
        //given
        TraineeUpdateRequestDTO requestDTO = new TraineeUpdateRequestDTO();
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTraineeRequest(requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateNullFirstNameUpdateTraineeRequestTest() {
        //given
        TraineeUpdateRequestDTO requestDTO = new TraineeUpdateRequestDTO();
        requestDTO.setUsername(RandomStringUtils.randomAlphabetic(10));
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTraineeRequest(requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("First name must be at least two characters long");
    }

    @Test
    void validateNullLastNameUpdateTraineeRequestTest() {
        //given
        TraineeUpdateRequestDTO requestDTO = new TraineeUpdateRequestDTO();
        requestDTO.setUsername(RandomStringUtils.randomAlphabetic(10));
        requestDTO.setFirstName(RandomStringUtils.randomAlphabetic(5));
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTraineeRequest(requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Last name must be at least two characters long");
    }

    @Test
    void validateNullIsActiveUpdateTraineeRequestTest() {
        //given
        TraineeUpdateRequestDTO requestDTO = new TraineeUpdateRequestDTO();
        requestDTO.setUsername(RandomStringUtils.randomAlphabetic(10));
        requestDTO.setFirstName(RandomStringUtils.randomAlphabetic(5));
        requestDTO.setLastName(RandomStringUtils.randomAlphabetic(5));
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTraineeRequest(requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Active status must not be null");
    }

    @Test
    void validateUpdateTraineeRequestTest() {
        //given
        TraineeUpdateRequestDTO requestDTO = new TraineeUpdateRequestDTO();
        requestDTO.setUsername(RandomStringUtils.randomAlphabetic(10));
        requestDTO.setFirstName(RandomStringUtils.randomAlphabetic(5));
        requestDTO.setLastName(RandomStringUtils.randomAlphabetic(5));
        requestDTO.setIsActive(true);
        //then
        assertDoesNotThrow(() -> traineeValidation.validateUpdateTraineeRequest(requestDTO));
    }

    @Test
    void validateNullTrainersUpdateTrainersRequestTest() {
        //given
        List<TraineeTrainerUpdateRequestDTO> requestDTO = null;
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTrainersRequest(requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Request must not be null");
    }

    @Test
    void validateNullTrainersUsernameUpdateTrainersRequestTest() {
        //given
        List<TraineeTrainerUpdateRequestDTO> requestDTO = List.of(
                new TraineeTrainerUpdateRequestDTO(),
                new TraineeTrainerUpdateRequestDTO());
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTrainersRequest(requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateUpdateTrainersRequestTest() {
        //given
        TraineeTrainerUpdateRequestDTO listMember1 = new TraineeTrainerUpdateRequestDTO(
                RandomStringUtils.randomAlphabetic(10));
        TraineeTrainerUpdateRequestDTO listMember2 = new TraineeTrainerUpdateRequestDTO(
                RandomStringUtils.randomAlphabetic(10));
        List<TraineeTrainerUpdateRequestDTO> requestDTO = List.of(listMember1, listMember2);
        //then
        assertDoesNotThrow(() -> traineeValidation.validateUpdateTrainersRequest(requestDTO));

    }

    @Test
    void validateNullUsernameUsernameNotNullTest() {
        //given
        String username = null;
        //when
        assertThatThrownBy(() -> traineeValidation.validateUsernameNotNull(username))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateUsernameNotNullTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(10);
        //then
        assertDoesNotThrow(() -> traineeValidation.validateUsernameNotNull(username));
    }

    @Test
    void validateNullActiveStatusRequest() {
        String username = RandomStringUtils.randomAlphabetic(10);
        Boolean isActive = null;
        //when
        assertThatThrownBy(() -> traineeValidation.validateActiveStatusRequest(username, isActive))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Active status must not be null");
    }

    @Test
    void validateActiveStatusRequest() {
        String username = RandomStringUtils.randomAlphabetic(10);
        Boolean isActive = true;
        //then
        assertDoesNotThrow(() -> traineeValidation.validateActiveStatusRequest(username, isActive));
    }
}
