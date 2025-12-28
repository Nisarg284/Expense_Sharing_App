package com.n23.expense_sharing_app.dto.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private int status;       // e.g., 404, 400
    private String message;   // e.g., "User not found"
    private LocalDateTime timestamp;


}
