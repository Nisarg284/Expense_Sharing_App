package com.n23.expense_sharing_app.service;


import com.n23.expense_sharing_app.dto.GroupRequestDTO;
import com.n23.expense_sharing_app.entity.Group;
import com.n23.expense_sharing_app.entity.User;
import com.n23.expense_sharing_app.repository.GroupRepository;
import com.n23.expense_sharing_app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
//@RequiredArgsConstructor
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;


    public Group createGroup(GroupRequestDTO requestDTO)
    {
        // Collect All Users from Database
        List<User> groupMembers = userRepository.findAllById(requestDTO.getUserIds());

        // Validation
        if(groupMembers.size() != requestDTO.getUserIds().size())
        {
            throw new EntityNotFoundException("Some users are not in Database");
        }

        // create new group
        Group group = new Group();
        group.setName(requestDTO.getName());
        group.setDescription(requestDTO.getDescription());
        group.setGroupMembers(new HashSet<>(groupMembers));

        // save
        return groupRepository.save(group);
    }


}
