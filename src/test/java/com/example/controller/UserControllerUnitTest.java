package com.example.controller;

import com.example.entity.UserDetails;
import com.example.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserControllerUnitTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private HttpSession session;

    @Mock
    private Principal principal;


    @Test
    void testChangeUserPasswordWhenPasswordsDoNotMatch() {
        // given
        String password = "currentPassword";
        String newPassword = "newPass";
        String repeatPassword = "wrongRepeat";

        UserDetails userDetails = new UserDetails();
        userDetails.setPassword("encodedPassword");

        when(principal.getName()).thenReturn("user@example.com");
        when(userService.getUserByEmail("user@example.com")).thenReturn(userDetails);

        // when
        String view = userController.changeUserPassword(password, newPassword, repeatPassword, principal, session);

        // then
        verify(session).setAttribute("errorMessage", "Błędnie powtórzyłeś hasło");
        assertEquals("redirect:/user/account", view);
    }

    @Test
    void testEditAccountSucceeds() {
        // given
        UserDetails user = new UserDetails();
        when(userService.editUserAccount(user)).thenReturn(user);

        // when
        String result = userController.editAccount(session, user);

        // then
        verify(session).setAttribute("successMessage", "Konto użytkownika zostało edytowane");
        assertEquals("redirect:/user/account", result);
    }
}

