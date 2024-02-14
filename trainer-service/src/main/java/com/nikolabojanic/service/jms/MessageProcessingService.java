package com.nikolabojanic.service.jms;

import com.nikolabojanic.dto.TrainerWorkloadRequestDto;
import com.nikolabojanic.enumeration.Action;
import com.nikolabojanic.service.TrainerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional("jmsTransactionManager")
@RequiredArgsConstructor
public class MessageProcessingService {
    private final TrainerService trainerService;

    /**
     * Processes the message based on the {@link Action}.
     *
     * @param requestDto The {@link TrainerWorkloadRequestDto} to be added or deleted.
     */
    public void processMessage(TrainerWorkloadRequestDto requestDto) {
        if (requestDto.getAction() == Action.ADD) {
            trainerService.addTraining(requestDto);
        } else {
            trainerService.deleteTraining(requestDto.getUsername(), requestDto.getDate(), requestDto.getDuration());
        }
        log.info("Successfully updated trainer's training summary.");
    }
}
