package com.nikolabojanic.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrainingTypeRequestDto {
    private String name;

    public TrainingTypeRequestDto(String name) {
        this.name = name;
    }
}
