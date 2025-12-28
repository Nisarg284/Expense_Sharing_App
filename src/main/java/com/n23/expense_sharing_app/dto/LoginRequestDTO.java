package com.n23.expense_sharing_app.dto;


import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}
