package com.baseball.roto.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "run")
@Data
public class RunProperties {
    private String action;
    private String league;
    private String weeks;
}
