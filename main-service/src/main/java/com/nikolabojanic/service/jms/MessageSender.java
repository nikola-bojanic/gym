package com.nikolabojanic.service.jms;

import com.nikolabojanic.dto.TrainerWorkloadRequestDto;
import com.nikolabojanic.logging.MdcFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessageSender {
    private static final String WORKLOAD_QUEUE = "workload.queue";
    private final JmsTemplate jmsTemplate;
    private final MdcFilter mdcFilter;

    /**
     * Sends a message to the ActiveMq message queue..
     *
     * @param requestDto The {@link TrainerWorkloadRequestDto} to be sent as a message.
     */
    public void send(TrainerWorkloadRequestDto requestDto) {
        jmsTemplate.convertAndSend(WORKLOAD_QUEUE, requestDto, message -> {
            message.setStringProperty("traceId", mdcFilter.getTraceId());
            return message;
        });
    }
}
