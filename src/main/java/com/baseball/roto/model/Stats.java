package com.baseball.roto.model;

import com.ebay.xcelite.annotations.Column;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(StatsId.class)
@Data
public class Stats {
    @Id
    private int week;
    @Id
    private String name;

    private Hitting hitting;
    private Pitching pitching;
    private RotoEntity roto;

//    private int runs;
//    private int homeRuns;
//    private int rbis;
//    private int sbs;
//    private double avg;
//    private double ops;
//
//    private int wins;
//    private int strikeouts;
//    private double era;
//    private double whip;
//    private int qualityStarts;
//    private int netSaves;

    public Stats() {
    }

    public Stats(Hitting hitting, Pitching pitching) {
        if (!hitting.getName().equals(pitching.getName())) { throw new RuntimeException("Mistake!"); }
        this.name = hitting.getName();
        this.week = 1;
        this.hitting = hitting;
        this.pitching = pitching;
    }
}
