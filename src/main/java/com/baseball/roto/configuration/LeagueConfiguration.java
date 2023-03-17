package com.baseball.roto.configuration;

import com.baseball.roto.model.League;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LeagueConfiguration {
//    @Value("${league}")
//    private String league;

    @Bean
    public League leagueSettings(@Value("${league}") String league) {
        return League.valueOf(league);
    }
}
