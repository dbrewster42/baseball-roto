package com.baseball.roto.model.excel;

import com.ebay.xcelite.annotations.Column;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Hitting {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

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
