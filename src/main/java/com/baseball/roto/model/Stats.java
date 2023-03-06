package com.baseball.roto.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@IdClass(StatsId.class)
@Data
public class Stats {
    @Id
    private int week = 0;
    @Id
    private String name;
    @Id
    private String league;
//    @Id
//    private int year;

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

    private float total;
    private float hitting;
    private float pitching;


    public Map<String, List<Float>> gatherHittingStats(){
        Map<String, List<Float>> map = new HashMap<>();
        List<Float> stats = new ArrayList<>();
        stats.add((float) runs);
        stats.add((float) homeRuns);
        stats.add((float) rbis);
        stats.add((float) sbs);
        stats.add(avg);
        stats.add(ops);
        map.put(name, stats);
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
        map.put(name, stats);
        return map;
    }

    public void determineTotals(Map<String, List<Float>> hittingStats, Map<String, List<Float>> pitchingStats) {
        this.hitting = sumTotal(hittingStats);
        this.pitching = sumTotal(pitchingStats);
        this.total = hitting + pitching;
    }

    private float sumTotal(Map<String, List<Float>> stats) {
        return (float) stats.get(name).stream().mapToDouble(stat -> stat).sum();
    }
}
