package com.baseball.roto.configuration;

import com.baseball.roto.exception.BadInput;
import com.baseball.roto.model.League;
import com.baseball.roto.model.entity.Stats;
import com.baseball.roto.repository.StatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.baseball.roto.model.League.leagueNames;
import static java.lang.String.format;

@Configuration
public class LeagueConfiguration {
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public League league(@Value("${league}") String league) {
        try {
            return League.valueOf(league);
        } catch (Exception e) {
            throw new BadInput(format("The given league [%s] must be one of the following supported options - %s", league, leagueNames()));
        }
    }

    @Bean
    public StatsRepository<? extends Stats> repository(League league) {
        return applicationContext.getBean(league.getRepository());
    }

}
