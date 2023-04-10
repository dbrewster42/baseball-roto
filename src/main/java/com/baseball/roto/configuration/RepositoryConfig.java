package com.baseball.roto.configuration;

import com.baseball.roto.model.League;
import com.baseball.roto.model.entity.Stats;
import com.baseball.roto.repository.StatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    @Autowired
    private ApplicationContext applicationContext;


    @Bean
    public StatsRepository repository(League league) {
        return applicationContext.getBean(league.getRepository());
    }
}
