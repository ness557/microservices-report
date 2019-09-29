package com.ness.microservices.secondlevelservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Slf4j
@Service
public class MoreCalculationService {

    @Value("${eureka.instance.instance-id}")
    private String instanceId;

    public String calculate(String arg1, String arg2)
            throws NoSuchAlgorithmException {

        log.info("Received params: [{}], [{}]", arg1, arg2);
        log.info("Starting calculations.");

        int i = new Random().nextInt(1500);
        log.info("Going to sleep for [{}] ms.", i);

        try {
            Thread.sleep(i);
        } catch (InterruptedException ignored) {
        }

        MessageDigest md5 = MessageDigest.getInstance("MD5");

        md5.update(arg2.getBytes());
        byte[] hash = md5.digest(arg1.getBytes());

        log.info("Calculations finished.");
        return hash + " from 2nd lvl server " + instanceId;
    }

}
