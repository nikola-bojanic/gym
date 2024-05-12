package com.nikolabojanic.validation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.nikolabojanic.dto.TraineeRegistrationRequestDto;
import com.nikolabojanic.dto.TraineeTrainerUpdateRequestDto;
import com.nikolabojanic.dto.TraineeUpdateRequestDto;
import com.nikolabojanic.exception.ScValidationException;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraineeValidationTest {
    @InjectMocks
    private TraineeValidation traineeValidation;

    @Test
    void validateNullRegisterRequestTest() {
        //given
        TraineeRegistrationRequestDto requestDto = null;
        //when
        assertThatThrownBy(() -> traineeValidation.validateRegisterRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Cannot create trainee profile with null value");
    }

    @Test
    void validateNoFirstNameRegisterRequestTest() {
        //given
        TraineeRegistrationRequestDto requestDto = new TraineeRegistrationRequestDto();
        //when
        assertThatThrownBy(() -> traineeValidation.validateRegisterRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("First name must be at least two characters long");

    }

    @Test
    void validateNoLastNameRegisterRequestTest() {
        //given
        TraineeRegistrationRequestDto requestDto = new TraineeRegistrationRequestDto();
        requestDto.setFirstName(RandomStringUtils.randomAlphabetic(5));
        //when
        assertThatThrownBy(() -> traineeValidation.validateRegisterRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Last name must be at least two characters long");
    }

    @Test
    void validateRegisterRequestTest() {
        //given
        TraineeRegistrationRequestDto requestDto = new TraineeRegistrationRequestDto();
        requestDto.setFirstName(RandomStringUtils.randomAlphabetic(5));
        requestDto.setLastName(RandomStringUtils.randomAlphabetic(5));
        //then
        assertDoesNotThrow(() -> traineeValidation.validateRegisterRequest(requestDto));
    }

    @Test
    void validateNullUpdateTraineeRequestTest() {
        //given
        TraineeUpdateRequestDto requestDto = null;
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTraineeRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Cannot update a trainee with null value.");
    }

    @Test
    void validateNullUsernameUpdateTraineeRequestTest() {
        //given
        TraineeUpdateRequestDto requestDto = new TraineeUpdateRequestDto();
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTraineeRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateNullFirstNameUpdateTraineeRequestTest() {
        //given
        TraineeUpdateRequestDto requestDto = new TraineeUpdateRequestDto();
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(10));
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTraineeRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("First name must be at least two characters long");
    }

    @Test
    void validateNullLastNameUpdateTraineeRequestTest() {
        //given
        TraineeUpdateRequestDto requestDto = new TraineeUpdateRequestDto();
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(10));
        requestDto.setFirstName(RandomStringUtils.randomAlphabetic(5));
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTraineeRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Last name must be at least two characters long");
    }

    @Test
    void validateNullIsActiveUpdateTraineeRequestTest() {
        //given
        TraineeUpdateRequestDto requestDto = new TraineeUpdateRequestDto();
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(10));
        requestDto.setFirstName(RandomStringUtils.randomAlphabetic(5));
        requestDto.setLastName(RandomStringUtils.randomAlphabetic(5));
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTraineeRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Active status must not be null");
    }

    @Test
    void validateUpdateTraineeRequestTest() {
        //given
        TraineeUpdateRequestDto requestDto = new TraineeUpdateRequestDto();
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(10));
        requestDto.setFirstName(RandomStringUtils.randomAlphabetic(5));
        requestDto.setLastName(RandomStringUtils.randomAlphabetic(5));
        requestDto.setIsActive(true);
        //then
        assertDoesNotThrow(() -> traineeValidation.validateUpdateTraineeRequest(requestDto));
    }

    @Test
    void validateNullTrainersUpdateTrainersRequestTest() {
        //given
        List<TraineeTrainerUpdateRequestDto> requestDto = null;
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTrainersRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Request must not be null");
    }

    @Test
    void validateNullTrainersUsernameUpdateTrainersRequestTest() {
        //given
        List<TraineeTrainerUpdateRequestDto> requestDto = List.of(
            new TraineeTrainerUpdateRequestDto(),
            new TraineeTrainerUpdateRequestDto());
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTrainersRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateUpdateTrainersRequestTest() {
        //given
        TraineeTrainerUpdateRequestDto listMember1 = new TraineeTrainerUpdateRequestDto(
            RandomStringUtils.randomAlphabetic(10));
        TraineeTrainerUpdateRequestDto listMember2 = new TraineeTrainerUpdateRequestDto(
            RandomStringUtils.randomAlphabetic(10));
        List<TraineeTrainerUpdateRequestDto> requestDto = List.of(listMember1, listMember2);
        //then
        assertDoesNotThrow(() -> traineeValidation.validateUpdateTrainersRequest(requestDto));

    }

    @Test
    void validateNullUsernameUsernameNotNullTest() {
        //given
        String username = null;
        //when
        assertThatThrownBy(() -> traineeValidation.validateUsernameNotNull(username))
            //then
            .isInstanceOf(ScValidationException.class)
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
            .isInstanceOf(ScValidationException.class)
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
