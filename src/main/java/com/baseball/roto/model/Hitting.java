package com.baseball.roto.model;

import com.ebay.xcelite.annotations.Column;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
public class Hitting implements Stats {
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

    private List<Double> stats;

    @Override
    public List<Double> gatherStats(){
        stats = new ArrayList<>();
        stats.add((double) runs);
        stats.add((double) homeRuns);
        stats.add((double) rbis);
        stats.add((double) sbs);
        stats.add(avg);
        stats.add(ops);
        return stats;
    }
}
