package com.example.service.serviceImplementation;

import com.example.entity.Cart;
import com.example.entity.UserDetails;
import com.example.entity.Watch;
import com.example.repository.CartRepository;
import com.example.repository.UserRepository;
import com.example.repository.WatchRepository;
import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WatchRepository watchRepository;

    @Override
    public List<Cart> getCartsByUser(Integer userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);

        carts.sort((c1, c2) -> c1.getId().compareTo(c2.getId()));

        BigDecimal totalOrderAmount = BigDecimal.ZERO;

        for (Cart cart : carts) {
            BigDecimal amount = cart.getWatch().getPrice()
                    .multiply(BigDecimal.valueOf(cart.getQuantity()));
            cart.setAmount(amount);
            totalOrderAmount = totalOrderAmount.add(amount);
        }
        for (Cart cart : carts) {
            cart.setTotalOrderAmount(totalOrderAmount);
        }
        return carts;
    }

    @Override
    public Cart saveCart(Integer watchId, Integer userId) {
        UserDetails userDetails = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Watch watch = watchRepository.findById(watchId)
                .orElseThrow(() -> new RuntimeException("Watch not found with id: " + watchId));

        int availableStock = watch.getStock();

        if (availableStock <= 0) {
            return null;
        }

        Cart cart = cartRepository.findByWatchIdAndUserId(watchId, userId);

        if (ObjectUtils.isEmpty(cart)) {
            cart = new Cart();
            cart.setWatch(watch);
            cart.setUser(userDetails);
            cart.setQuantity(1);
            cart.setAmount(watch.getPrice());
        } else {
            if (cart.getQuantity() < availableStock) {
                cart.setQuantity(cart.getQuantity() + 1);
                cart.setAmount(watch.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
            } else {
                return null;
            }
        }

        return cartRepository.save(cart);
    }


    @Override
    public Integer getCountCart(Integer userId) {
        return cartRepository.countByUserId(userId);
    }

    @Override
    public void updateQuantity(String operation, Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));

        Watch watch = cart.getWatch();
        int updatedQuantity;
        if ("de".equalsIgnoreCase(operation)) {
            updatedQuantity = cart.getQuantity() - 1;

            if (updatedQuantity <= 0) {
                cartRepository.delete(cart);
                watch.setStock(watch.getStock() + 1);
                watchRepository.save(watch);
                return;
            }
            watch.setStock(watch.getStock() + 1);
        } else if ("in".equalsIgnoreCase(operation)) {
            if (watch.getStock() <= 0) {
                throw new RuntimeException("Product out of stock");
            }
            updatedQuantity = cart.getQuantity() + 1;

            watch.setStock(watch.getStock() - 1);
        } else {
            throw new IllegalArgumentException("Invalid quantity update operation: " + operation);
        }
        cart.setQuantity(updatedQuantity);
        cart.setAmount(watch.getPrice().multiply(BigDecimal.valueOf(updatedQuantity)));
        cartRepository.save(cart);
        watchRepository.save(watch);
    }

    @Override
    public void removeItemFromCart(int cid) {

        Cart cart = cartRepository.findById(cid)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        Watch watch = cart.getWatch();
        watch.setStock(watch.getStock() + cart.getQuantity());
        watchRepository.save(watch);
        cartRepository.delete(cart);
    }

    @Override
    public void clearCartForUser(int userId) {
        cartRepository.deleteByUserId(userId);
    }

}


