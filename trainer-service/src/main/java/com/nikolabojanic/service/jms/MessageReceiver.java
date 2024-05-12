package com.nikolabojanic.service.jms;

import com.nikolabojanic.dto.TrainerWorkloadRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageReceiver {
    private final MessageProcessingService messageProcessingService;

    /**
     * Listens for messages from the specified JMS destination (queue) and processes them asynchronously.
     *
     * @param requestDto The payload of the received JMS message, representing a workload request for a trainer.
     * @param traceId    The trace ID extracted from the JMS message header for logging and traceability.
     */
    @JmsListener(destination = "${app.active-mq.workload-queue}", concurrency = "${spring.jms.listener.concurrency}")
    public void receive(
        @Payload TrainerWorkloadRequestDto requestDto,
        @Header("traceId") String traceId) {
        MDC.put("traceId", traceId);
        log.info("Received workload for trainer {}", requestDto.getUsername());
        messageProcessingService.processMessage(requestDto);
    }
}