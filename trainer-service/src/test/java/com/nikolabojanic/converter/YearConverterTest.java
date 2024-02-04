package com.nikolabojanic.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.nikolabojanic.dto.YearDto;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
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
    void convertToYearTest() {
        //arrange
        Integer year = Integer.parseInt(RandomStringUtils.randomNumeric(5));
        Map<Month, Double> workload = new HashMap<>();
        //act
        YearDto yearDto = yearConverter.convertToYear(year, workload);
        //assert
        assertThat(yearDto).isNotNull();
    }
}