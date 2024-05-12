package com.nikolabojanic.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerWorkloadResponseDto {
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private List<YearDto> years;

}
