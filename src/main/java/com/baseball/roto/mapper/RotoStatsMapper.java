package com.baseball.roto.mapper;

import com.baseball.roto.model.Stats;
import com.baseball.roto.model.excel.Hitting;
import com.baseball.roto.model.excel.Pitching;
import com.baseball.roto.model.excel.Roto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RotoStatsMapper {

    @Mapping(target = "name", source = "hitting.name")
    Stats toStats(Hitting hitting, Pitching pitching, int week, String league);

//    List<Stats> toStatsList(List<Hitting> hitting, List<Pitching> pitching, int week);

    Roto toRoto(Stats source);

    List<Roto> toRotoList(List<Stats> source);

}
