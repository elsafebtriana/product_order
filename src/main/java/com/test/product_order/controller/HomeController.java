package com.test.product_order.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "home")
@Slf4j
public class HomeController {
    @GetMapping("/index")
    public String home(){
        return "index";
    }
}
