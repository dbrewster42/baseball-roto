package com.baseball.roto.model.excel;

import com.baseball.roto.model.Stats;
import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;
import lombok.Data;

@Data
@Row(colsOrder = {"rank", "name", "total", "hitting", "pitching", " ", "total change", "hitting change", "pitching change"})
public class Roto {
    @Column
    private float rank;
    @Column
    private String name;
    @Column
    private float total;
    @Column
    private float hitting;
    @Column
    private float pitching;
    @Column(name = "total change")
    private float totalChange = .11f;
    @Column(name = "hitting change")
    private float hittingChange;
    @Column(name = "pitching change")
    private float pitchingChange;
    @Column(name = " ")
    private final String __ = null;

    public Roto() {}

    public void setChangesFromGiven(Stats oldStats) {
        totalChange = total - oldStats.getTotal();
        hittingChange = hitting - oldStats.getHitting();
        pitchingChange = pitchingChange - oldStats.getPitching();
    }

}
