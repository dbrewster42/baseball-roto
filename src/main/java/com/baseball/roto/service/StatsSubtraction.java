package com.baseball.roto.service;

import com.baseball.roto.exception.BadInput;
import com.baseball.roto.model.League;
import com.baseball.roto.model.LeagueStats;
import com.baseball.roto.model.entity.Stats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StatsSubtraction {
    private final LeagueStats currentStats;
    private final LeagueStats previousStats;
    private final League league;
    private final float weight;

    public static LeagueStats getRecentLeagueStats(List<Stats> currentStats, List<Stats> excludedStats, League league, float weight) {
        StatsSubtraction statsSubtraction = new StatsSubtraction(currentStats, excludedStats, league, weight);
        return statsSubtraction.getRecentLeagueStats();
    }

    private StatsSubtraction(List<Stats> currentStats, List<Stats> previousStats, League league, float weight) {
        this.currentStats = new LeagueStats(currentStats);
        this.previousStats = new LeagueStats(previousStats);
        this.weight = weight;
        this.league = league;
    }

    private LeagueStats getRecentLeagueStats() {
        currentStats.setHittingStats(subtractStatLists(currentStats.getHittingStats(), previousStats.getHittingStats(), league.getHitCountingStats()));
        currentStats.setPitchingStats(subtractStatLists(currentStats.getPitchingStats(), previousStats.getPitchingStats(), league.getPitchCountingStats()));
        return currentStats;
    }

    private Map<String, List<Float>> subtractStatLists(Map<String, List<Float>> currentStats, Map<String, List<Float>> oldStats, int countingColumns) {
        List<Entry<String, List<Float>>> unmatchedStats = new ArrayList<>();
        for (Entry<String, List<Float>> playersStats : currentStats.entrySet()) {
            oldStats.entrySet().stream()
                .filter(entry -> entry.getKey().equals(playersStats.getKey()))
                .map(Entry::getValue)
                .findAny()
                .ifPresentOrElse(
                    playersOldStats -> subtractPlayersStats(playersStats, playersOldStats, countingColumns),
                    () -> unmatchedStats.add(playersStats)
                );
        }
        if (unmatchedStats.size() == 1) {
            oldStats.entrySet().stream()
                .filter(lw -> currentStats.entrySet().stream().noneMatch(stats -> lw.getKey().equals(stats.getKey())))
                .findAny()
                .ifPresent(lw ->  subtractPlayersStats(unmatchedStats.get(0), lw.getValue(), countingColumns));
        } else if (!unmatchedStats.isEmpty()) {
            throw new BadInput("There are multiple unmatched player names so recent stats cannot be calculated");
        }
        currentStats.forEach((key, value) -> System.out.println(key + " - " + value));
        return currentStats;
    }
    private void subtractPlayersStats(Entry<String, List<Float>> playersStats, List<Float> playersOldStats, int countingColumns) {
        for (int i = 0; i < countingColumns; i++) {
            playersStats.getValue().set(i, playersStats.getValue().get(i) - playersOldStats.get(i));
        }
        for (int i = countingColumns; i < league.getStatColumns(); i++) {
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
