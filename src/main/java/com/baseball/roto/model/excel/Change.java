package com.baseball.roto.model.excel;

import com.baseball.roto.model.Stats;
import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;
import lombok.Data;

@Data
@Row(colsOrder = { " ", "total change", "hitting change", "pitching change"})
public class Change {
    @Column(name = "total change")
    private float totalChange = .11f;
    @Column(name = "hitting change")
    private float hittingChange;
    @Column(name = "pitching change")
    private float pitchingChange;
    @Column(name = " ")
    private final String space = null;

    public Change(Stats newStats, Stats oldStats) {
        totalChange = newStats.getTotal() - oldStats.getTotal();
        hittingChange = newStats.getHitting() - oldStats.getHitting();
        pitchingChange = newStats.getHitting() - oldStats.getPitching();
    }
}
