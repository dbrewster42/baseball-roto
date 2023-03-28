package com.baseball.roto.configuration;

import com.baseball.roto.exception.BadInput;
import com.baseball.roto.model.League;
import com.baseball.roto.model.entity.ChampStats;
import com.baseball.roto.model.entity.PsdStats;
import com.baseball.roto.repository.ChampRepository;
import com.baseball.roto.repository.PsdRepository;
import com.baseball.roto.repository.StatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
public class RepositoryConfiguration {
    @Autowired private StatsRepository<ChampStats> champRepository;
    @Autowired private StatsRepository<PsdStats> psdRepository;

    @Bean
    public StatsRepository<?> repository(League league) {
        if (league.equals(League.CHAMPIONS)) {
            return champRepository;
        } else if (league.equals(League.PSD)) {
            return psdRepository;
        }
        throw new BadInput("The given league is not valid");
    }
}
