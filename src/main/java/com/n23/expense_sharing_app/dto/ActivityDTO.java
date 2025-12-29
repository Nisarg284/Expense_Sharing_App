package com.n23.expense_sharing_app.dto;

import com.n23.expense_sharing_app.enums.ExpenseType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ActivityDTO {

    private Long id;
    private String message; // e.g "Nisarg paid Vrukshika"
    private Double amount;
    private Date timestamp;
    private ExpenseType type; // " Expense or Settlement"

}
