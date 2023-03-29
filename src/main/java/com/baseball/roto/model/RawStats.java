package com.baseball.roto.model;

import com.baseball.roto.mapper.RotoStatsMapper;
import com.baseball.roto.model.entity.ChampStats;
import com.baseball.roto.model.entity.PsdStats;
import com.baseball.roto.model.entity.Stats;
import com.baseball.roto.model.excel.Hitting;
import com.baseball.roto.model.excel.Pitching;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Builder
@Getter
public class RawStats {
    private List<Hitting> hittingList;
    private List<Pitching> pitchingList;

//    public List<ChampStats> convertToStatsList(RotoStatsMapper rotoStatsMapper, int week, League league) {
//        orderListsByName();
//        List<ChampStats> statsList = new ArrayList<>();
//        for (int i = 0; i < league.getSize(); i++) {
//            statsList.add(rotoStatsMapper.toChampStats(hittingList.get(i), pitchingList.get(i), week));
//        }
//        return statsList;
//    }
    public <T> List<T> convertToStatsList(RotoStatsMapper rotoStatsMapper, int week, League league) {
        orderListsByName();
        List<T> statsList = new ArrayList<>();
//        for (int i = 0; i < league.getSize(); i++) {
//            statsList.add(rotoStatsMapper.toPsdStats(hittingList.get(i), pitchingList.get(i), week));
//        }
        for (int i = 0; i < league.getSize(); i++) {
            if (league.equals(League.CHAMPIONS)) {
                statsList.add((T) rotoStatsMapper.toChampStats(hittingList.get(i), pitchingList.get(i), week));
            } else if (league.equals(League.PSD)) {
                statsList.add((T) rotoStatsMapper.toPsdStats(hittingList.get(i), pitchingList.get(i), week));
            }
        }
        return statsList;
    }

    public void orderListsByName(){
        hittingList.sort(Comparator.comparing(Hitting::getName));
        pitchingList.sort(Comparator.comparing(Pitching::getName));
    }

}
