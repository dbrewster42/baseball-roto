package com.baseball.roto.model;

import com.baseball.roto.model.excel.Hitting;
import com.baseball.roto.model.excel.Pitching;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

@Getter
public class RawStats {
    private final List<Hitting> hittingList;
    private final List<Pitching> pitchingList;

    public RawStats(List<Hitting> hittingList, List<Pitching> pitchingList) {
        this.hittingList = hittingList;
        this.pitchingList = pitchingList;
        orderListsByName();
    }

    public void orderListsByName(){
        hittingList.sort(Comparator.comparing(Hitting::getName));
        pitchingList.sort(Comparator.comparing(Pitching::getName));
    }
}
