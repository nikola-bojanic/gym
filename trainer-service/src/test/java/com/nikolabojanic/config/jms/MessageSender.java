package com.nikolabojanic.config.jms;

import com.nikolabojanic.dto.TrainerWorkloadRequestDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessageSender {
    private final JmsTemplate jmsTemplate;
    @Value("${app.active-mq.workload-queue}")
    private String queue;

    /**
     * Sends a message to the ActiveMq message queue.
     *
     * @param requestDto The {@link TrainerWorkloadRequestDto} to be sent as a message.
     */
    public void send(TrainerWorkloadRequestDto requestDto) {
        jmsTemplate.convertAndSend(queue, requestDto, message -> {
            message.setStringProperty("traceId", RandomStringUtils.randomAlphabetic(10));
            return message;
        });
    }
}
