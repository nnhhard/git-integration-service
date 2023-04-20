package com.example.gitintegrationservice.config;

import com.example.gitintegrationservice.dto.FileDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration
public class ConcurrentHashMapConfig {

    @Bean
    public ConcurrentMap<String, FileDto> concurrentHashMapFileDto() {
        return new ConcurrentHashMap<>();
    }
}
