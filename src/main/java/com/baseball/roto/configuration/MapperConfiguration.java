package com.baseball.roto.configuration;

import com.baseball.roto.exception.BadInput;
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
        if (league.equals(League.CHAMPIONS)) {
            return Mappers.getMapper(ChampStatsMapper.class);
        } else if (league.equals(League.PSD)) {
            return Mappers.getMapper(PsdStatsMapper.class);
        } else if (league.equals(League.STANDARD)) {
            return Mappers.getMapper(StandardStatsMapper.class);
        }
        throw new BadInput("The given league is not valid");
    }
}
