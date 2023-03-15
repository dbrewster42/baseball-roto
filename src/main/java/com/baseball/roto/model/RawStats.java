package com.baseball.roto.model;

import com.baseball.roto.mapper.StatsMapper;
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

    public int getNumberOfPlayers() {
        int size = hittingList.size();
        if (size != pitchingList.size()) {
            throw new RuntimeException("Stats are mismatched");
        }
        return size;
    }

    public List<Stats> convertToStatsList(StatsMapper statsMapper, int week, String league) {
        orderListsByName();
        return IntStream.range(0, getNumberOfPlayers())
            .mapToObj(i -> statsMapper.toStats(hittingList.get(i), pitchingList.get(i), week, league))
            .collect(Collectors.toList());
    }

    public void orderListsByName(){
        hittingList.sort(Comparator.comparing(Hitting::getName));
        pitchingList.sort(Comparator.comparing(Pitching::getName));
    }

}
