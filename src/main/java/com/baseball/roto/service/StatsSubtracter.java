package com.baseball.roto.service;

import com.baseball.roto.model.LeagueSettings;
import com.baseball.roto.model.Stats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import static com.baseball.roto.mapper.StatFieldsMapper.convertStatFieldsToMap;


@Service
@Slf4j
public class StatsSubtracter {
    private final LeagueSettings league;
    private final RotoCalculator rotoCalculator;

    public StatsSubtracter(LeagueSettings league, RotoCalculator rotoCalculator) {
        this.league = league;
        this.rotoCalculator = rotoCalculator;
    }

    public List<Stats> calculateRecentStats(List<Stats> statsList, List<Stats> lastMonthsStats, int week, int includedWeeks) {
        float weight = (week - includedWeeks) / (float) includedWeeks;
        Map<String, List<Float>> hittingStats = subtractStatLists(statsList, lastMonthsStats, Stats::gatherHittingStats, weight);
        Map<String, List<Float>> pitchingStats = subtractStatLists(statsList, lastMonthsStats, Stats::gatherPitchingStats, weight);
        return rotoCalculator.calculateRotoPoints(statsList, hittingStats, pitchingStats);
    }
    private Map<String, List<Float>> subtractStatLists(List<Stats> statsList, List<Stats> oldStatsList, Function<Stats, Map<String, List<Float>>> getter, float weight) {
        return subtractStatLists(convertStatFieldsToMap(statsList, getter), convertStatFieldsToMap(oldStatsList, getter), weight);
    }
    private Map<String, List<Float>> subtractStatLists(Map<String, List<Float>> currentStats, Map<String, List<Float>> oldStats, float weight) {
        List<Entry<String, List<Float>>> unmatchedStats = new ArrayList<>();
        for (Entry<String, List<Float>> playersStats : currentStats.entrySet()) {
            oldStats.entrySet().stream()
                .filter(entry -> entry.getKey().equals(playersStats.getKey()))
                .map(Entry::getValue)
                .findAny()
                .ifPresentOrElse(
                    playersOldStats -> subtractPlayersStats(playersStats, playersOldStats, weight),
                    () -> unmatchedStats.add(playersStats)
                );
        }
        if (unmatchedStats.size() == 1) {
            oldStats.entrySet().stream()
                .filter(lw -> currentStats.entrySet().stream().noneMatch(stats -> lw.getKey().equals(stats.getKey())))
                .findAny()
                .ifPresent(lw ->  subtractPlayersStats(unmatchedStats.get(0), lw.getValue(), weight));
        } else if (!unmatchedStats.isEmpty()) {
            throw new RuntimeException("There are multiple unmatched players so recent stats cannot be calculated");
        }
        currentStats.forEach((k, v) -> log.info(k + " - " + v));

        return currentStats;
    }
    private void subtractPlayersStats(Entry<String, List<Float>> playersStats, List<Float> playersOldStats, float weight) {
        for (int i = 0; i < league.getHitCounterCol(); i++) { //todo will not work for PSD league
            playersStats.getValue().set(i, playersStats.getValue().get(i) - playersOldStats.get(i));
        }
        for (int i = league.getHitCounterCol(); i < league.getStatColumns(); i++) {
            playersStats.getValue().set(i, calculateAveragedValues(weight, playersOldStats.get(i), playersStats.getValue().get(i)));
        }
    }

    private float calculateAveragedValues(float weight, float oldValue, float newValue) {
        float diff = roundToThousandth(newValue - oldValue);
        return roundToThousandth(newValue + (diff * weight));
    }
    private float roundToThousandth(float value) { //stats are only accurate to 3 decimal places
        return Math.round(value * 1000) / 1000f;
    }
}
