package com.nikolabojanic.dto;

import com.nikolabojanic.enumeration.Action;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerWorkloadRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private Boolean isActive;
    @NotNull
    private LocalDate date;
    @NotNull
    private Double duration;
    @NotNull
    private Action action;
}
