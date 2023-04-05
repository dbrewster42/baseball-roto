package com.baseball.roto.configuration;

import com.baseball.roto.model.League;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.baseball.roto.validation.InputValidator.validatedLeague;

@Configuration
public class LeagueConfiguration {
    @Bean
    public League league(@Value("${league}") String league) {
        return validatedLeague(league);
    }
}
