package com.nikolabojanic.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.nikolabojanic.dto.TrainerWorkloadRequestDto;
import com.nikolabojanic.dto.TrainerWorkloadResponseDto;
import com.nikolabojanic.dto.YearDto;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.entity.YearEntity;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainerConverterTest {
    @Mock
    private YearConverter yearConverter;
    @InjectMocks
    private TrainerConverter trainerConverter;

    @Test
    void convertToTrainerTest() {
        //arrange
        TrainerWorkloadRequestDto requestDto = new TrainerWorkloadRequestDto();
        requestDto.setDate(LocalDate.now());
        requestDto.setDuration(Double.parseDouble(RandomStringUtils.randomNumeric(5)));
        when(yearConverter.convertToYearEntity(
            any(Integer.class), any(Month.class), any(Double.class))).thenReturn(new YearEntity());
        //act
        TrainerEntity trainer = trainerConverter.convertToTrainer(requestDto);
        //assert
        assertThat(trainer).isNotNull();
    }

    @Test
    void convertToResponseDtoTest() {
        //arrange
        TrainerEntity trainer = new TrainerEntity();
        YearEntity year = new YearEntity();
        trainer.setYears(List.of(year));
        when(yearConverter.convertToYearDto(any(YearEntity.class))).thenReturn(new YearDto());
        //act
        TrainerWorkloadResponseDto responseDto = trainerConverter.convertToResponseDto(trainer);
        //assert
        assertThat(responseDto).isNotNull();
    }
}
