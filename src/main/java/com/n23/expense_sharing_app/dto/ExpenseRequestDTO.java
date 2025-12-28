package com.n23.expense_sharing_app.dto;


import lombok.Data;

import java.util.Map;

@Data
public class ExpenseRequestDTO {

    private String description;
    private Double totalAmount;
    private Long payerId; // Who paid?
    private Long groupId; // which group?

    // Map<UserId, AmountOwed>
    // Example: { 1: 50.0, 2: 50.0 }
    private Map<Long,Double> splits;
}
