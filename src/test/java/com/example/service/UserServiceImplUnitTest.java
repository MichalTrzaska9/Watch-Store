package com.example.service;

import com.example.entity.UserDetails;
import com.example.repository.UserRepository;
import com.example.service.serviceImplementation.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplUnitTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testSaveUse() {
        // given
        UserDetails user = new UserDetails();
        user.setPassword("plainPassword");

        UserDetails savedUser = new UserDetails();
        savedUser.setId(1);
        savedUser.setPassword("encodedPassword");
        savedUser.setIsEnable(true);
        savedUser.setRole("ROLE_USER");

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserDetails.class))).thenReturn(savedUser);

        // when
        UserDetails result = userService.saveUser(user);

        // then
        assertEquals("encodedPassword", result.getPassword());
        assertTrue(result.getIsEnable());
        assertEquals("ROLE_USER", result.getRole());

        verify(userRepository).save(user);
        verify(passwordEncoder).encode("plainPassword");
    }

    @Test
    void testUpdateAccountStatusWhenUserExists() {
        // given
        Integer userId = 1;
        UserDetails user = new UserDetails();
        user.setId(userId);
        user.setIsEnable(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserDetails.class))).thenReturn(user);

        // when
        Boolean result = userService.updateAccountStatus(userId, true);

        // then
        assertTrue(result);
        assertTrue(user.getIsEnable());
        verify(userRepository).save(user);
    }
}
