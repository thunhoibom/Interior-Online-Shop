package com.example.InteriorsECM.controller;

import com.example.InteriorsECM.dto.UserDto;
import com.example.InteriorsECM.model.mysql.UserPrincipal;
import com.example.InteriorsECM.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    public UserService userService;

    @Autowired
    void UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register-page";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDto);
            return "register-page";
        }
        userService.registerUser(userDto);
        return "redirect:/login/user";
    }


    //USER LOGIN ENDPOINTS
    @GetMapping("/login/user")
    public String userLogin(Model model, HttpServletRequest request,
                            HttpServletResponse response) {
//----------------
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setValue(""); // Clear the value
                cookie.setPath("/"); // Ensure the path matches the original cookie
                cookie.setMaxAge(0); // Expire the cookie immediately
                cookie.setHttpOnly(true); // Match original cookie settings
                cookie.setSecure(true); // Match original cookie settings
                response.addCookie(cookie); // Add the expired cookie to the response
            }
        }
//-----------
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "login-page";
    }

    @PostMapping("/login/user")
    public String userLogin(
            @Valid @ModelAttribute("user") UserDto userDto,
            BindingResult bindingResult,
            Model model,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            @RequestParam(value = "rememberPassword", required = false) String rememberPassword) {

        // Validate form input
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasFieldErrors("username") || bindingResult.hasFieldErrors("password")) {
                model.addAttribute("user", userDto);
                return "login-page";
            }
        }

        try {
            // Verify user credentials and get JWT token
            String token = userService.verifyCustomer(userDto);

            // Check authentication principal
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication.getPrincipal() instanceof UserPrincipal userPrincipal)) {
                redirectAttributes.addFlashAttribute("error", "Lỗi xác thực người dùng");
                return "redirect:/login/user";
            }

            // Extract user and cart IDs
            int cartId = userPrincipal.getUser().getCart().getId();
            int userId = userPrincipal.getUser().getUser_id();

            // Create cookies for cart ID and user ID
            Cookie cartIdCookie = new Cookie("cart-id", String.valueOf(cartId));
            Cookie userIdCookie = new Cookie("uid", String.valueOf(userId));

            // Secure cookie settings
            cartIdCookie.setHttpOnly(true);
            cartIdCookie.setPath("/");
            userIdCookie.setHttpOnly(true);
            userIdCookie.setPath("/");

            // Create JWT cookie
            Cookie jwtCookie = new Cookie("jwtoken", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");

            // Set cookie expiration based on rememberPassword
            int cookieMaxAge = "yes".equals(rememberPassword) ? 7 * 24 * 60 * 60 : 1 * 60 * 60; // 7 days or 1 hour
            jwtCookie.setMaxAge(cookieMaxAge);
            cartIdCookie.setMaxAge(cookieMaxAge);
            userIdCookie.setMaxAge(cookieMaxAge);

            // Add cookies to response
            response.addCookie(jwtCookie);
            response.addCookie(cartIdCookie);
            response.addCookie(userIdCookie);

            // Add success message
            redirectAttributes.addFlashAttribute("loginSuccess", true);
            return "redirect:/menu/products";

        } catch (BadCredentialsException e) {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
            model.addAttribute("user", new UserDto());
            return "redirect:/login/user";
        } catch (AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền truy cập");
            model.addAttribute("user", new UserDto());
            return "redirect:/login/user";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi không mong muốn");
            model.addAttribute("user", new UserDto());
            return "redirect:/login/user";
        }
    }


    //ADMIN LOGIN ENDPOINTS
    @GetMapping("/login/admin")
    public String adminLogin(Model model, HttpServletRequest request,
                            HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                response.addCookie(cookie);
            }
        }
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "/admin/admin-login.html";
    }
    @PostMapping("/login/admin")
    public String adminLogin(@Valid @ModelAttribute("user") UserDto userDto,
                             BindingResult bindingResult,
                             Model model,
                            HttpServletResponse response,
                             RedirectAttributes redirectAttributes,
                             @RequestParam(value="rememberPassword", required = false) String rememberPassword) {
        if(bindingResult.hasErrors()){
            boolean hasUsernameError = bindingResult.hasFieldErrors("username");
            boolean hasPasswordError = bindingResult.hasFieldErrors("password");
            if (hasUsernameError || hasPasswordError) {
                model.addAttribute("user", userDto);
                return "login-page";
            }
        }

        try {
            String token = userService.verifyAdmin(userDto);
            if (token != null) {
                Cookie cookie = new Cookie("jwtoken", token);
                cookie.setHttpOnly(true);
                cookie.setPath("/"); // Cookie available for entire app
                if(rememberPassword == "yes"){
                    cookie.setMaxAge(7 * 24 * 60 * 60);
                }else{
                    cookie.setMaxAge(1 * 60 * 60);
                }
                response.addCookie(cookie);
                redirectAttributes.addFlashAttribute("loginSuccess", true);
                return "redirect:/admin/dashboard";
            }
        }catch (BadCredentialsException e) {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
            model.addAttribute("user", new UserDto());
            return "redirect:/login/admin";
        }catch (AccessDeniedException | java.nio.file.AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền truy cập");
            model.addAttribute("user", new UserDto());
            return "redirect:/login/admin";
        }
        return "login-page";
    }

    @GetMapping("/forgot-password")
    public String resetPassword() {
        return "reset-password";
    }


}
