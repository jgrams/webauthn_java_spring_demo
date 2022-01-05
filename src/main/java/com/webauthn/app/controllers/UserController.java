package com.webauthn.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @GetMapping("/")
    public String welcome() {
        return "index";
    }

    @PostMapping("/")
    public String register() {
        return "Hello!";
    }

    @GetMapping("/signin")
    public String existingUser() {
        return "signin";
    }

    @PostMapping("/signin")
    public String logIn() {
        return "Hello2!";
    }
}
