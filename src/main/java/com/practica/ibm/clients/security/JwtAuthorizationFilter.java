package com.practica.ibm.clients.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.practica.ibm.clients.models.Account;
import com.practica.ibm.clients.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    JwtTokenService jwtTokenService;

    @Autowired
    AccountRepository accountRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Account validatedAccount;
        try {
            validatedAccount = getValidatedAccountFromRequest(request, response);
        } catch (IOException e) {
            System.out.println("Failed to validate token");
            return;
        }
        if (validatedAccount != null) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(validatedAccount, null, new ArrayList<>());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(request, response);
    }


    private Account getValidatedAccountFromRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            System.out.println("Authentication failed due to missing bearer header");
            return null;
        }

        String token = header.substring(7);
        JsonNode validatedToken = jwtTokenService.validateToken(token);
        if (validatedToken.hasNonNull("error")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(validatedToken.get("error").toString());
            return null;
        }

        String email = validatedToken.get("email").textValue();
        Optional<Account> accountOptional = accountRepository.getAccountByEmail(email);
        if (!accountOptional.isPresent()) {
            System.out.println("email '" + email + "' not found");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write("Email not found");
            return null;
        }
        return accountOptional.get();
    }

}
