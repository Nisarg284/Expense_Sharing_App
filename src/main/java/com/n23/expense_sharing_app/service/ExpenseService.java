package com.n23.expense_sharing_app.service;


import com.n23.expense_sharing_app.dto.ExpenseRequestDTO;
import com.n23.expense_sharing_app.entity.Expense;
import com.n23.expense_sharing_app.entity.ExpenseSplit;
import com.n23.expense_sharing_app.entity.Group;
import com.n23.expense_sharing_app.entity.User;
import com.n23.expense_sharing_app.exception.ResourceNotFoundException;
import com.n23.expense_sharing_app.repository.ExpenseRepository;
import com.n23.expense_sharing_app.repository.ExpenseSplitRepository;
import com.n23.expense_sharing_app.repository.GroupRepository;
import com.n23.expense_sharing_app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ExpenseService {


    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseSplitRepository expenseSplitRepository;


    @Transactional
    public Expense addExpense(ExpenseRequestDTO requestDTO)
    {
        // Validate Group exists
        Group group = groupRepository.findById(requestDTO.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with this id"));

        // validate Payer exists
        User payer = userRepository.findById(requestDTO.getPayerId())
                .orElseThrow(() -> new ResourceNotFoundException("Payer Not Found"));

        // Validate the Amount by compare the total amount with splits amount
        double splitSum = requestDTO.getSplits().values().stream()
                .mapToDouble(val -> val).sum();

        if(requestDTO.getTotalAmount() != splitSum)
        {
            throw new RuntimeException("Math doesn't add up!");
        }

        // create and Save Expense Header
        Expense expense = new Expense();
        expense.setAmount(requestDTO.getTotalAmount());
        expense.setDescription(requestDTO.getDescription());
        expense.setGroup(group);
        expense.setPaidBy(payer);

        Expense savedExpense = expenseRepository.save(expense);

        // create and save splits
        for(Map.Entry<Long,Double> entry : requestDTO.getSplits().entrySet())
        {
            Long userId = entry.getKey();
            Double amount = entry.getValue();

            // find Udharyo
            User udharyo = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            ExpenseSplit split = new ExpenseSplit();
            split.setExpense(savedExpense);
            split.setAmount(amount);
            split.setUser(udharyo);

            expenseSplitRepository.save(split);
        }

        return savedExpense;

    }
}
