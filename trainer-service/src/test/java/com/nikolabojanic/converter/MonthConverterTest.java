package com.nikolabojanic.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.nikolabojanic.dto.MonthDto;
import java.time.Month;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MonthConverterTest {
    @InjectMocks
    private MonthConverter monthConverter;

    @Test
    void convertToMonthTest() {
        //arrange
        Month month = Month.DECEMBER;
        Double duration = Double.parseDouble(RandomStringUtils.randomNumeric(5));
        //act
        MonthDto monthDto = monthConverter.convertToMonth(month, duration);
        //assert
        assertThat(monthDto.getMonth()).isEqualTo(month);
        assertThat(monthDto.getTrainerWorkload()).isEqualTo(duration);
    }
}