package com.example.controller;

import com.example.entity.*;
import com.example.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WatchService watchService;

    @GetMapping("/")
    public String home() {
        return "user/home";
    }

    @ModelAttribute
    public void getUser(Principal principal, Model model) {
        if (principal != null) {
            String email = principal.getName();
            UserDetails userDetails = userService.getUserByEmail(email);
            model.addAttribute("user", userDetails);
            Integer countCart = cartService.getCountCart(userDetails.getId());
            model.addAttribute("countCart", countCart);
        }
        List<Brand> allActiveBrand = brandService.getAllActiveBrand();
        model.addAttribute("brands", allActiveBrand);
    }

    @GetMapping("/account")
    public String account() {
        return "/user/account";
    }

    @PostMapping("/edit_account")
    public String editAccount(HttpSession httpSession, @ModelAttribute UserDetails userDetails) {

        httpSession.removeAttribute("errorMessage");
        httpSession.removeAttribute("successMessage");

        UserDetails editUserAccount = userService.editUserAccount(userDetails);
        if (!ObjectUtils.isEmpty(editUserAccount)) {
            httpSession.setAttribute("successMessage", "Konto użytkownika zostało edytowane");
        } else {
            httpSession.setAttribute("errorMessage", "Błąd, konto użytkownika nie zostało edytowane");
        }
        return "redirect:/user/account";
    }

    @PostMapping("/change_user_password")
    public String changeUserPassword(@RequestParam String password, @RequestParam String newPassword,
                                     @RequestParam String repeatPassword,
                                     Principal principal, HttpSession httpSession) {

        httpSession.removeAttribute("errorMessage");
        httpSession.removeAttribute("successMessage");

        UserDetails loggedInUser = getLoggedInUserDetails(principal);

        boolean matches = passwordEncoder.matches(password, loggedInUser.getPassword());

        if (!newPassword.equals(repeatPassword)) {
            httpSession.setAttribute("errorMessage", "Błędnie powtórzyłeś hasło");
            return "redirect:/user/account";
        } else if (matches) {
            String passwordProtected = passwordEncoder.encode(newPassword);
            loggedInUser.setPassword(passwordProtected);
            UserDetails editUser = userService.editUser(loggedInUser);
            if (!ObjectUtils.isEmpty(editUser)) {
                httpSession.setAttribute("successMessage", "Hasło zostało zmienione");
            } else {
                httpSession.setAttribute("errorMessage", "Błąd serwera, hasło nie zostało zmienione");
            }
        } else {
            httpSession.setAttribute("errorMessage", "Błędne aktualne hasło");
        }
        return "redirect:/user/account";
    }

    @GetMapping("/addCart")
    public String addCart(@RequestParam Integer wid, @RequestParam Integer uid, HttpSession httpSession, Model model) {
        Watch watch = watchService.getWatchById(wid);

        if (watch.getStock() <= 0) {
            httpSession.setAttribute("errorMessage", "Zegarek nie jest dostępny w magazynie i nie może zostać dodany do koszyka.");
        } else {
            Cart saveCart = cartService.saveCart(wid, uid);
            if (ObjectUtils.isEmpty(saveCart)) {
                httpSession.setAttribute("errorMessage", "Zegarek nie może zostać dodany do koszyka.");
            } else {
                watch.setStock(watch.getStock() - 1);
                watchService.saveWatch(watch);
                httpSession.setAttribute("successMessage", "Zegarek dodany do koszyka.");
            }
        }

        model.addAttribute("watch", watch);

        return "redirect:/watch/" + wid;
    }

    @GetMapping("/cart")
    public String loadCart(Principal principal, Model model) {
        UserDetails user = getLoggedInUserDetails(principal);
        List<Cart> carts = cartService.getCartsByUser(user.getId());

        if (carts == null || carts.isEmpty()) {
            return "redirect:/user/empty_cart";
        }
        model.addAttribute("carts", carts);
        BigDecimal totalOrderAmount = carts.stream()
                .map(Cart::getTotalOrderAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("totalOrderAmount", totalOrderAmount);
        return "/user/cart";
    }

    @GetMapping("/empty_cart")
    public String emptyCart() {
        return "user/empty_cart";
    }

    @GetMapping("/cartQuantityUpdate")
    public String updateCartQuantity(@RequestParam String operation, @RequestParam Integer cartId, HttpSession httpSession) {
        try {
            cartService.updateQuantity(operation, cartId);
            httpSession.setAttribute("successMessage", "Koszyk zaktualizowany");
        } catch (RuntimeException e) {
            httpSession.setAttribute("errorMessage", "Brak produktu w magazynie");
        }
        return "redirect:/user/cart";
    }

    private UserDetails getLoggedInUserDetails(Principal principal) {
        String email = principal.getName();
        return userService.getUserByEmail(email);
    }

    @PostMapping("/user/removeFromCart")
    public String removeFromCart(@RequestParam("cartId") int cartId) {
        cartService.removeItemFromCart(cartId);
        return "redirect:/user/cart";
    }


    @GetMapping("/orders")
    public String orders(Principal principal, Model model) {
        UserDetails userDetails = getLoggedInUserDetails(principal);
        List<Cart> carts = cartService.getCartsByUser(userDetails.getId());
        model.addAttribute("carts", carts);
        if (!carts.isEmpty()) {
            BigDecimal orderPrice = carts.get(carts.size() - 1).getTotalOrderAmount();
            BigDecimal orderAmount = carts.get(carts.size() - 1).getTotalOrderAmount();
            model.addAttribute("orderPrice", orderPrice);
            model.addAttribute("orderAmount", orderAmount);
        }
        return "/user/order";
    }


    @PostMapping("/save-order")
    public String saveOrder(@ModelAttribute Order order, Principal principal) {

        UserDetails userDetails = getLoggedInUserDetails(principal);
        orderService.saveOrder(userDetails.getId(), order);
        cartService.clearCartForUser(userDetails.getId());
        return "redirect:/user/successful_order";
    }

    @GetMapping("/successful_order")
    public String loadSuccessfulOrder() {
        return "/user/successful_order";
    }

    @GetMapping("/my-orders")
    public String userOrders(Model model, Principal principal) {
        UserDetails loginUser = getLoggedInUserDetails(principal);
        List<WatchOrder> orders = orderService.getOrdersByUser(loginUser.getId());
        model.addAttribute("orders", orders);
        return "/user/user_orders";
    }


}
