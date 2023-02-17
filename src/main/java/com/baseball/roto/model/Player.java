package com.baseball.roto.model;

import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;

import java.util.List;

@Row(colsOrder = {"rank", "name", "total", "hitting", "pitching", "space", "total_change", "hitting_change", "pitching_change"})
public class Player {
    @Column
    public double rank;
    @Column
    public String name;
    @Column
    public double total;
    @Column
    public double hitting;
    @Column
    public double pitching;
    @Column
    public double total_change = .11;
    @Column
    public double hitting_change;
    @Column
    public double pitching_change;
    @Column
    public final String space = null;

    private List<Double> hittingStats;
    private List<Double> pitchingStats;

    public Player(){}

    public Player(String name, double hitting) {
        this.name = name;
        this.hitting = hitting;
    }
    public Player(String name, double hitting, double pitching) {
        this.name = name;
        this.hitting = hitting;
        this.pitching = pitching;
    }
}
