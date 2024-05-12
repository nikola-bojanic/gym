package com.nikolabojanic.service.jms;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.nikolabojanic.dto.TrainerWorkloadRequestDto;
import com.nikolabojanic.enumeration.Action;
import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageReceiverTest {
    @Mock
    private MessageProcessingService messageProcessingService;
    @InjectMocks
    private MessageReceiver messageReceiver;

    @Test
    void receive() {
        //arrange
        doNothing().when(messageProcessingService).processMessage(any(TrainerWorkloadRequestDto.class));
        //act
        messageReceiver.receive(
            new TrainerWorkloadRequestDto(
                RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(5),
                true,
                LocalDate.now(),
                Double.parseDouble(RandomStringUtils.randomNumeric(5)),
                Action.ADD
            ),
            RandomStringUtils.randomAlphabetic(5)
        );
        //assert
        verify(messageProcessingService, times(1))
            .processMessage(any(TrainerWorkloadRequestDto.class));
    }
}