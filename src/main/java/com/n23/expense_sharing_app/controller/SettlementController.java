package com.n23.expense_sharing_app.controller;


import com.n23.expense_sharing_app.service.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/settlements")
public class SettlementController {


    @Autowired
    private SettlementService settlementService;

    @GetMapping("/{groupId}")
    public ResponseEntity<Map<String,Object>> getSettlement(@PathVariable Long groupId)
    {
        // Get the raw balance
        Map<Long,Double> balances = settlementService.getUserBalance(groupId);

        // 2. Get the simplified transactions (Phase 2 - Your Algorithm)
        List<String> transections = settlementService.simplifyDebts(balances);

        return ResponseEntity.ok(Map.of(
                "raw_balance",balances,
                "simplified_transections",transections)
        );
    }
}
