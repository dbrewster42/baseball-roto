package com.baseball.roto.service;

import com.baseball.roto.exception.BadInput;
import com.baseball.roto.model.League;
import com.baseball.roto.model.LeagueStats;
import com.baseball.roto.model.Stats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StatsSubtraction {
    private final LeagueStats currentStats;
    private final LeagueStats previousStats;
    private final League league;
    private final float weight;
    private boolean isHitting;

    public static LeagueStats getRecentLeagueStats(List<Stats> currentStats, List<Stats> previousStats, League league, float weight) {
        StatsSubtraction statsSubtraction = new StatsSubtraction(currentStats, previousStats, league, weight);
        return statsSubtraction.getRecentLeagueStats();
    }

    private StatsSubtraction(List<Stats> currentStats, List<Stats> previousStats, League league, float weight) {
        this.currentStats = new LeagueStats(currentStats);
        this.previousStats = new LeagueStats(previousStats);
        this.weight = weight;
        this.league = league;
        this.isHitting = true;
    }

    private LeagueStats getRecentLeagueStats() {
        currentStats.setHittingStats(subtractStatLists(currentStats.getHittingStats(), previousStats.getHittingStats()));
        isHitting = false;
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
            throw new BadInput("There are multiple unmatched player names so recent stats cannot be calculated");
        }

        return currentStats;
    }
    private void subtractPlayersStats(Entry<String, List<Float>> playersStats, List<Float> playersOldStats) {
        int counterColumns = isHitting ? league.getHitCounterCol() : league.getPitchCounterCol();
        for (int i = 0; i < counterColumns; i++) {
            playersStats.getValue().set(i, playersStats.getValue().get(i) - playersOldStats.get(i));
        }
        for (int i = counterColumns; i < league.getStatColumns(); i++) {
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
