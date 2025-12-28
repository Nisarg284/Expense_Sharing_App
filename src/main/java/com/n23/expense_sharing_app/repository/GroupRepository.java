package com.n23.expense_sharing_app.repository;

import com.n23.expense_sharing_app.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findGroupsByGroupMembersId(Long groupMembersId);
}