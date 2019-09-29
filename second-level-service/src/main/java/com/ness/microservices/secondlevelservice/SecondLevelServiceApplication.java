package com.ness.microservices.secondlevelservice;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
public class SecondLevelServiceApplication {

    @Autowired
    private MoreCalculationService moreCalculationService;

    public static void main(String[] args) {
        SpringApplication.run(SecondLevelServiceApplication.class, args);
    }

    @HystrixCommand(fallbackMethod = "getFallbackValue", commandProperties = {
            //more than 3 timeout requests in a rolling window - open circuit
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
            //rolling window in ms
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000"),
            //how long circuit should be opened
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),
            //max timeout for method execution
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "900"),
            //30% of 'bad' responses - opens
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "30")
    })
    @GetMapping("/get-calculation-result")
    public ResponseEntity<String> calculate(@RequestParam("arg1") String arg1,
                                            @RequestParam("arg2") String arg2)
            throws NoSuchAlgorithmException {

        return ResponseEntity.ok(moreCalculationService.calculate(arg1, arg2));
    }

    private ResponseEntity<String> getFallbackValue(String arg1, String arg2) {
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
    }
}
