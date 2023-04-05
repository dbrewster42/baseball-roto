package com.baseball.roto.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
public class ChampStats extends Stats {
    private int runs;
    private int homeRuns;
    private int rbis;
    private int sbs;
    private float avg;
    private float ops;

    private int wins;
    private int strikeouts;
    private float era;
    private float whip;
    private int qualityStarts;
    private int netSaves;

    public Map<String, List<Float>> gatherHittingStats(){
        Map<String, List<Float>> map = new HashMap<>();
        List<Float> stats = new ArrayList<>();
        stats.add((float) runs);
        stats.add((float) homeRuns);
        stats.add((float) rbis);
        stats.add((float) sbs);
        stats.add(avg);
        stats.add(ops);
        map.put(getName(), stats);
        return map;
    }

    public Map<String, List<Float>> gatherPitchingStats(){
        Map<String, List<Float>> map = new HashMap<>();
        List<Float> stats = new ArrayList<>();
        stats.add((float) wins);
        stats.add((float) strikeouts);
        stats.add((float) qualityStarts);
        stats.add((float) netSaves);
        stats.add(era);
        stats.add(whip);
        map.put(getName(), stats);
        return map;
    }
}