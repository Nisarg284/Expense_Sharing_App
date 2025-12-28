package com.n23.expense_sharing_app.repository;

import com.n23.expense_sharing_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);

    boolean existsByEmail(String email);
}