package com.nikolabojanic.converter;

import com.nikolabojanic.dto.MonthDto;
import java.time.Month;
import org.springframework.stereotype.Component;

@Component
public class MonthConverter {
    /**
     * Converts a month and a duration of a training to a MonthDto object.
     *
     * @param month    {@link Month} The month of the training.
     * @param duration {@link Double} Duration of the training.
     * @return {@link MonthDto} A MonthDto object containing month of the training and the duration.
     */
    public MonthDto convertToMonth(Month month, Double duration) {
        MonthDto workloadDto = new MonthDto();
        workloadDto.setMonth(month);
        workloadDto.setTrainerWorkload(duration);
        return workloadDto;
    }
}
