package com.baseball.roto.mapper;

import com.baseball.roto.model.excel.Hitting;
import com.baseball.roto.model.excel.Pitching;
import com.baseball.roto.model.excel.Roto;
import com.baseball.roto.model.Stats;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatsMapper {
    @Mapping(target = "name", source = "hitting.name")
    Stats toStats(Hitting hitting, Pitching pitching);

    @Mapping(target = "name", source = "hitting.name")
    Stats toStats(Hitting hitting, Pitching pitching, int week);

    @Mapping(target = "name", source = "hitting.name")
    Stats toStats(Hitting hitting, Pitching pitching, int week, Roto roto);

    Roto toRoto(Stats source);

    List<Roto> toRotoList(List<Stats> source);

}
