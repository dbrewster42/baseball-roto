package com.baseball.roto.mapper;

import com.baseball.roto.model.Hitting;
import com.baseball.roto.model.Pitching;
import com.baseball.roto.model.Stats;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StatsMapper {
//    @Mapping(target = "week", source = "week")
//    @Mapping(target = "runs", source = "hitting.runs")
//    @Mapping(target = "rbis", source = "hitting.rbis")
    @Mapping(target = "name", source = "hitting.name")

    Stats toStats(Hitting hitting, Pitching pitching, int week);

//    @AfterMapping
//    default void mapWeek(@MappingTarget Stats stats, @Value("${week}") int week) {
//        stats.setWeek(week);
//    }
}
