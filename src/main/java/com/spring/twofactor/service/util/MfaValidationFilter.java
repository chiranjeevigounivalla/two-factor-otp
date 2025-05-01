package com.spring.twofactor.service.util;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MfaValidationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            Boolean mfaPassed = (Boolean) request.getSession().getAttribute("MFA_AUTHENTICATED");

            String path = request.getRequestURI();

            if (!Boolean.TRUE.equals(mfaPassed) &&
                !path.startsWith("/mfa") &&
                !path.startsWith("/verify-otp") &&
                !path.startsWith("/logout")) {

                response.sendRedirect("/mfa");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
