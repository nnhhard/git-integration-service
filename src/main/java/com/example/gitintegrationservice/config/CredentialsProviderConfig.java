package com.example.gitintegrationservice.config;

import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CredentialsProviderConfig {
    @Bean
    public UsernamePasswordCredentialsProvider credentialsProvider(
            @Value("${git.login}") String login,
            @Value("${git.password}") String password) {
        return new UsernamePasswordCredentialsProvider(login, password);
    }
}