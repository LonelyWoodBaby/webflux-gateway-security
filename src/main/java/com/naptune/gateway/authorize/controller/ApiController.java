package com.naptune.gateway.authorize.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
    @RequestMapping("/test")
    public String testPrivate(String name){
        System.out.println(name);
        return "已进入方法";
    }
}
