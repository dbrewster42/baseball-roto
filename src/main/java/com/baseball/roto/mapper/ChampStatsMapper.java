package com.baseball.roto.mapper;

import com.baseball.roto.model.entity.ChampStats;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChampStatsMapper extends StatsMapper<ChampStats> { }
