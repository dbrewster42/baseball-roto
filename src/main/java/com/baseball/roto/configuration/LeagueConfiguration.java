package com.baseball.roto.configuration;

import com.baseball.roto.exception.BadInput;
import com.baseball.roto.model.League;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.lang.String.format;

@Configuration
public class LeagueConfiguration {
    @Bean
    public League league(@Value("${league}") String league) {
        try {
            return League.valueOf(league);
        } catch (Exception e) {
            throw new BadInput(format("The given league [%s] is invalid [%s]", league, e.getMessage()));
        }
    }
}
