package com.nikolabojanic.validation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.nikolabojanic.exception.ScValidationException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

@ExtendWith(MockitoExtension.class)
class TrainingTypeValidationTest {
    @InjectMocks
    private TrainingTypeValidation trainingTypeValidation;

    private static Stream<Arguments> validateUsernameSource() {
        String name = null;
        return Stream.of(
            Arguments.of(
                name
            ),
            Arguments.of(
                "  "
            ),
            Arguments.of(
                RandomStringUtils.randomAlphabetic(1)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("validateUsernameSource")
    void validateBadUsernameTest(String name) {
        assertThatThrownBy(() -> trainingTypeValidation.validateUsername(name))
            .hasMessage("Training type name must be at least 2 characters long")
            .isInstanceOf(ScValidationException.class);
    }

    @Test
    void validateUsernameTest() {
        //arrange
        String name = RandomStringUtils.randomAlphabetic(2);
        //act
        assertDoesNotThrow(() -> trainingTypeValidation.validateUsername(name));
    }
}