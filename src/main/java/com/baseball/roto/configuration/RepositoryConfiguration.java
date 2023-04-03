package com.baseball.roto.configuration;

import com.baseball.roto.model.League;
import com.baseball.roto.model.entity.ChampStats;
import com.baseball.roto.model.entity.PsdStats;
import com.baseball.roto.model.entity.StandardStats;
import com.baseball.roto.model.entity.Stats;
import com.baseball.roto.repository.StatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {
    @Autowired private StatsRepository<ChampStats> champRepository;
    @Autowired private StatsRepository<PsdStats> psdRepository;
    @Autowired private StatsRepository<StandardStats> standardRepository;

    @Bean
    public StatsRepository<? extends Stats> repository(League league) {
        switch (league) {
            case CHAMPIONS:
                return champRepository;
            case PSD:
                return psdRepository;
            case STANDARD:
                return standardRepository;
            default:
                throw new RuntimeException("The validated league could not be matched with a StatsRepository");
        }
    }
}
