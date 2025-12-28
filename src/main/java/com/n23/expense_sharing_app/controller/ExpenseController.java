package com.n23.expense_sharing_app.controller;


import com.n23.expense_sharing_app.dto.ExpenseRequestDTO;
import com.n23.expense_sharing_app.entity.Expense;
import com.n23.expense_sharing_app.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody ExpenseRequestDTO expenseRequestDTO)
    {
        Expense expense = expenseService.addExpense(expenseRequestDTO);

        return ResponseEntity.ok(expense);
    }
}
