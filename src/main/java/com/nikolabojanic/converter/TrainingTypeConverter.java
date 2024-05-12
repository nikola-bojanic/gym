package com.nikolabojanic.converter;

import com.nikolabojanic.dto.TrainingTypeResponseDTO;
import com.nikolabojanic.model.TrainingTypeEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Slf4j
@Component
public class TrainingTypeConverter {

    public TrainingTypeEntity convertIdToModel(Long id) {
        TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity();
        trainingTypeEntity.setId(id);
        log.info("Successfully converted training type id to training type model.");
        return trainingTypeEntity;
    }

    public TrainingTypeResponseDTO convertModelToResponse(TrainingTypeEntity trainingType) {
        TrainingTypeResponseDTO responseDTO = new TrainingTypeResponseDTO();
        responseDTO.setId(trainingType.getId());
        responseDTO.setName(trainingType.getName());
        log.info("Successfully converted training type model to training type response.");
        return responseDTO;
    }
}
