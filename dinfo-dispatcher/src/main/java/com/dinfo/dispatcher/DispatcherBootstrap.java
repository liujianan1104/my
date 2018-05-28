package com.dinfo.dispatcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @author yangxf
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class DispatcherBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(DispatcherBootstrap.class, args);
    }
}
