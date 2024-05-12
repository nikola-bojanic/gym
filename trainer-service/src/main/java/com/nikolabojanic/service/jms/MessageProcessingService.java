package com.nikolabojanic.service.jms;

import com.nikolabojanic.dto.TrainerWorkloadRequestDto;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.entity.TrainingEntity;
import com.nikolabojanic.enumeration.Action;
import com.nikolabojanic.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional("jmsTransactionManager")
@RequiredArgsConstructor
public class MessageProcessingService {
    private final TrainingService trainingService;

    /**
     * Processes the message based on the {@link Action}.
     *
     * @param requestDto The {@link TrainerWorkloadRequestDto} to be added or deleted.
     */
    public void processMessage(TrainerWorkloadRequestDto requestDto) {
        if (requestDto.getAction() == Action.ADD) {
            TrainerEntity trainer = TrainerEntity.builder()
                .username(requestDto.getUsername())
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .isActive(requestDto.getIsActive())
                .build();
            trainingService.addTraining(TrainingEntity.builder()
                .trainer(trainer)
                .date(requestDto.getDate())
                .duration(requestDto.getDuration()).build());
        } else {
            trainingService.deleteTrainings(requestDto.getUsername(), requestDto.getDate());
        }
        log.info("Successfully updated trainer workload.");
    }
}