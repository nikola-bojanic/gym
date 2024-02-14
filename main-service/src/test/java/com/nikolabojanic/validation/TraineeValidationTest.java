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
    void validateNoFirstNameNoLastNameRegisterRequestTest() {
        //given
        List<String> message = List.of(
            "First name must be at least two characters long",
            "Last name must be at least two characters long");
        TraineeRegistrationRequestDto requestDto = new TraineeRegistrationRequestDto();
        //when
        assertThatThrownBy(() -> traineeValidation.validateRegisterRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
    }

    @Test
    void validateBlankFirstNameBlankLastNameRegisterRequestTest() {
        //given
        List<String> message = List.of(
            "First name must be at least two characters long",
            "Last name must be at least two characters long");
        TraineeRegistrationRequestDto requestDto = new TraineeRegistrationRequestDto(
            "    ",
            "    ",
            null,
            null
        );
        //when
        assertThatThrownBy(() -> traineeValidation.validateRegisterRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
    }

    @Test
    void validateShortFirstNameShortLastNameRegisterRequestTest() {
        //given
        List<String> message = List.of(
            "First name must be at least two characters long",
            "Last name must be at least two characters long");
        TraineeRegistrationRequestDto requestDto = new TraineeRegistrationRequestDto(
            RandomStringUtils.randomAlphabetic(1),
            RandomStringUtils.randomAlphabetic(1),
            null,
            null
        );
        //when
        assertThatThrownBy(() -> traineeValidation.validateRegisterRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
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
    void validateNullUsernameNullFirstNameNullLastNameNullActiveStatusUpdateTraineeRequestTest() {
        //given
        TraineeUpdateRequestDto requestDto = new TraineeUpdateRequestDto();
        List<String> message = List.of(
            "Username must be at least 4 characters long",
            "First name must be at least two characters long",
            "Last name must be at least two characters long",
            "Active status must not be null");
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTraineeRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
    }

    @Test
    void validateBlankUsernameBlankFirstNameBlankLastNameUpdateTraineeRequestTest() {
        //given
        TraineeUpdateRequestDto requestDto = new TraineeUpdateRequestDto(
            " ",
            " ",
            " ",
            null,
            null,
            true
        );
        List<String> message = List.of(
            "Username must be at least 4 characters long",
            "First name must be at least two characters long",
            "Last name must be at least two characters long");
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTraineeRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
    }

    @Test
    void validateShortUsernameShortFirstNameShortLastNameUpdateTraineeRequestTest() {
        //given
        TraineeUpdateRequestDto requestDto = new TraineeUpdateRequestDto(
            RandomStringUtils.randomAlphabetic(3),
            RandomStringUtils.randomAlphabetic(1),
            RandomStringUtils.randomAlphabetic(1),
            null,
            null,
            true
        );
        List<String> message = List.of(
            "Username must be at least 4 characters long",
            "First name must be at least two characters long",
            "Last name must be at least two characters long");
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTraineeRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
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
        TraineeTrainerUpdateRequestDto updateRequestDto = new TraineeTrainerUpdateRequestDto();
        List<TraineeTrainerUpdateRequestDto> requestDto = List.of(updateRequestDto);
        List<String> message = List.of(
            "Username " + updateRequestDto.getUsername() + " invalid. Username must be at least 4 characters long");
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTrainersRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
    }

    @Test
    void validateBlankTrainersUsernameUpdateTrainersRequestTest() {
        //given
        TraineeTrainerUpdateRequestDto updateRequestDto = new TraineeTrainerUpdateRequestDto();
        updateRequestDto.setUsername(" ");
        List<TraineeTrainerUpdateRequestDto> requestDto = List.of(updateRequestDto);
        List<String> message = List.of(
            "Username " + updateRequestDto.getUsername() + " invalid. Username must be at least 4 characters long");
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTrainersRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
    }

    @Test
    void validateShortTrainersUsernameUpdateTrainersRequestTest() {
        //given
        TraineeTrainerUpdateRequestDto updateRequestDto = new TraineeTrainerUpdateRequestDto();
        updateRequestDto.setUsername(RandomStringUtils.randomAlphabetic(3));
        List<TraineeTrainerUpdateRequestDto> requestDto = List.of(updateRequestDto);
        List<String> message = List.of(
            "Username " + updateRequestDto.getUsername() + " invalid. Username must be at least 4 characters long");
        //when
        assertThatThrownBy(() -> traineeValidation.validateUpdateTrainersRequest(requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
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
    void validateNullUsernameTest() {
        //given
        String username = null;
        //when
        assertThatThrownBy(() -> traineeValidation.validateUsername(username))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateBlankUsernameTest() {
        //given
        String username = " ";
        //when
        assertThatThrownBy(() -> traineeValidation.validateUsername(username))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateShortUsernameTest() {
        //given
        String username = null;
        //when
        assertThatThrownBy(() -> traineeValidation.validateUsername(username))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateUsernameTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(10);
        //then
        assertDoesNotThrow(() -> traineeValidation.validateUsername(username));
    }

}
