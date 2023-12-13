package com.example.mywallet.service.impl;

import com.example.mywallet.repositories.TokenRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepo tokenRepo;
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        final String authHeader=request.getHeader("Authorization");
        final String jwt;

        if(authHeader==null || !authHeader.startsWith("Bearer"))
        {
            return;
        }
        jwt=authHeader.substring(7);
        var storedToken=tokenRepo.findByToken(jwt).orElse(null);
        if (storedToken!=null)
        {
            storedToken.setRevoked(true);
            storedToken.setExpired(true);
            tokenRepo.save(storedToken);
        }




    }
}
