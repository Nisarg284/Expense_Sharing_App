package com.n23.expense_sharing_app.security;


import com.n23.expense_sharing_app.entity.User;
import com.n23.expense_sharing_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1 Fetch Our Custom User Entity
        User user = userRepository.findUserByEmail(email);

        // 2. Return a Spring Security User object
        // (We use email as username, and empty list for authorities for now)

        return new CustomUserDetails(user);
    }
}
