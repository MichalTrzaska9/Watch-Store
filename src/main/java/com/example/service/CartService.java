package com.example.service;

import com.example.entity.Cart;

import java.util.List;

public interface CartService {

    public Cart saveCart(Integer watchId, Integer userId);

    public List<Cart> getCartsByUser(Integer userId);

    public Integer getCountCart(Integer userId);

    public void updateQuantity(String operation, Integer cartId);

    public void removeItemFromCart(int cid);

    public void clearCartForUser(int userId);
}
