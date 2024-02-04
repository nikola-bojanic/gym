package com.nikolabojanic.converter;

import com.nikolabojanic.dto.TrainingTypeResponseDto;
import com.nikolabojanic.entity.TrainingTypeEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Slf4j
@Component
public class TrainingTypeConverter {
    /**
     * Convert a training type ID to a TrainingTypeEntity model.
     *
     * @param id The training type ID to be converted.
     * @return TrainingTypeEntity representing the converted model.
     */
    public TrainingTypeEntity convertIdToModel(Long id) {
        TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity();
        trainingTypeEntity.setId(id);
        log.info("Successfully converted training type id to training type model.");
        return trainingTypeEntity;
    }

    /**
     * Convert a TrainingTypeEntity to a TrainingTypeResponseDto.
     *
     * @param trainingType The TrainingTypeEntity to be converted.
     * @return TrainingTypeResponseDto representing the converted response.
     */
    public TrainingTypeResponseDto convertModelToResponse(TrainingTypeEntity trainingType) {
        TrainingTypeResponseDto responseDto = new TrainingTypeResponseDto();
        responseDto.setId(trainingType.getId());
        responseDto.setName(trainingType.getName());
        log.info("Successfully converted training type model to training type response.");
        return responseDto;
    }
}
