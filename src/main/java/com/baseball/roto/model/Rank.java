package com.baseball.roto.model;

import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;

@Row(colsOrder = { "hitting_rank", "name", "hitting", "space", "pitching_rank", "name_", "pitching" })
public class Rank {
    @Column
    public double hitting_rank;
    @Column
    public double pitching_rank;
    @Column
    public String name;
    @Column
    public double hitting;
    @Column
    public String name_;
    @Column
    public double pitching;
    @Column
    public final String space = null;

    public Rank(double rank, Player hitter, Player pitcher) {
        this.hitting_rank = rank;
        this.pitching_rank = rank;
        this.name = hitter.name;
        this.hitting = hitter.hitting;
        this.name_ = pitcher.name;
        this.pitching = pitcher.pitching;
    }
}
