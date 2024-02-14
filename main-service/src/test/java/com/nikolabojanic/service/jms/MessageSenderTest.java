package com.nikolabojanic.service.jms;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.nikolabojanic.dto.TrainerWorkloadRequestDto;
import com.nikolabojanic.enumeration.Action;
import com.nikolabojanic.logging.MdcFilter;
import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

@ExtendWith(MockitoExtension.class)
class MessageSenderTest {
    @Mock
    private JmsTemplate jmsTemplate;
    @Mock
    private MdcFilter mdcFilter;
    @InjectMocks
    private MessageSender messageSender;

    @Test
    void sendTest() {
        //arrange
        messageSender.setQueue("test");
        doNothing().when(jmsTemplate)
            .convertAndSend(any(String.class), any(TrainerWorkloadRequestDto.class), any(MessagePostProcessor.class));
        //act
        messageSender.send(new TrainerWorkloadRequestDto(
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            true,
            LocalDate.now(),
            Double.parseDouble(RandomStringUtils.randomNumeric(5)),
            Action.ADD
        ));
        //assert
        verify(jmsTemplate, times(1))
            .convertAndSend(any(String.class), any(TrainerWorkloadRequestDto.class), any(MessagePostProcessor.class));
    }
}