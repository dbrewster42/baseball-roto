package com.baseball.roto.mapper;

import com.baseball.roto.model.entity.ChampStats;
import com.baseball.roto.model.entity.PsdStats;
import com.baseball.roto.model.entity.Stats;
import com.baseball.roto.model.excel.Hitting;
import com.baseball.roto.model.excel.Pitching;
import com.baseball.roto.model.excel.Roto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RotoStatsMapper {

    @Mapping(target = "name", source = "hitting.name")
    ChampStats toChampStats(Hitting hitting, Pitching pitching, int week);

    @Mapping(target = "name", source = "hitting.name")
    PsdStats toPsdStats(Hitting hitting, Pitching pitching, int week);

//    List<Stats> toStatsList(List<Hitting> hitting, List<Pitching> pitching, int week);

    Roto toRoto(Stats source);

    List<Roto> toRotoList(List<Stats> source);

}
