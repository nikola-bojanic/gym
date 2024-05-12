package com.nikolabojanic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActiveTrainerResponseDTO {
    private String username;
    private String firstName;
    private String lastName;
    private Long specializationId;
}
