package com.nikolabojanic.validation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.nikolabojanic.exception.TsValidationException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorkloadValidationTest {
    @InjectMocks
    private WorkloadValidation workloadValidation;

    @Test
    void validateNullUsernameTest() {
        //arrange
        String username = null;
        //act
        assertThatThrownBy(() -> workloadValidation.validateUsernameNotNull(username))
            //assert
            .isInstanceOf(TsValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateShortUsernameTest() {
        //arrange
        String username = RandomStringUtils.randomAlphabetic(3);
        assertThatThrownBy(() -> workloadValidation.validateUsernameNotNull(username))
            //assert
            .isInstanceOf(TsValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateBlankUsernameTest() {
        //arrange
        String username = "    ";
        assertThatThrownBy(() -> workloadValidation.validateUsernameNotNull(username))
            //assert
            .isInstanceOf(TsValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }
}