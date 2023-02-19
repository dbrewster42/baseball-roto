package com.baseball.roto.model;

import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;

@Row(colsOrder = { "rank", "name", "hitting", "__", "team", "pitching" })
public class Rank {
    @Column
    public double rank;
    @Column
    public String name;
    @Column
    public double hitting;
    @Column
    public String team;
    @Column
    public double pitching;
    @Column
    public final String __ = null;

    public Rank(double rank, Roto hitter, Roto pitcher) {
        this.rank = rank;
        this.name = hitter.getName();
        this.hitting = hitter.getHitting();
        this.team = pitcher.getName();
        this.pitching = pitcher.getPitching();
    }
}
