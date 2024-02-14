package com.nikolabojanic.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.nikolabojanic.dto.MonthDto;
import com.nikolabojanic.entity.MonthEntity;
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
    void convertToMonthDtoTest() {
        //arrange
        MonthEntity month = new MonthEntity();
        month.setMonth(Month.FEBRUARY);
        month.setTrainingSummary(Double.parseDouble(RandomStringUtils.randomNumeric(5)));
        //act
        MonthDto dto = monthConverter.convertToMonthDto(month);
        //assert
        assertThat(dto.getMonth()).isEqualTo(month.getMonth());
        assertThat(dto.getTrainingSummary()).isEqualTo(month.getTrainingSummary());
    }

    @Test
    void convertToMonthEntityTest() {
        //arrange
        Double duration = Double.parseDouble(RandomStringUtils.randomNumeric(5));
        Month month = Month.FEBRUARY;
        //act
        MonthEntity entity = monthConverter.convertToMonthEntity(month, duration);
        //assert
        assertThat(entity.getMonth()).isEqualTo(month);
        assertThat(entity.getTrainingSummary()).isEqualTo(duration);

    }

}