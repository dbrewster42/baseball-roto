package com.baseball.roto.configuration;

import com.baseball.roto.model.LeagueSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LeagueConfiguration {
//    @Value("${league}")
//    private String league;

    @Bean
    public LeagueSettings leagueSettings(@Value("${league}") String league) {
        return LeagueSettings.valueOf(league);
    }
}
