package com.nikolabojanic.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.nikolabojanic.dto.MonthDto;
import com.nikolabojanic.dto.YearDto;
import com.nikolabojanic.entity.MonthEntity;
import com.nikolabojanic.entity.YearEntity;
import java.time.Month;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class YearConverterTest {
    @Mock
    private MonthConverter monthConverter;
    @InjectMocks
    private YearConverter yearConverter;

    @Test
    void convertToYearDtoTest() {
        //arrange
        YearEntity yearEntity = new YearEntity();
        MonthEntity monthEntity = new MonthEntity();
        yearEntity.setMonths(List.of(monthEntity));
        when(monthConverter.convertToMonthDto(any(MonthEntity.class))).thenReturn(new MonthDto());
        //act
        YearDto yearDto = yearConverter.convertToYearDto(yearEntity);
        //assert
        assertThat(yearDto).isNotNull();
    }

    @Test
    void convertYearAndMonthToYearEntityTest() {
        //arrange
        Integer year = Integer.parseInt(RandomStringUtils.randomNumeric(5));
        MonthEntity month = new MonthEntity();
        //act
        YearEntity yearEntity = yearConverter.convertToYearEntity(year, month);
        //assert
        assertThat(yearEntity.getYear()).isEqualTo(year);
    }

    @Test
    void convertYearMonthAndDurationToYearEntityTest() {
        //arrange
        Integer year = Integer.parseInt(RandomStringUtils.randomNumeric(5));
        Month month = Month.FEBRUARY;
        Double duration = Double.parseDouble(RandomStringUtils.randomNumeric(5));
        when(monthConverter.convertToMonthEntity(month, duration)).thenReturn(new MonthEntity());
        //act
        YearEntity yearEntity = yearConverter.convertToYearEntity(year, month, duration);
        //assert
        assertThat(yearEntity.getYear()).isEqualTo(year);
    }
}