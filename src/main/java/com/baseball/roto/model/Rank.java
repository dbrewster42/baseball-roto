package com.baseball.roto.model;

import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;

@Row(colsOrder = { "rank", "name", "hitting", " ", "name ", "pitching" })
public class Rank {
    @Column
    public float rank;
    @Column(name = "name")
    public String hittingName;
    @Column
    public float hitting;
    @Column(name = "name ")
    public String pitchingName;
    @Column
    public float pitching;
    @Column(name = " ")
    public final String __ = null;

    public Rank(float rank, Roto hitter, Roto pitcher) {
        this.rank = rank;
        this.hittingName = hitter.getName();
        this.hitting = hitter.getHitting();
        this.pitchingName = pitcher.getName();
        this.pitching = pitcher.getPitching();
    }
}
