package com.n23.expense_sharing_app.service;


import com.n23.expense_sharing_app.dto.SettlementTransactionDTO;
import com.n23.expense_sharing_app.entity.Expense;
import com.n23.expense_sharing_app.entity.ExpenseSplit;
import com.n23.expense_sharing_app.entity.User;
import com.n23.expense_sharing_app.enums.ExpenseType;
import com.n23.expense_sharing_app.exception.ResourceNotFoundException;
import com.n23.expense_sharing_app.repository.ExpenseRepository;
import com.n23.expense_sharing_app.repository.ExpenseSplitRepository;
import com.n23.expense_sharing_app.repository.GroupRepository;
import com.n23.expense_sharing_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class SettlementService {


//    @Autowired
    private ExpenseRepository expenseRepository;

//    @Autowired
    private ExpenseSplitRepository expenseSplitRepository;
    private final GroupRepository groupRepository; // add this field

    private UserRepository userRepository;


    public Map<Long,Double> getUserBalance(Long groupId)
    {

        if(!groupRepository.existsById(groupId))
        {
            throw new ResourceNotFoundException("Group not found with id:"+groupId);
        }

        List<Expense> expenses = expenseRepository.findByGroupIdWithSplits(groupId);

        Map<Long,Double> balance = new HashMap<>();

        for(Expense expense : expenses)
        {
            User paidBy = expense.getPaidBy();
            // 1. Add (+) Total Amount to Payer

//            if(!balance.containsKey(paidBy.getId()))
//            {
//                balance.put(paidBy.getId(),expense.getAmount() );
//            }else{
//                balance.put(paidBy.getId(), balance.get(paidBy.getId())+expense.getAmount());
//            }

            balance.merge(paidBy.getId(), expense.getAmount(),(old,curr) ->old+curr);

//            List<ExpenseSplit> allSplitExpenses = expenseSplitRepository.findByExpenseId((expense.getId()));
            for(ExpenseSplit split : expense.getExpenseSplits())
            {
                User borrower = split.getUser();
//                if (!balance.containsKey(gareeb.getId()))
//                {
//                    balance.put(gareeb.getId(), -split.getAmount());
//                }else{
//                    balance.put(gareeb.getId(), balance.get(gareeb.getId()) - split.getAmount());
//                }

                balance.merge(borrower.getId(), -split.getAmount(), Double::sum);
            }
        }
        return balance;
    }



    public List<SettlementTransactionDTO> simplifyDebts(Map<Long,Double>balances)
    {
        // 1. Separate into two lists: People who OWE (Negative) and EARN (Positive)
        // We store them as AbstractMap.SimpleEntry<UserId, Amount>
        // max Heap
        PriorityQueue<Map.Entry<Long,Double>> positive = new PriorityQueue<>(
                (a,b) -> Double.compare(b.getValue(),a.getValue())
        );

        // min heap
        PriorityQueue<Map.Entry<Long,Double>> negative = new PriorityQueue<>(
                (a,b) -> Double.compare(a.getValue(),b.getValue())
        );

        for(Map.Entry<Long,Double> entry : balances.entrySet())
        {
            if(entry.getValue() > 0.01)
            {
                positive.add(entry);
            }else if(entry.getValue() < -0.01){
                negative.add(entry);
            }
        }


        List<SettlementTransactionDTO> transections = new ArrayList<>();

        // The main Logic
        while (!positive.isEmpty() && !negative.isEmpty())
        {
            Map.Entry<Long,Double> receiver = positive.remove();
            Map.Entry<Long,Double> payer = negative.remove();

            double settlementAmount = Math.min(Math.abs(payer.getValue()), receiver.getValue());

            transections.add(
                    new SettlementTransactionDTO(
                            payer.getKey(),
                            receiver.getKey(),
                            settlementAmount
                    )
            );


            // update remaining balance
            double payerRemaining = payer.getValue() + settlementAmount; // -50 + 50 = 0
            double receiverRemaining = receiver.getValue() - settlementAmount; // 100 - 50 = 50

            if(Math.abs(payerRemaining) > 0.01)
            {
                payer.setValue(payerRemaining);
                negative.add(payer);
            }

            if(Math.abs(receiverRemaining) > 0.01)
            {
                receiver.setValue(receiverRemaining);
                positive.add(receiver);
            }
        }
            return transections;
    }



    // Settle up
    @Transactional
    public void settleUp(Long groupId,Long payerId,Long receiverId,Double amount)
    {
        // 1 create the "Expense" Representing the payment
        Expense settlement = new Expense();
        settlement.setDescription("Settlement Payment");
        settlement.setAmount(amount);
        settlement.setGroup(groupRepository.getReferenceById(groupId));
        settlement.setPaidBy(userRepository.getReferenceById(payerId)); // // The person GIVING money
        settlement.setType(ExpenseType.SETTLEMENT);

        Expense savedSettlement = expenseRepository.save(settlement);


        // 2. Create the split logic
        // The Receiver is treated as the one "Consuming" the money for accounting purposes

        ExpenseSplit split = new ExpenseSplit();
        split.setExpense(savedSettlement);
        split.setUser(userRepository.getReferenceById(receiverId));
        split.setAmount(amount);

        expenseSplitRepository.save(split);

    }


}
