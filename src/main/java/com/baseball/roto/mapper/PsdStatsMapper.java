package com.baseball.roto.mapper;

import com.baseball.roto.model.entity.PsdStats;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PsdStatsMapper extends StatsMapper<PsdStats> { }
