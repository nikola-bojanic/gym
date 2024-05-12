package com.nikolabojanic.dto;

import com.nikolabojanic.enumeration.Action;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TrainerWorkloadRequestDto {
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private LocalDate date;
    private Double duration;
    private Action action;
}
