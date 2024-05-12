package com.nikolabojanic.config.jms;

import com.nikolabojanic.dto.TrainerWorkloadRequestDto;
import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Lazy;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageReceiver {
    private final BlockingQueue<Object> blockingQueue;

    public MessageReceiver(@Lazy BlockingQueue<Object> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    /**
     * Listens for messages from the specified JMS destination (queue) and processes them asynchronously.
     *
     * @param requestDto The payload of the received JMS message, representing a trainer's training update request.
     * @param traceId    The trace ID extracted from the JMS message header for logging and traceability.
     */
    @JmsListener(destination = "${app.active-mq.workload-queue}")
    public void receive(
        @Payload TrainerWorkloadRequestDto requestDto,
        @Header("traceId") String traceId) throws InterruptedException {
        MDC.put("traceId", traceId);
        log.info("Received training summary update for trainer {}", requestDto.getUsername());
        blockingQueue.put(requestDto);
    }
}
