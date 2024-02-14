package com.nikolabojanic.converter;

import com.nikolabojanic.dto.MonthDto;
import com.nikolabojanic.entity.MonthEntity;
import java.time.Month;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MonthConverter {
    /**
     * Converts a MonthEntity object into a MonthDto.
     *
     * @param monthEntity The MonthEntity to be converted.
     * @return A MonthDto representing the converted information from the MonthEntity.
     */
    public MonthDto convertToMonthDto(MonthEntity monthEntity) {
        MonthDto workloadDto = new MonthDto();
        workloadDto.setMonth(monthEntity.getMonth());
        workloadDto.setTrainingSummary(monthEntity.getTrainingSummary());
        log.info("Successfully converted a month entity into a month dto");
        return workloadDto;
    }

    /**
     * Converts a MonthDto object into a MonthEntity.
     *
     * @param month    The Month enum representing the month.
     * @param duration The training duration for the month.
     * @return A MonthEntity representing the converted information from the MonthDto.
     */
    public MonthEntity convertToMonthEntity(Month month, Double duration) {
        MonthEntity monthEntity = new MonthEntity();
        monthEntity.setMonth(month);
        monthEntity.setTrainingSummary(duration);
        log.info("Successfully converted a month dto into a month entity");
        return monthEntity;
    }
}
