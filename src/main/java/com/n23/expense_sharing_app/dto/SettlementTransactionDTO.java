package com.n23.expense_sharing_app.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SettlementTransactionDTO {

    private Long payerId;
    private Long receiverId;
    private Double amount;
}
