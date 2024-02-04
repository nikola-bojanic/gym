package com.nikolabojanic.converter;

import com.nikolabojanic.dto.MonthDto;
import com.nikolabojanic.dto.YearDto;
import java.time.Month;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class YearConverter {
    private final MonthConverter monthConverter;

    /**
     * Converts a year and a workload for that year to a YearDto object.
     *
     * @param year     {@link Integer} Year of the trainer's workload.
     * @param workload {@link Map} Map of the duration of the monthly trainings.
     * @return {@link YearDto} YearDto object.
     */
    public YearDto convertToYear(Integer year, Map<Month, Double> workload) {
        YearDto yearDto = new YearDto();
        yearDto.setYear(year);
        List<MonthDto> workloadDto = workload.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(pair -> monthConverter.convertToMonth(pair.getKey(), pair.getValue()))
            .toList();
        yearDto.setMonths(workloadDto);
        return yearDto;
    }
}
