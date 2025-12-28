package com.n23.expense_sharing_app.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@AllArgsConstructor
//@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {


//    @Autowired
    private CustomUserDetailsService customUserDetailsService;

//    @Autowired
    private JwtUtils jwtUtils;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String email = null;
        String jwt = null;

        // 1. check for Bearer Token
        if(authHeader != null && authHeader.startsWith("Bearer "))
        {
            jwt = authHeader.substring(7);
            email = jwtUtils.extractUsername(jwt);
        }

        // 2. validate & Authenticate
        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(email);

            if(jwtUtils.validateToken(jwt,userDetails))
            {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // This Line sets the user as "Logged In" for this request
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
