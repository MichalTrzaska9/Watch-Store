package com.example.controller;

import com.example.entity.Brand;
import com.example.entity.UserDetails;
import com.example.entity.Watch;
import com.example.entity.WatchOrder;
import com.example.service.*;
import com.example.util.OrderStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private WatchService watchService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private PasswordEncoder passwordEncoder;


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

    @GetMapping
    public String index() {
        return "admin/index";
    }

    @GetMapping("/addAdminAccount")
    public String addAdminAccount(HttpSession httpSession) {

        return "/admin/add_admin_account";
    }

    @PostMapping("/saveAdminAccount")
    public String saveAdminAccount(HttpSession httpSession, @ModelAttribute UserDetails userDetails) {

        UserDetails saveAdmin = userService.saveAdmin(userDetails);

        if (ObjectUtils.isEmpty(saveAdmin)) {
            httpSession.setAttribute("errorMessage", "Błąd serwera");
        } else {
            httpSession.setAttribute("successMessage", "Konto administratora zostało dodane");
        }

        return "redirect:/admin/addAdminAccount";
    }

    @GetMapping("/account")
    public String account() {
        return "/admin/account";
    }

    @PostMapping("/editAccount")
    public String editAccount(HttpSession httpSession, @ModelAttribute UserDetails userDetails) {
        UserDetails editAdminAccount = userService.editUserAccount(userDetails);

        if (!ObjectUtils.isEmpty(editAdminAccount)) {
            httpSession.setAttribute("successMessage", "Konto użytkownika zostało edytowane");
        } else {
            httpSession.setAttribute("errorMessage", "Błąd, konto użytkownika nie zostało edytowane");
        }
        return "redirect:/admin/account";
    }

    private UserDetails getLoggedInAdminDetails(Principal principal) {
        String email = principal.getName();
        return userService.getUserByEmail(email);
    }

    @PostMapping("/changeAdminPassword")
    public String changeAdminPassword(@RequestParam String password, @RequestParam String newPassword,
                                      @RequestParam String repeatPassword,
                                      Principal principal, HttpSession httpSession) {


        UserDetails loggedInAdmin = getLoggedInAdminDetails(principal);

        boolean matches = passwordEncoder.matches(password, loggedInAdmin.getPassword());

        if (!newPassword.equals(repeatPassword)) {
            httpSession.setAttribute("errorMessage", "Błędnie powtórzyłeś hasło");
            return "redirect:/admin/account";
        } else if (matches) {
            String passwordProtected = passwordEncoder.encode(newPassword);
            loggedInAdmin.setPassword(passwordProtected);
            UserDetails editUser = userService.editUser(loggedInAdmin);
            if (!ObjectUtils.isEmpty(editUser)) {
                httpSession.setAttribute("successMessage", "Hasło zostało zmienione");
            } else {
                httpSession.setAttribute("errorMessage", "Błąd serwera, hasło nie zostało zmienione");
            }
        } else {
            httpSession.setAttribute("errorMessage", "Błędne aktualne hasło");

        }
        return "redirect:/admin/account";
    }

    @GetMapping("/brand")
    public String brand(Model model) {
        model.addAttribute("brands", brandService.getAllBrand());
        return "admin/brand";
    }


    @PostMapping("/saveBrand")
    public String saveBrand(@ModelAttribute Brand brand, @RequestParam("file") MultipartFile file,
                            HttpSession httpSession)
            throws IOException {


        String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
        brand.setImageName(imageName);

        Boolean existBrand = brandService.existBrand(brand.getName());

        if (existBrand) {
            httpSession.setAttribute("errorMessage", "Marka już istnieje");
        } else {
            Brand saveBrand = brandService.saveBrand(brand);

            if (ObjectUtils.isEmpty(saveBrand)) {
                httpSession.setAttribute("errorMessage", "Błąd serwera");
            } else {

                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "brand_img" + File.separator
                        + file.getOriginalFilename());

                System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                httpSession.setAttribute("successMessage", "Marka zapisana");
            }
        }
        return "redirect:/admin/brand";
    }

    @GetMapping("/loadEditBrand/{id}")
    public String loadEditBrand(@PathVariable int id, Model m) {
        m.addAttribute("brand", brandService.getBrandById(id));
        return "admin/edit_brand";
    }

    @PostMapping("/updateBrand")
    public String updateBrand(@ModelAttribute Brand brand, @RequestParam("file") MultipartFile file, HttpSession httpSession)
            throws IOException {


        Brand oldBrand = brandService.getBrandById(brand.getId());
        String imageName = file.isEmpty() ? oldBrand.getImageName() : file.getOriginalFilename();

        if (!ObjectUtils.isEmpty(brand)) {
            oldBrand.setName(brand.getName());
            oldBrand.setIsActive(brand.getIsActive());
            oldBrand.setImageName(imageName);
        }
        Brand updateBrand = brandService.saveBrand(oldBrand);

        if (!ObjectUtils.isEmpty(updateBrand)) {

            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "brand_img" + File.separator
                        + file.getOriginalFilename());

                System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            httpSession.setAttribute("successMessage", "Marka edytowana");
        } else {
            httpSession.setAttribute("errorMessage", "Błąd serwera");
        }
        return "redirect:/admin/loadEditBrand/" + brand.getId();
    }

    @GetMapping("/deleteBrand/{id}")
    public String deleteBrand(@PathVariable int id, HttpSession httpSession) {

        Boolean deleteBrand = brandService.deleteBrand(id);

        if (deleteBrand) {
            httpSession.setAttribute("successMessage", "Marka usunięta");
        } else {
            httpSession.setAttribute("errorMessage", "Błąd serwera");
        }
        return "redirect:/admin/brand";
    }


    @GetMapping("/loadAddWatch")
    public String loadAddWatch(Model model) {
        List<Brand> brands = brandService.getAllBrand();
        model.addAttribute("brands", brands);
        return "admin/add_watch";
    }

    @PostMapping("/saveWatch")
    public String saveWatch(@ModelAttribute Watch watch, @RequestParam("file") MultipartFile image,
                            HttpSession httpSession) throws IOException {


        String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();

        watch.setImage(imageName);

        Watch saveWatch = watchService.saveWatch(watch);

        if (!ObjectUtils.isEmpty(saveWatch)) {
            File saveFile = new ClassPathResource("static/img").getFile();
            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "brand_img" + File.separator
                    + image.getOriginalFilename());

            System.out.println(path);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            httpSession.setAttribute("successMessage", "Zegarek zapisany");
        } else {
            httpSession.setAttribute("errorMessage", "Błąd serwera");
        }
        return "redirect:/admin/loadAddWatch";
    }

    @GetMapping("/watches")
    public String loadViewWatches(Model model,
                                  @RequestParam(name = "pageSize", defaultValue = "12") Integer pageSize,
                                  @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
                                  @RequestParam(defaultValue = "") String x) {

        Page<Watch> viewedPage = null;

        if (StringUtils.hasLength(x)) {
            viewedPage = watchService.searchWatchPagination(pageNumber, pageSize, x);
        } else {
            viewedPage = watchService.getAllWatchesPagination(pageNumber, pageSize);
        }

        List<Watch> watches = new ArrayList<>(viewedPage.getContent());

        watches.sort(Comparator.comparingLong(Watch::getId).reversed());
        model.addAttribute("watches", watches);
        model.addAttribute("watchesSize", watches.size());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageNumber", viewedPage.getNumber());
        model.addAttribute("firstPage", viewedPage.isFirst());
        model.addAttribute("lastPage", viewedPage.isLast());
        model.addAttribute("sumPages", viewedPage.getTotalPages());
        model.addAttribute("sumElements", viewedPage.getTotalElements());

        return "admin/watches";
    }


    @GetMapping("/editWatch/{id}")
    public String editWatch(@PathVariable int id, Model model) {
        model.addAttribute("watch", watchService.getWatchById(id));
        model.addAttribute("brands", brandService.getAllBrand());
        return "admin/edit_watch";
    }

    @PostMapping("/updateWatch")
    public String updateWatch(@ModelAttribute Watch watch, @RequestParam("file") MultipartFile image,
                              HttpSession httpSession, Model model) {

        String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();

        watch.setImage(imageName);

        Watch updateWatch = watchService.updateWatch(watch, image);
        if (!ObjectUtils.isEmpty(updateWatch)) {
            httpSession.setAttribute("successMessage", "Zegarek edytowany");
        } else {
            httpSession.setAttribute("errorMessage", "Błąd serwera");
        }
        return "redirect:/admin/editWatch/" + watch.getId();

    }

    @GetMapping("/deleteWatch/{id}")
    public String deleteWatch(@PathVariable int id, HttpSession httpSession) {

        try {
            Boolean deleteWatch = watchService.deleteWatch(id);
            if (deleteWatch) {
                httpSession.setAttribute("successMessage", "Zegarek usunięty");
            } else {
                httpSession.setAttribute("errorMessage", "Błąd serwera");
            }
        } catch (DataIntegrityViolationException e) {
            httpSession.setAttribute("errorMsg", "Nie można usunąć zegarka, ponieważ jest on dodany do koszyka użytkownika.");
        } catch (Exception e) {
            httpSession.setAttribute("errorMessage", "Wystąpił nieoczekiwany błąd.");
        }

        return "redirect:/admin/watches";
    }

    @GetMapping("/users")
    public String getUsers(@RequestParam Integer role,
                           @RequestParam(required = false) String x,
                           Model model) {
        List<UserDetails> users = null;

        String roleType = (role == 1) ? "ROLE_ADMIN" : "ROLE_USER";

        if (StringUtils.hasLength(x)) {
            String[] parts = x.split(" ", 2);
            String name = "";
            String surname = "";

            if (parts.length == 1) {
                surname = parts[0];
            } else {
                name = parts[0];
                surname = parts[1];
            }

            users = userService.searchUsers(roleType, name, surname);
        } else {
            users = userService.getUsers(roleType);
        }
        if (users != null) {
            users.sort(Comparator.comparingLong(UserDetails::getId).reversed());
        }
        model.addAttribute("userRole", role);
        model.addAttribute("users", users);
        return "/admin/users";
    }

    @GetMapping("/updateStatus")
    public String updateAccountStatus(@RequestParam Integer role, @RequestParam Boolean status,
                                      @RequestParam Integer id, HttpSession httpSession) {
        Boolean f = userService.updateAccountStatus(id, status);
        if (f) {
            httpSession.setAttribute("successMessage", "Status konta zaktualizowany");
        } else {
            httpSession.setAttribute("errorMessage", "Błąd serwera");
        }
        return "redirect:/admin/users?role=" + role;
    }

    @GetMapping("/order_list")
    public String getOrderList(Model model, HttpSession httpSession,
                               @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize,
                               @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {

        Page<WatchOrder> viewedPage = orderService.getAllOrdersPagination(pageNumber, pageSize);

        model.addAttribute("orders", viewedPage.getContent());
        model.addAttribute("search", false);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageNumber", viewedPage.getNumber());
        model.addAttribute("firstPage", viewedPage.isFirst());
        model.addAttribute("lastPage", viewedPage.isLast());
        model.addAttribute("sumPages", viewedPage.getTotalPages());

        for (WatchOrder order : viewedPage.getContent()) {
            UserDetails userDetails = order.getUserDetails();
            if (userDetails != null) {
                model.addAttribute("customerName", userDetails.getName() + " "
                        + userDetails.getSurname());
                model.addAttribute("customerAddress", userDetails.getStreet() + ", "
                        + userDetails.getCity() + ", " + userDetails.getCountry());
                model.addAttribute("customerEmail", userDetails.getEmail());
                model.addAttribute("customerPhone", userDetails.getPhone());
                model.addAttribute("customerPostcode", userDetails.getPostcode());
            }
        }
        return "/admin/order_list";
    }


    @PostMapping("/updateOrderStatus")
    public String updateOrderStatus(@RequestParam Integer id,
                                    @RequestParam Integer orderStatus,
                                    HttpSession httpSession) {
        OrderStatus[] values = OrderStatus.values();
        String status = null;

        for (OrderStatus orderSt : values) {
            if (orderSt.getId().equals(orderStatus)) {
                status = orderSt.getName();
            }
        }
        WatchOrder updateOrder = orderService.updateOrderStatus(id, status);

        if (ObjectUtils.isEmpty(updateOrder)) {
            httpSession.setAttribute("errorMessage", "Błąd, status zamówienia niezakutalizowany");
        } else {
            httpSession.setAttribute("successMessage", "Status zamówienia zaktualizowany");
        }

        return "redirect:/admin/order_list";
    }

    @GetMapping("/search_order")
    public String searchWatch(Model model, @RequestParam String orderId, HttpSession httpSession,
                              @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize,
                              @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {

        model.addAttribute("httpSession", httpSession);

        if (!StringUtils.hasLength(orderId)) {
            Page<WatchOrder> viewedPage = orderService.getAllOrdersPagination(pageNumber, pageSize);
            model.addAttribute("orders", viewedPage);
            model.addAttribute("search", false);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("pageNumber", viewedPage.getNumber());
            model.addAttribute("firstPage", viewedPage.isFirst());
            model.addAttribute("lastPage", viewedPage.isLast());
            model.addAttribute("sumPages", viewedPage.getTotalPages());
        } else {
            WatchOrder watchOrder = orderService.getOrdersByOrderId(orderId.trim());

            if (!ObjectUtils.isEmpty(watchOrder)) {
                model.addAttribute("orderDetails", watchOrder);
                model.addAttribute("search", true);
            } else {
                httpSession.setAttribute("errorMessage", "Niepoprawne id zamówienia: " + orderId);
                model.addAttribute("search", false);

                Page<WatchOrder> viewedPage = orderService.getAllOrdersPagination(pageNumber, pageSize);
                model.addAttribute("orders", viewedPage);
                model.addAttribute("pageSize", pageSize);
                model.addAttribute("pageNumber", viewedPage.getNumber());
                model.addAttribute("firstPage", viewedPage.isFirst());
                model.addAttribute("lastPage", viewedPage.isLast());
                model.addAttribute("sumPages", viewedPage.getTotalPages());
            }
        }
        return "/admin/order_list";
    }


}
