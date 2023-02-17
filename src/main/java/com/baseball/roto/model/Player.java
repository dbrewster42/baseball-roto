package com.baseball.roto.model;

import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;
import lombok.Data;

import java.util.List;

@Data
@Row(colsOrder = {"rank", "name", "total", "hitting", "pitching", "space", "total_change", "hitting_change", "pitching_change"})
public class Player {
    @Column
    private double rank;
    @Column
    private String name;
    @Column
    private double total;
    @Column
    private double hitting;
    @Column
    private double pitching;
    @Column
    private double total_change = .11;
    @Column
    private double hitting_change;
    @Column
    private double pitching_change;
    @Column
    private final String space = null;

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
