package com.n23.expense_sharing_app;


import com.n23.expense_sharing_app.entity.Group;
import com.n23.expense_sharing_app.entity.User;
import com.n23.expense_sharing_app.repository.GroupRepository;
import com.n23.expense_sharing_app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
@AllArgsConstructor
public class DataSeeder implements CommandLineRunner {

//    @Autowired
    private UserRepository userRepository;

//    @Autowired
    private GroupRepository groupRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Create Users with HASHED passwords
        User user1 = new User();
        user1.setName("Alice");
        user1.setEmail("alice@test.com");
        user1.setPassword(passwordEncoder.encode("password123")); // <--- Hashing happening here

        User user2 = new User();
        user2.setName("Bob");
        user2.setEmail("bob@test.com");
        user2.setPassword(passwordEncoder.encode("password123"));

        User user3 = new User();
        user3.setName("Charlie");
        user3.setEmail("charlie@test.com");
        user3.setPassword(passwordEncoder.encode("password123"));

        userRepository.saveAll(Arrays.asList(user1, user2, user3));

        // 2. Create a Group
        Group tripGroup = new Group();
        tripGroup.setName("Goa Trip 2025");
        tripGroup.setDescription("Fun in the sun");

        // 3. Add Users to Group
        tripGroup.setGroupMembers(new HashSet<>(Arrays.asList(user1, user2, user3)));

        groupRepository.save(tripGroup);

        System.out.println("---------------------------------------------");
        System.out.println("✅ SUCCESS: Data seeded with HASHED passwords!");
        System.out.println("---------------------------------------------");        }
    }
