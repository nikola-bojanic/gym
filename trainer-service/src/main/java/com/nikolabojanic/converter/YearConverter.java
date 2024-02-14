package com.nikolabojanic.converter;

import com.nikolabojanic.dto.MonthDto;
import com.nikolabojanic.dto.YearDto;
import com.nikolabojanic.entity.MonthEntity;
import com.nikolabojanic.entity.YearEntity;
import java.time.Month;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class YearConverter {
    private final MonthConverter monthConverter;

    /**
     * Converts a YearEntity into a YearDto.
     *
     * @param yearEntity The YearEntity to be converted.
     * @return A YearDto representing the converted information from the YearEntity.
     */
    public YearDto convertToYearDto(YearEntity yearEntity) {
        YearDto yearDto = new YearDto();
        yearDto.setYear(yearEntity.getYear());
        List<MonthDto> months = yearEntity.getMonths().stream()
            .map(monthConverter::convertToMonthDto)
            .toList();
        yearDto.setMonths(months);
        log.info("Successfully converted a year entity into yearDto");
        return yearDto;
    }

    /**
     * Converts a year, month entity into a YearEntity.
     *
     * @param year        The year to be converted.
     * @param monthEntity The MonthEntity to be converted.
     * @return A YearEntity representing the converted information from the year and month entity.
     */
    public YearEntity convertToYearEntity(int year, MonthEntity monthEntity) {
        YearEntity yearEntity = new YearEntity();
        yearEntity.setYear(year);
        yearEntity.setMonths(List.of(monthEntity));
        log.info("Successfully converted a year and month entity into a year entity");
        return yearEntity;
    }

    /**
     * Converts a year, month, and duration into a YearEntity.
     *
     * @param year     The year to be converted.
     * @param month    The Month to be converted.
     * @param duration The duration to be converted.
     * @return A YearEntity representing the converted information from the year, month, and duration.
     */
    public YearEntity convertToYearEntity(int year, Month month, double duration) {
        YearEntity yearEntity = new YearEntity();
        yearEntity.setYear(year);
        yearEntity.setMonths(List.of(monthConverter.convertToMonthEntity(month, duration)));
        log.info("Successfully converted a year, month entity and a duration into a year entity");
        return yearEntity;
    }
}
