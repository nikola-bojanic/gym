package com.nikolabojanic.service.jms;

import com.nikolabojanic.dto.TrainerWorkloadRequestDto;
import com.nikolabojanic.logging.MdcFilter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Setter
public class MessageSender {
    private final JmsTemplate jmsTemplate;
    private final MdcFilter mdcFilter;
    @Value("${app.active-mq.workload-queue}")
    private String queue;

    /**
     * Sends a message to the ActiveMq message queue.
     *
     * @param requestDto The {@link TrainerWorkloadRequestDto} to be sent as a message.
     */
    public void send(TrainerWorkloadRequestDto requestDto) {
        jmsTemplate.convertAndSend(queue, requestDto, message -> {
            message.setStringProperty("traceId", mdcFilter.getTraceId());
            return message;
        });
    }
}
