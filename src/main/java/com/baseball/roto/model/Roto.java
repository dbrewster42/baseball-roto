package com.baseball.roto.model;

import com.ebay.xcelite.annotations.AnyColumn;
import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;
import lombok.Data;

import java.util.Map;

@Data
@Row(colsOrder = {"rank", "name", "total", "hitting", "pitching", " ", "total_change", "hitting_change", "pitching_change"})
public class Roto {
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
    @Column(name = "total_change")
    private double totalChange = .11;
    @Column(name = "hitting_change")
    private double hittingChange;
    @Column(name = "pitching_change")
    private double pitchingChange;
    @Column(name = " ")
    private final String __ = null;

    public Roto() {}

}
