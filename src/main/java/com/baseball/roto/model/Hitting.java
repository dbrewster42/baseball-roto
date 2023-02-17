package com.baseball.roto.model;

import com.ebay.xcelite.annotations.Column;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Hitting {
    @Column(name="N")
    private String name;
    @Column(name="R")
    private int runs;
    @Column(name="HR")
    private int homeRuns;
    @Column(name="RBI")
    private int rbis;
    @Column(name="SB")
    private int sbs;
    @Column(name="AVG")
    private double avg;
    @Column(name="OPS")
    private double ops;


    public Map<String, List<Double>> gatherStats(){
        Map<String, List<Double>> map = new HashMap<>();
        List<Double> stats = new ArrayList<>();
        stats.add((double) runs);
        stats.add((double) homeRuns);
        stats.add((double) rbis);
        stats.add((double) sbs);
        stats.add(avg);
        stats.add(ops);
        map.put(name, stats);
        return map;
    }
}
