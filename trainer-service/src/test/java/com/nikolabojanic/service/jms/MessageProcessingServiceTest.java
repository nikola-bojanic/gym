package com.nikolabojanic.service.jms;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nikolabojanic.dto.TrainerWorkloadRequestDto;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.enumeration.Action;
import com.nikolabojanic.service.TrainerService;
import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageProcessingServiceTest {
    @Mock
    private TrainerService trainerService;
    @InjectMocks
    private MessageProcessingService messageProcessingService;

    @Test
    void processAddMessageTest() {
        //arrange
        when(trainerService.addTraining(any(TrainerWorkloadRequestDto.class))).thenReturn(new TrainerEntity());
        //act
        messageProcessingService.processMessage(
            new TrainerWorkloadRequestDto(
                RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(5),
                true,
                LocalDate.now(),
                Double.parseDouble(RandomStringUtils.randomNumeric(5)),
                Action.ADD
            )
        );
        //assert
        verify(trainerService, times(1)).addTraining(any(TrainerWorkloadRequestDto.class));
    }

    @Test
    void processDeleteMessageTest() {
        //arrange
        when(trainerService.deleteTraining(anyString(), any(LocalDate.class), anyDouble()))
            .thenReturn(new TrainerEntity());
        //act
        messageProcessingService.processMessage(
            new TrainerWorkloadRequestDto(
                RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(5),
                true,
                LocalDate.now(),
                Double.parseDouble(RandomStringUtils.randomNumeric(5)),
                Action.DELETE
            )
        );
        //assert
        verify(trainerService, times(1))
            .deleteTraining(anyString(), any(LocalDate.class), anyDouble());
    }
}