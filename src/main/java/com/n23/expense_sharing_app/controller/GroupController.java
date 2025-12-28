package com.n23.expense_sharing_app.controller;


import com.n23.expense_sharing_app.dto.GroupRequestDTO;
import com.n23.expense_sharing_app.entity.Group;
import com.n23.expense_sharing_app.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody GroupRequestDTO groupRequestDTO)
    {
        Group group = groupService.createGroup(groupRequestDTO);

        return ResponseEntity.ok(group);
    }

}
