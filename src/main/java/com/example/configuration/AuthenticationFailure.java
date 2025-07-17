package com.example.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class
AuthenticationFailure implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        if (exception instanceof DisabledException) {
            request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", "Konto zablokowane");
        } else {
            request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", "Niepoprawne dane logowania");
        }
        response.sendRedirect("/login?error");

    }
}
