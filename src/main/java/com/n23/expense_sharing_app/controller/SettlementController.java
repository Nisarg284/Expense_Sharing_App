package com.n23.expense_sharing_app.controller;


import com.n23.expense_sharing_app.dto.SettlementTransactionDTO;
import com.n23.expense_sharing_app.repository.GroupRepository;
import com.n23.expense_sharing_app.service.GroupService;
import com.n23.expense_sharing_app.service.SettlementService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@AllArgsConstructor
@RestController
@RequestMapping("/api/groups/{groupId}/settlements")
public class SettlementController {


//    @Autowired
    private SettlementService settlementService;
//    private GroupService groupService;

//    @GetMapping("/{groupId}")
    public ResponseEntity<Map<String,Object>> getSettlement(@PathVariable Long groupId)
    {
        // Get the raw balance
        Map<Long,Double> balances = settlementService.getUserBalance(groupId);

        // 2. Get the simplified transactions (Phase 2 - Your Algorithm)
        List<SettlementTransactionDTO> transections = settlementService.simplifyDebts(balances);

        return ResponseEntity.ok(Map.of(
                "raw_balance",balances,
                "simplified_transections",transections)
        );
    }


    @PostMapping("/pay")
    public ResponseEntity<String> settleUp(@PathVariable Long groupId,@RequestBody SettlementTransactionDTO request)
    {
        settlementService.settleUp(groupId, request.getPayerId(), request.getReceiverId(), request.getAmount());

        return ResponseEntity.ok("Payment recorded! Balances Updated");
    }





}
