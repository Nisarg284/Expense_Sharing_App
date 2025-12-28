package com.n23.expense_sharing_app.service;


import com.n23.expense_sharing_app.entity.User;
import com.n23.expense_sharing_app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User register(User user)
    {
        // validation check
        if(userRepository.existsByEmail(user.getEmail()))
        {
            throw new RuntimeException("Email already Exists");
        }

        // 2. Hash Password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        return savedUser;
    }

}
