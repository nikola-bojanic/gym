package com.nikolabojanic.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TraineeRegistrationRequestDto {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
}
