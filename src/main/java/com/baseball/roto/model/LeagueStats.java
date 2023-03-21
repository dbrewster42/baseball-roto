package com.baseball.roto.model;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Data
public class LeagueStats {
    private final List<Stats> statsList;
    private Map<String, List<Float>> hittingStats;
    private Map<String, List<Float>> pitchingStats;

    public LeagueStats(List<Stats> statsList) {
        this.statsList = statsList;
        this.hittingStats = convertStatFieldsToMap(Stats::gatherHittingStats);
        this.pitchingStats = convertStatFieldsToMap(Stats::gatherPitchingStats);
    }

    public void determineTotals() {
        statsList.forEach(stats -> stats.determineTotals(hittingStats, pitchingStats));
    }

    private Map<String, List<Float>> convertStatFieldsToMap(Function<Stats, Map<String, List<Float>>> statsGetter) {
        return statsList.stream()
            .map(statsGetter)
            .reduce((left, right) -> {
                left.putAll(right);
                return left;
            }).orElseThrow(() -> new RuntimeException("error combining stat lists"));
    }
}
