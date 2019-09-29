package com.ness.microservices.firstlevelservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableEurekaClient
@SpringBootApplication
public class FirstLevelServiceApplication {

    @Autowired
    private RandomHashService service;

    public static void main(String[] args) {
        SpringApplication.run(FirstLevelServiceApplication.class, args);
    }

    @GetMapping("/getRandomHash")
    public String getRandomHash(@RequestParam("arg") String arg) {
        return service.calculate(arg);
    }
}
