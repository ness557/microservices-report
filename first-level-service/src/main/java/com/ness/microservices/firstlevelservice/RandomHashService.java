package com.ness.microservices.firstlevelservice;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class RandomHashService {

    @Autowired
    private LoadBalancerClient loadBalanced;

    @Value("${eureka.instance.instance-id}")
    private String instanceId;

    public String calculate(String arg) {
        log.info("Generating random string.");
        String random = RandomStringUtils.random(10, true, false);
        log.info("String is generated. Performing request to another service.");

        String calculations = getCalculations(arg, random);

        log.info("Got result, [{}]", calculations);
        return calculations;
    }

    private String getCalculations(String arg, String random) {
        log.info("Choosing second level service instance.");
        ServiceInstance choose = loadBalanced.choose("second-level-service");
        log.info("Choose is [{}]", choose.getInstanceId());

        String url = choose.getUri() + String.format("/get-calculation-result?arg1=%s&arg2=%s", arg, random);

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody() + " from 1st lvl server " + instanceId;

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.REQUEST_TIMEOUT)) {
                log.error("Something went wrong: [{}]", e.getStatusText());
                return "Whoops, something went wrong. It takes long time to response.";
            }
        }
        return "";
    }
}
