package com.example.integration;

import com.example.entity.Cart;
import com.example.entity.UserDetails;
import com.example.entity.Watch;
import com.example.repository.CartRepository;
import com.example.repository.UserRepository;
import com.example.repository.WatchRepository;
import com.example.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CartServiceIntegrationTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WatchRepository watchRepository;

    @Autowired
    private CartRepository cartRepository;

    private UserDetails testUser;
    private Watch testWatch;

    @BeforeEach
    void setUp() {
        testUser = new UserDetails();
        testUser.setName("Jan");
        testUser.setSurname("Kowalski");
        testUser.setEmail("jan.kowalski@gmail.com");
        testUser.setPassword("password");
        userRepository.save(testUser);

        testWatch = new Watch();
        testWatch.setModel("s");
        testWatch.setBrand("x");
        testWatch.setPrice(new BigDecimal("50055"));
        testWatch.setStock(5);
        testWatch.setIsActive(true);
        watchRepository.save(testWatch);
    }

    @Test
    void testSaveCartAddNewWatch() {
        Cart cart = cartService.saveCart(testWatch.getId(), testUser.getId());

        assertNotNull(cart, "Cart should not be null");
        assertEquals(testUser.getId(), cart.getUser().getId());
        assertEquals(testWatch.getId(), cart.getWatch().getId());
        assertEquals(1, cart.getQuantity());
        assertEquals(testWatch.getPrice(), cart.getAmount());
    }

    @Test
    void testGetCartsByUserCalculatesAmounts() {
        cartService.saveCart(testWatch.getId(), testUser.getId());
        List<Cart> carts = cartService.getCartsByUser(testUser.getId());

        assertFalse(carts.isEmpty(), "Cart list should not be empty");
        Cart firstCart = carts.get(0);

        assertEquals(testUser.getId(), firstCart.getUser().getId());
        assertEquals(testWatch.getPrice(), firstCart.getAmount());
        assertNotNull(firstCart.getTotalOrderAmount());

        assertEquals(firstCart.getAmount(), firstCart.getTotalOrderAmount());
    }
}