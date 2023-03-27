package com.baseball.roto.model;

import com.baseball.roto.mapper.RotoStatsMapper;
import com.baseball.roto.model.entity.Stats;
import com.baseball.roto.model.excel.Hitting;
import com.baseball.roto.model.excel.Pitching;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Builder
@Getter
public class RawStats {
    private List<Hitting> hittingList;
    private List<Pitching> pitchingList;

    public List<Stats> convertToStatsList(RotoStatsMapper rotoStatsMapper, int week, League league) {
        orderListsByName();
        List<Stats> statsList = new ArrayList<>();
        for (int i = 0; i < league.getSize(); i++) {
            if (league.equals(League.CHAMPIONS)) {
                statsList.add(rotoStatsMapper.toChampStats(hittingList.get(i), pitchingList.get(i), week, league.getName()));
            } else if (league.equals(League.PSD)) {
                statsList.add(rotoStatsMapper.toPsdStats(hittingList.get(i), pitchingList.get(i), week, league.getName()));
            }
        }
        return statsList;
    }

    public void orderListsByName(){
        hittingList.sort(Comparator.comparing(Hitting::getName));
        pitchingList.sort(Comparator.comparing(Pitching::getName));
    }

}
