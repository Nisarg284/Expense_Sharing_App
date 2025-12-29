package com.n23.expense_sharing_app.controller;


import com.n23.expense_sharing_app.dto.ActivityDTO;
import com.n23.expense_sharing_app.dto.GroupRequestDTO;
import com.n23.expense_sharing_app.entity.Group;
import com.n23.expense_sharing_app.service.ExpenseService;
import com.n23.expense_sharing_app.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@AllArgsConstructor
public class GroupController {

    private final ExpenseService expenseService;
    //    @Autowired
    private GroupService groupService;

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody GroupRequestDTO groupRequestDTO)
    {
        Group group = groupService.createGroup(groupRequestDTO);

        return ResponseEntity.ok(group);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Group>> getUserGroups(@PathVariable Long userId)
    {
        List<Group> groups = groupService.getUserGroups(userId);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{groupId}/activity")
    public ResponseEntity<List<ActivityDTO>> getGroupActivity(@PathVariable Long groupId)
    {
        return ResponseEntity.ok(expenseService.getGroupActivity(groupId));
    }

}
