package com.baseball.roto.mapper;

import com.baseball.roto.model.Hitting;
import com.baseball.roto.model.Pitching;
import com.baseball.roto.model.Stats;
import org.mapstruct.Mapper;

@Mapper
public interface StatsMapper {
    Stats mapToStats(Hitting hitting, Pitching pitching);
}
