package com.nikolabojanic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TraineeRegistrationRequestDTO {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
}
