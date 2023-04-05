package com.baseball.roto.mapper;

import com.baseball.roto.model.entity.Stats;
import com.baseball.roto.model.excel.Hitting;
import com.baseball.roto.model.excel.Pitching;
import org.mapstruct.Mapping;

public interface StatsMapper<T extends Stats> {
    @Mapping(target = "name", source = "hitting.name")
    T toStats(Hitting hitting, Pitching pitching, int week);
}
