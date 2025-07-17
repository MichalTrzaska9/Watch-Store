package com.example.controller;

import com.example.entity.Brand;
import com.example.entity.UserDetails;
import com.example.entity.Watch;
import com.example.service.BrandService;
import com.example.service.CartService;
import com.example.service.UserService;
import com.example.service.WatchService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class UnloggedUserController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private WatchService watchService;

    @Autowired
    private UserService userService;


    @Autowired
    private CartService cartService;

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

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute UserDetails userDetails, HttpSession httpSession) {

        Boolean existsUser = userService.existsUser(userDetails.getEmail());

        if (!existsUser) {
            UserDetails saveUser = userService.saveUser(userDetails);

            if (!ObjectUtils.isEmpty(saveUser)) {
                httpSession.setAttribute("successMessage", "Rejestracja udana");
            } else {
                httpSession.setAttribute("errorMsg", "Błąd serwera");
            }
        } else {
            httpSession.setAttribute("errorMessage", "Użytkownik o podanym e-mailu jest już zarejestrowany");
        }
        return "redirect:/register";
    }

    @GetMapping("/watches")
    public String watches(Model model, @RequestParam(defaultValue = "") String x,
                          @RequestParam(value = "brand", defaultValue = "") String brand,
                          @RequestParam(name = "pageSize", defaultValue = "12") Integer pageSize,
                          @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber

    ) {
        List<Brand> brands = brandService.getAllActiveBrand();
        model.addAttribute("desiredBrand", brand);
        model.addAttribute("brands", brands);

        Page<Watch> viewedPage = null;
        if (StringUtils.hasLength(x)) {
            viewedPage = watchService.searchActiveWatchPagination(brand, pageNumber, pageSize, x);
        } else {
            viewedPage = watchService.getAllActiveWatchPagination(brand, pageNumber, pageSize);
        }

        List<Watch> watches = viewedPage.getContent();
        model.addAttribute("watches", watches);
        model.addAttribute("watchesSize", watches.size());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageNumber", viewedPage.getNumber());
        model.addAttribute("firstPage", viewedPage.isFirst());
        model.addAttribute("lastPage", viewedPage.isLast());
        model.addAttribute("sumPages", viewedPage.getTotalPages());
        return "watches";
    }

    @GetMapping("/watch/{id}")
    public String watch(@PathVariable int id, Model model) {
        Watch watch = watchService.getWatchById(id);
        model.addAttribute("watch", watch);
        return "view_watch";
    }


    @GetMapping("/search_watch")
    public String searchWatch(@RequestParam String search, Model model) {
        List<Watch> searchWatches = watchService.searchWatch(search);
        model.addAttribute("watches", searchWatches);
        List<Brand> brands = brandService.getAllActiveBrand();
        model.addAttribute("brands", brands);
        return "watches";
    }
}
