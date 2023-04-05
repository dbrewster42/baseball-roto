package com.baseball.roto.configuration;

import com.baseball.roto.mapper.StatsMapper;
import com.baseball.roto.model.League;
import com.baseball.roto.model.entity.Stats;
import com.baseball.roto.repository.StatsRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.baseball.roto.validation.InputValidator.validatedLeague;

@Configuration
public class LeagueConfiguration {
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public League league(@Value("${league}") String league) {
        return validatedLeague(league);
    }

    @Bean
    public StatsRepository<? extends Stats> repository(League league) {
        return applicationContext.getBean(league.getRepository());
    }

    @Bean
    public StatsMapper<? extends Stats> statsMapper(League league) {
        return Mappers.getMapper(league.getMapper());
    }
}
