package com.baseball.roto.model.entity;

import com.ebay.xcelite.annotations.Column;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
public class BasketballStats extends Stats {
    @Column(name="FG%")
    private int FG;
    @Column(name="FT%")
    private int FT;
    @Column(name="3PTM")
    private int threes;
    @Column(name="PTS")
    private int points;
    @Column(name="REB")
    private int rebounds;
    @Column(name="AST")
    private int assists;
    @Column(name="ST")
    private int steals;
    @Column(name="BLK")
    private int blocks;
    @Column(name="TO")
    private float turnovers;


    @Override
    public Map<String, List<Float>> gatherHittingStats() {
        Map<String, List<Float>> map = new HashMap<>();
        List<Float> stats = new ArrayList<>();
        stats.add((float) FG);
        stats.add((float) FT);
        stats.add((float) threes);
        stats.add((float) points);
        stats.add((float) rebounds);
        stats.add((float) assists);
        stats.add((float) steals);
        stats.add((float) blocks);
        stats.add(turnovers);
        map.put(getName(), stats);
        return map;
    }

    @Override
    public Map<String, List<Float>> gatherPitchingStats() {
        return Map.of();
    }

}