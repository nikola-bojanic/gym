package com.nikolabojanic.converter;

import com.nikolabojanic.dto.TrainerWorkloadRequestDto;
import com.nikolabojanic.dto.TrainerWorkloadResponseDto;
import com.nikolabojanic.dto.YearDto;
import com.nikolabojanic.entity.TrainerEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TrainerConverter {
    private final YearConverter yearConverter;

    /**
     * Converts a TrainerWorkloadRequestDto into a TrainerEntity.
     *
     * @param requestDto The TrainerWorkloadRequestDto to be converted.
     * @return A TrainerEntity representing the converted information from the TrainerWorkloadRequestDto.
     */
    public TrainerEntity convertToTrainer(TrainerWorkloadRequestDto requestDto) {
        TrainerEntity trainer = TrainerEntity.builder()
            .username(requestDto.getUsername())
            .firstName(requestDto.getFirstName())
            .lastName(requestDto.getLastName())
            .isActive(requestDto.getIsActive())
            .years(List.of(yearConverter.convertToYearEntity(
                requestDto.getDate().getYear(),
                requestDto.getDate().getMonth(),
                requestDto.getDuration())))
            .build();
        log.info("Successfully converted a trainer request dto into a trainer entity");
        return trainer;
    }

    /**
     * Converts a TrainerEntity into a TrainerWorkloadResponseDto.
     *
     * @param trainer The TrainerEntity to be converted.
     * @return A TrainerWorkloadResponseDto representing the converted information from the TrainerEntity.
     */
    public TrainerWorkloadResponseDto convertToResponseDto(TrainerEntity trainer) {
        TrainerWorkloadResponseDto responseDto = new TrainerWorkloadResponseDto();
        responseDto.setUsername(trainer.getUsername());
        responseDto.setFirstName(trainer.getFirstName());
        responseDto.setLastName(trainer.getLastName());
        responseDto.setIsActive(trainer.getIsActive());
        List<YearDto> years = trainer.getYears().stream()
            .map(yearConverter::convertToYearDto)
            .toList();
        responseDto.setYears(years);
        log.info("Successfully converted a trainer entity into a trainer request dto");
        return responseDto;
    }
}
