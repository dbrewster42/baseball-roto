package com.baseball.roto.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StatsSubtraction {
    private final LeagueStats currentStats;
    private final LeagueStats previousStats;
    private float weight;
    private int totalColumns;
    private int counterColumns;

    public StatsSubtraction(List<Stats> currentStatsList, List<Stats> previousStatsList) {
        this.currentStats = new LeagueStats(currentStatsList);
        this.previousStats = new LeagueStats(previousStatsList);
    }

    public LeagueStats getRecentLeagueStats(float weight, LeagueSettings leagueSettings) {
        this.weight = weight;
        totalColumns = leagueSettings.getStatColumns();
        counterColumns = leagueSettings.getHitCounterCol();
        currentStats.setHittingStats(subtractStatLists(currentStats.getHittingStats(), previousStats.getHittingStats()));
        counterColumns = leagueSettings.getPitchCounterCol();
        currentStats.setPitchingStats(subtractStatLists(currentStats.getPitchingStats(), previousStats.getPitchingStats()));
        return currentStats;
    }

    private Map<String, List<Float>> subtractStatLists(Map<String, List<Float>> currentStats, Map<String, List<Float>> oldStats) {
        List<Entry<String, List<Float>>> unmatchedStats = new ArrayList<>();
        for (Entry<String, List<Float>> playersStats : currentStats.entrySet()) {
            oldStats.entrySet().stream()
                .filter(entry -> entry.getKey().equals(playersStats.getKey()))
                .map(Entry::getValue)
                .findAny()
                .ifPresentOrElse(
                    playersOldStats -> subtractPlayersStats(playersStats, playersOldStats),
                    () -> unmatchedStats.add(playersStats)
                );
        }
        if (unmatchedStats.size() == 1) {
            oldStats.entrySet().stream()
                .filter(lw -> currentStats.entrySet().stream().noneMatch(stats -> lw.getKey().equals(stats.getKey())))
                .findAny()
                .ifPresent(lw ->  subtractPlayersStats(unmatchedStats.get(0), lw.getValue()));
        } else if (!unmatchedStats.isEmpty()) {
            throw new RuntimeException("There are multiple unmatched players so recent stats cannot be calculated");
        }
//        currentStats.forEach((k, v) -> log.info(k + " - " + v));

        return currentStats;
    }
    private void subtractPlayersStats(Entry<String, List<Float>> playersStats, List<Float> playersOldStats) {
        for (int i = 0; i < counterColumns; i++) {
            playersStats.getValue().set(i, playersStats.getValue().get(i) - playersOldStats.get(i));
        }
        for (int i = counterColumns; i < totalColumns; i++) {
            playersStats.getValue().set(i, calculateAveragedValues(playersStats.getValue().get(i), playersOldStats.get(i)));
        }
    }

    private float calculateAveragedValues(float newValue, float oldValue) {
        float diff = roundToThousandth(newValue - oldValue);
        return roundToThousandth(newValue + (diff * weight));
    }
    private float roundToThousandth(float value) { //stats are only accurate to 3 decimal places
        return Math.round(value * 1000) / 1000f;
    }
}
