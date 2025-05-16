package com.example.InteriorsECM.controller;

import com.example.InteriorsECM.dto.UserDto;
import com.example.InteriorsECM.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
        return "redirect:/menu/products";
    }


    //USER LOGIN ENDPOINTS
    @GetMapping("/login")
    public String userLogin(Model model, HttpServletRequest request,
                            HttpServletResponse response) {
//----------------
//Reset cookies
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

    @PostMapping("/login")
    public String userLogin(@ModelAttribute("user") UserDto userDto, Model model,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        String token = userService.verify(userDto);
        if (token != null) {
            Cookie cookie = new Cookie("jwtoken", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/"); // Cookie available for entire app
            cookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(cookie);
            return "redirect:/menu/products";
        } else {
            UserDto user = new UserDto();
            model.addAttribute("user", user);
            return "login-page";
        }
    }


    //ADMIN LOGIN ENDPOINTS
    @GetMapping("/admin/login")
    public String adminLogin(Model model, HttpServletRequest request,
                            HttpServletResponse response) {
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
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "/admin/admin-login.html";
    }
    @PostMapping("/admin/login")
    public String adminLogin(@ModelAttribute("user") UserDto userDto, Model model,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        String token = userService.verify(userDto);
        if (token != null) {
            Cookie cookie = new Cookie("jwtoken", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/"); // Cookie available for entire app
            cookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(cookie);
            return "redirect:/admin/dashboard";
        } else {
            UserDto user = new UserDto();
            model.addAttribute("user", user);
            return "admin/admin-login";
        }
    }
}
