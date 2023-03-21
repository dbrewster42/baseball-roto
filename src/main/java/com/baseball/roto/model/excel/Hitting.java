package com.baseball.roto.model.excel;

import com.ebay.xcelite.annotations.Column;
import lombok.Data;

@Data
public class Hitting {
    @Column(name="N")
    private String name;
    @Column(name="R")
    private int runs;
    @Column(name="HR")
    private int homeRuns;
    @Column(name="RBI")
    private int rbis;
    @Column(name="SB")
    private int sbs;
    @Column(name="AVG")
    private float avg;
    @Column(name="OPS")
    private float ops;

}
