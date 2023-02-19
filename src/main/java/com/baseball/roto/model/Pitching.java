package com.baseball.roto.model;

import com.ebay.xcelite.annotations.Column;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
public class Pitching {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(name="N")
    private String name;
    @Column(name="W")
    private int wins;
    @Column(name="K")
    private int strikeouts;
    @Column(name="ERA")
    private double era;
    @Column(name="WHIP")
    private double whip;
    @Column(name="QS")
    private int qualityStarts;
    @Column(name="NSV")
    private int netSaves;

    public Map<String, List<Double>> gatherStats(){
        Map<String, List<Double>> map = new HashMap<>();
        List<Double> stats = new ArrayList<>();
        stats.add((double) wins);
        stats.add((double) strikeouts);
        stats.add((double) qualityStarts);
        stats.add((double) netSaves);
        stats.add(era);
        stats.add(whip);
        map.put(name, stats);
        return map;
    }
}
