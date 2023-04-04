package com.baseball.roto.configuration;

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
        return Mappers.getMapper(league.getMapper());
    }
}
