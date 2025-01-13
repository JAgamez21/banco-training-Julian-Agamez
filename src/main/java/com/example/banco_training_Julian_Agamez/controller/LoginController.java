package com.example.banco_training_Julian_Agamez.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class LoginController {
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
