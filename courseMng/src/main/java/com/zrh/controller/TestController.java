package com.zrh.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/sms/test")
@RestController
public class TestController {
    @GetMapping("helloWorld")
    public String fun() {
        return "hello world";
    }
}
