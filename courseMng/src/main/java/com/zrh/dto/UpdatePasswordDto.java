package com.zrh.dto;

import lombok.Data;

@Data
public class UpdatePasswordDto {
    private String level;

    private String username;

    private String passwordAgain;

    private String password;

    private String newPassword;
}
