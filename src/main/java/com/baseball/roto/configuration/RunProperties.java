package com.baseball.roto.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties
@PropertySource("file:options.properties")
@Data
public class RunProperties {
    private String action;
    private String league;
    private Integer weeks;
    private String oldName;
    private String newName;
    private Integer numberOfPlayers;
}
