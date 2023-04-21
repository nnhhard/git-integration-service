package com.example.gitintegrationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class GitIntegrationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitIntegrationServiceApplication.class, args);
    }

}
