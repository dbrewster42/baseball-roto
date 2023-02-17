package com.baseball.roto.model;

import com.ebay.xcelite.annotations.Column;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Pitching implements Stats {
    @Column(name="N")
    private String name;
    @Column(name="W")
    private int wins;
    @Column(name="K")
    private int strikeouts;
    @Column(name="ERA")
    private double era;
    @Column(name="WHIP")
    private double whip;
    @Column(name="QS")
    private int qualityStarts;
    @Column(name="NSV")
    private int netSaves;

    private List<Double> stats;

    @Override
    public List<Double> gatherStats(){
        stats = new ArrayList<>();
        stats.add((double) wins);
        stats.add((double) strikeouts);
        stats.add(era);
        stats.add(whip);
        stats.add((double) qualityStarts);
        stats.add((double) netSaves);
        return stats;
    }
}
