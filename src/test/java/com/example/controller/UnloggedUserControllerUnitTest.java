package com.example.controller;

import com.example.entity.UserDetails;
import com.example.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnloggedUserControllerUnitTest {

    @InjectMocks
    private UnloggedUserController controller;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @Test
    void testSaveUserSuccess() {
        UserDetails user = new UserDetails();
        user.setEmail("test@gmail.com");

        when(userService.existsUser("test@gmail.com")).thenReturn(false);
        when(userService.saveUser(user)).thenReturn(user);

        String result = controller.saveUser(user, session);

        verify(session).setAttribute("successMessage", "Rejestracja udana");
        assertEquals("redirect:/register", result);
    }

    @Test
    void testSaveUserIfEmailExists() {
        UserDetails user = new UserDetails();
        user.setEmail("test@gmail.com");

        when(userService.existsUser("test@gmail.com")).thenReturn(true);

        String result = controller.saveUser(user, session);

        verify(session).setAttribute("errorMessage", "Użytkownik o podanym e-mailu jest już zarejestrowany");
        assertEquals("redirect:/register", result);
    }

    @Test
    void testSaveUserError() {
        UserDetails user = new UserDetails();
        user.setEmail("test@gmail.com");

        when(userService.existsUser("test@gmail.com")).thenReturn(false);
        when(userService.saveUser(user)).thenReturn(null);

        String result = controller.saveUser(user, session);

        verify(session).setAttribute("errorMsg", "Błąd serwera");
        assertEquals("redirect:/register", result);
    }
}
