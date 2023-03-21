package com.baseball.roto.model;

import com.baseball.roto.mapper.RotoStatsMapper;
import com.baseball.roto.model.excel.Hitting;
import com.baseball.roto.model.excel.Pitching;
import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Builder
@Getter
public class RawStats {
    private List<Hitting> hittingList;
    private List<Pitching> pitchingList;

    public List<Stats> convertToStatsList(RotoStatsMapper rotoStatsMapper, int week, League league) {
        orderListsByName();
        return IntStream.range(0, league.getSize())
            .mapToObj(i -> rotoStatsMapper.toStats(hittingList.get(i), pitchingList.get(i), week, league.getName()))
            .collect(Collectors.toList());
    }

    public void orderListsByName(){
        hittingList.sort(Comparator.comparing(Hitting::getName));
        pitchingList.sort(Comparator.comparing(Pitching::getName));
    }

}
