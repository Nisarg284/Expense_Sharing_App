package com.n23.expense_sharing_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testing {


    @GetMapping("/")
    public String home(){
        return "All Right";
    }
}
