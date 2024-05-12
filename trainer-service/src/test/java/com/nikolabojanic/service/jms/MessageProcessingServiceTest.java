package com.nikolabojanic.service.jms;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nikolabojanic.dto.TrainerWorkloadRequestDto;
import com.nikolabojanic.entity.TrainingEntity;
import com.nikolabojanic.enumeration.Action;
import com.nikolabojanic.service.TrainingService;
import java.time.LocalDate;
import java.util.ArrayList;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageProcessingServiceTest {
    @Mock
    private TrainingService trainingService;
    @InjectMocks
    private MessageProcessingService messageProcessingService;

    @Test
    void processAddMessageTest() {
        //arrange
        when(trainingService.addTraining(any(TrainingEntity.class))).thenReturn(new TrainingEntity());
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
        verify(trainingService, times(1)).addTraining(any(TrainingEntity.class));
    }

    @Test
    void processDeleteMessageTest() {
        //arrange
        when(trainingService.deleteTrainings(anyString(), any(LocalDate.class))).thenReturn(new ArrayList<>());
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
        verify(trainingService, times(1)).deleteTrainings(anyString(), any(LocalDate.class));
    }
}