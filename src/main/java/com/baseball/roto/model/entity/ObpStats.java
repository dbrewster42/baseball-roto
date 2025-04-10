package com.baseball.roto.model.entity;

import com.ebay.xcelite.annotations.Column;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
public class ObpStats extends Stats {
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
    @Column(name="OBP")
    private float obp;

    @Column(name="W")
    private int wins;
    @Column(name="K")
    private int strikeouts;
    @Column(name="ERA")
    private float era;
    @Column(name="WHIP")
    private float whip;
    @Column(name="SV")
    private int saves;
    @Column(name="QS")
    private int qualityStarts;

    public Map<String, List<Float>> gatherHittingStats(){
        Map<String, List<Float>> map = new HashMap<>();
        List<Float> stats = new ArrayList<>();
        stats.add((float) runs);
        stats.add((float) homeRuns);
        stats.add((float) rbis);
        stats.add((float) sbs);
        stats.add(avg);
        stats.add(obp);
        map.put(getName(), stats);
        return map;
    }

    public Map<String, List<Float>> gatherPitchingStats(){
        Map<String, List<Float>> map = new HashMap<>();
        List<Float> stats = new ArrayList<>();
        stats.add((float) wins);
        stats.add((float) strikeouts);
        stats.add((float) saves);
        stats.add((float) qualityStarts);
        stats.add(era);
        stats.add(whip);
        map.put(getName(), stats);
        return map;
    }
}
