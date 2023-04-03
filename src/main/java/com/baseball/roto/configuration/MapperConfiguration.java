package com.baseball.roto.configuration;

import com.baseball.roto.mapper.ChampStatsMapper;
import com.baseball.roto.mapper.PsdStatsMapper;
import com.baseball.roto.mapper.StandardStatsMapper;
import com.baseball.roto.mapper.StatsMapper;
import com.baseball.roto.model.League;
import com.baseball.roto.model.entity.Stats;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    public StatsMapper<? extends Stats> statsMapper(League league) {
        switch (league) {
            case CHAMPIONS:
                return Mappers.getMapper(ChampStatsMapper.class);
            case PSD:
                return Mappers.getMapper(PsdStatsMapper.class);
            case STANDARD:
                return Mappers.getMapper(StandardStatsMapper.class);
            default:
                throw new RuntimeException("The validated league could not be matched with a StatsMapper");
        }
    }
}
