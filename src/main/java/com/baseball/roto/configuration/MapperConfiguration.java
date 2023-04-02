package com.baseball.roto.configuration;

import com.baseball.roto.exception.BadInput;
import com.baseball.roto.mapper.ChampStatsMapper;
import com.baseball.roto.mapper.PsdStatsMapper;
import com.baseball.roto.mapper.StatsMapper;
import com.baseball.roto.model.League;
import com.baseball.roto.model.entity.Stats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {
    @Autowired private ChampStatsMapper champStatsMapper;
    @Autowired private PsdStatsMapper psdStatsMapper;

    @Bean
    public StatsMapper<? extends Stats> statsMapper(League league) {
        if (league.equals(League.CHAMPIONS)) {
            return champStatsMapper;
        } else if (league.equals(League.PSD)) {
            return psdStatsMapper;
        }
        throw new BadInput("The given league is not valid");
    }
}
