package com.nikolabojanic.validation;

import com.nikolabojanic.exception.SCValidationException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class TrainingTypeValidationTest {
    @InjectMocks
    private TrainingTypeValidation trainingTypeValidation;

    @Test
    void validateNullIdIdNotNullTest() {
        //given
        Long id = null;
        //when
        assertThatThrownBy(() -> trainingTypeValidation.validateIdNotNull(id))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Training type ID cannot be null");
    }

    @Test
    void validateIdNotNullTest() {
        //given
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(7));
        //then
        assertDoesNotThrow(() -> trainingTypeValidation.validateIdNotNull(id));
    }
}