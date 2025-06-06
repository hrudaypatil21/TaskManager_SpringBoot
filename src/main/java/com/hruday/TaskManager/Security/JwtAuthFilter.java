package com.hruday.TaskManager.Security;

import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter {

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final UserRepository userRepository;

    protected void doFilterInternal(
            HttpServletRequest req,  HttpServletResponse res, FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = req.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(req, res);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String empId = jwtService.extractEmpId(jwt);
            User user = userRepository.findByEmpId(empId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: "+empId));
            if(jwtService.validateToken(jwt, user)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null,
                        );

            }

        }
    }





}
