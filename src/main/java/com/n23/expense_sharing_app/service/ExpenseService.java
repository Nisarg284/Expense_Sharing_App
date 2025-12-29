package com.n23.expense_sharing_app.service;


import com.n23.expense_sharing_app.dto.ActivityDTO;
import com.n23.expense_sharing_app.dto.ExpenseRequestDTO;
import com.n23.expense_sharing_app.entity.Expense;
import com.n23.expense_sharing_app.entity.ExpenseSplit;
import com.n23.expense_sharing_app.entity.Group;
import com.n23.expense_sharing_app.entity.User;
import com.n23.expense_sharing_app.enums.ExpenseType;
import com.n23.expense_sharing_app.exception.ResourceNotFoundException;
import com.n23.expense_sharing_app.repository.ExpenseRepository;
import com.n23.expense_sharing_app.repository.ExpenseSplitRepository;
import com.n23.expense_sharing_app.repository.GroupRepository;
import com.n23.expense_sharing_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
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
    @CacheEvict(value = "groupBalances",key = "#requestDTO.groupId")
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
        expense.setType(ExpenseType.EXPENSE);

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

    public List<Expense> getGroupExpenses(Long groupId)
    {
        List<Expense> allExpensesOfGroup = expenseRepository.findByGroupIdWithSplits(groupId);
        return allExpensesOfGroup;
    }



    public List<ActivityDTO> getGroupActivity(Long groupId)
    {
        List<Expense> expenses = expenseRepository.findByGroupIdWithSplits(groupId);

        List<ActivityDTO> activityDTOList = expenses.stream()
                .map(expense -> {
                    String message;

                    if (expense.getType() == ExpenseType.SETTLEMENT) {
                        String receiverName = "Someone";
                        if (!expense.getExpenseSplits().isEmpty()) {
                            receiverName = expense.getExpenseSplits().getFirst().getUser().getName();
                        }
                        message = String.format("%s paid %s", expense.getPaidBy().getName(), receiverName);
                    } else {
                        message = String.format("%s added %s", expense.getPaidBy().getName(), expense.getDescription());
                    }

                    return new ActivityDTO(
                            expense.getId(),
                            message,
                            expense.getAmount(),
                            expense.getCreatedAt(),
                            expense.getType()
                    );
                }).toList();

        return activityDTOList;
    }

    // Delete Expense(soft delete)
    @Transactional
    @CacheEvict(value = "groupBalances",key = "#groupId")
    public void deleteExpense(Long groupId,Long expenseId)
    {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not Found"));

        expense.setDeleted(true); // soft delete

        expenseRepository.save(expense);


    }
}
