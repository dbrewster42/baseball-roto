package com.baseball.roto.model.entity;

import com.ebay.xcelite.annotations.Column;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.MappedSuperclass;
import java.util.List;
import java.util.Map;

@MappedSuperclass
@IdClass(StatsId.class)
@Data
public abstract class Stats {

    @Id
    private int week;
    @Column(name="Team Name")
    @Id
    private String name;

    private float total;
    private float hitting;
    private float pitching;

    public abstract Map<String, List<Float>> gatherHittingStats();
    public abstract Map<String, List<Float>> gatherPitchingStats();

    public void determineTotals(Map<String, List<Float>> hittingStats, Map<String, List<Float>> pitchingStats) {
        this.hitting = sumTotal(hittingStats);
        this.pitching = sumTotal(pitchingStats);
        this.total = hitting + pitching;
    }

    private float sumTotal(Map<String, List<Float>> stats) {
        return (float) stats.get(name).stream().mapToDouble(stat -> stat).sum();
    }
}
