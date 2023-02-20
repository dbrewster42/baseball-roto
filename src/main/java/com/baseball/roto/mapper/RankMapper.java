package com.baseball.roto.mapper;

import com.baseball.roto.model.Rank;
import com.baseball.roto.model.Stats;
import org.mapstruct.Mapper;

@Mapper
public interface RankMapper {
    Rank toRank(Stats stats);
}
