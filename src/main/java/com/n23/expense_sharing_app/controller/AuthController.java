package com.n23.expense_sharing_app.controller;


import com.n23.expense_sharing_app.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>>login(@RequestBody LoginRequestDTO request){
        String email = request.getEmail();
        String password = request.getPassword();

        // 1. Authenticate (This Will Throw exception if bad Credentials)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email,password)
        );

        // 2. Generate Token
        String token = jwtUtils.generateToken(email);

        return ResponseEntity.ok(Map.of("token",token));
    }
}
