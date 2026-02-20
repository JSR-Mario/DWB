package com.customer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/helloworld")
public class CtrlHelloWorld{
    @GetMapping
    public String helloWorld(){
        return "Hello World";
    }
}
