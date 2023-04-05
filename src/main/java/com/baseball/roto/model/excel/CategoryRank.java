package com.baseball.roto.model.excel;

import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;

@Row(colsOrder = { "rank", "name", "hitting", " ", "rank ", "name ", "pitching" })
public class CategoryRank {
    @Column(name = "rank")
    public float hittingRank;
    @Column(name = "name")
    public String hittingName;
    @Column
    public float hitting;
    @Column(name = "rank ")
    public float pitchingRank;
    @Column(name = "name ")
    public String pitchingName;
    @Column
    public float pitching;
    @Column(name = " ")
    public final String __ = null;

    public CategoryRank(Roto hitter) {
        this.hittingRank = hitter.getRank();
        this.hittingName = hitter.getName();
        this.hitting = hitter.getHitting();
    }
    public void setPitchingCategories(Roto pitcher) {
        this.pitchingRank = pitcher.getRank();
        this.pitchingName = pitcher.getName();
        this.pitching = pitcher.getPitching();
    }
}
