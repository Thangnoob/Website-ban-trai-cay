package com.nhom15.cloudgateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackController {

    @GetMapping("/orderServiceFallBack")
    public String orderServiceFallBack(){
        return "Order Service is down";
    }

    @GetMapping("/productServiceFallBack")
    public String productServiceFallBack(){
        return "product Service is down";
    }

    @GetMapping("/paymentServiceFallBack")
    public String paymentServiceFallBack(){
        return "payment Service is down";
    }

    @GetMapping("/userServiceFallBack")
    public String userServiceFallBack(){
        return "user Service is down";
    }
}
