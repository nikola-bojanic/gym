package com.nikolabojanic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserPasswordChangeRequestDto {
    private String username;
    private String oldPassword;
    private String newPassword;
}
