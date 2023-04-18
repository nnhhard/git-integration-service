package com.example.gitintegrationservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "git")
public class AppSetting implements GitDetailsConfig {

    private String url;
    private String folder;
    private String branch;
}
