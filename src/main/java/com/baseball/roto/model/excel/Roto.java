package com.baseball.roto.model.excel;

import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;
import lombok.Data;

@Data
@Row(colsOrder = {"rank", "name", "total", "hitting", "pitching", " ", "total_change", "hitting_change", "pitching_change"})
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
    @Column(name = "total_change")
    private float totalChange = .11f;
    @Column(name = "hitting_change")
    private float hittingChange;
    @Column(name = "pitching_change")
    private float pitchingChange;
    @Column(name = " ")
    private final String __ = null;

    public Roto() {}

}
