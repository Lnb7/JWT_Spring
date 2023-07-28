package com.authentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {


    @GetMapping("/welcome")
    public String getRequest(){
        return "Hi this authorized request";
    }
}
