package com.baseball.roto.service;

import com.baseball.roto.model.Stats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

@Service
@Slf4j
public class StatCalculationService {
    private static final int STAT_COLUMNS_SIZE = 6;
    private static final int COUNTING_STATS_SIZE = 4;

    public List<Stats> calculateStats(List<Stats> statsList) {
        Map<String, List<Float>> hittingStats = combineStatLists(statsList, Stats::gatherHittingStats);
        Map<String, List<Float>> pitchingStats = combineStatLists(statsList, Stats::gatherPitchingStats);
        return calculateStats(statsList, hittingStats, pitchingStats);
    }
    private List<Stats> calculateStats(List<Stats> statsList, Map<String, List<Float>> hittingStats, Map<String, List<Float>> pitchingStats) {
        for (int i = 0; i < STAT_COLUMNS_SIZE; i++){
            rankColumn(hittingStats, i, false);
            rankColumn(pitchingStats, i, i >= COUNTING_STATS_SIZE);
        }
        statsList.forEach(stats -> stats.determineTotals(hittingStats, pitchingStats));
        return statsList;
    }

    private Map<String, List<Float>> combineStatLists(List<Stats> stats, Function<Stats, Map<String, List<Float>>> getter) {
        return stats.stream()
            .map(getter)
            .reduce((left, right) -> {
                left.putAll(right);
                return left;
            }).orElseThrow(() -> new RuntimeException("error combining the gathered stats"));
    }

    private void rankColumn(Map<String, List<Float>> stats, int columnNumber, boolean isReversed){
        List<Float> statColumn = new ArrayList<>();
        for (List<Float> eachStatList : stats.values()){
            statColumn.add(eachStatList.get(columnNumber));
        }
        if (isReversed){
            statColumn.sort((o1, o2) -> Float.compare(o2, o1));
        } else {
            statColumn.sort(Float::compare);
        }
        List<Integer> ties = recordTies(statColumn);
        overwriteStatsWithRotoPoints(stats, columnNumber, statColumn);
        applyTies(stats, columnNumber, ties);
    }

    private List<Integer> recordTies(List<Float> statColumn) {
        List<Integer> ties = new ArrayList<>();
        float previous = -1.1f;
        for (float singleStat : statColumn){
            if (singleStat == previous){
                ties.add(statColumn.indexOf(singleStat) + 1);
            }
            previous = singleStat;
        }
        return ties;
    }

    private void overwriteStatsWithRotoPoints(Map<String, List<Float>> stats, int columnNumber, List<Float> statColumn) {
        for (List<Float> eachStatList : stats.values()){
            eachStatList.set(columnNumber, 1f + statColumn.indexOf(eachStatList.get(columnNumber)));
        }
    }

    private void applyTies(Map<String, List<Float>> stats, int columnNumber, List<Integer> tiedRanks){
        for (int i = 0; i < tiedRanks.size(); i++){
            float tieModifier = .5F;
            int tiedRank = tiedRanks.get(i);
            while (i < tiedRanks.size() - 1 && tiedRank == tiedRanks.get(i + 1)){
                tieModifier += .5;
                i++;
            }
            for (List<Float> statList : stats.values()){
                if (statList.get(columnNumber).intValue() == tiedRank){
                    statList.set(columnNumber, tiedRank + tieModifier);
                }
            }
        }
    }

    public List<Stats> subtractOldStats(List<Stats> statsList, List<Stats> lastMonthsStats, int week) {
        float weight = (week - 4) / 4f;
        Map<String, List<Float>> hittingStats = subtractStatLists(statsList, lastMonthsStats, Stats::gatherHittingStats, weight);
        Map<String, List<Float>> pitchingStats = subtractStatLists(statsList, lastMonthsStats, Stats::gatherPitchingStats, weight);
        return calculateStats(statsList, hittingStats, pitchingStats);
    }
    private Map<String, List<Float>> subtractStatLists(List<Stats> statsList, List<Stats> oldStatsList, Function<Stats, Map<String, List<Float>>> getter, float weight) {
        return subtractStatLists(combineStatLists(statsList, getter), combineStatLists(oldStatsList, getter), weight);
    }
    private Map<String, List<Float>> subtractStatLists(Map<String, List<Float>> currentStats, Map<String, List<Float>> oldStats, float weight) {
        for (Entry<String, List<Float>> playersStats : currentStats.entrySet()) {
            List<Float> playersOldStats = oldStats.entrySet().stream()
                .filter(entry -> entry.getKey().equals(playersStats.getKey()))
                .map(Entry::getValue)
                .findAny().orElseThrow();
            for (int i = 0; i < COUNTING_STATS_SIZE; i++) {
                playersStats.getValue().set(i, playersStats.getValue().get(i) - playersOldStats.get(i));
            }
            for (int i = COUNTING_STATS_SIZE; i < STAT_COLUMNS_SIZE; i++) {
                playersStats.getValue().set(i, calculateAveragedValues(weight, playersOldStats.get(i), playersStats.getValue().get(i)));
            }
        }
        return currentStats;
    }
    private float calculateAveragedValues(float weight, float oldValue, float newValue) {
        float diff = roundToThousandth(newValue - oldValue);
        return roundToThousandth(newValue + (diff * weight));
    }
    private float roundToThousandth(float value) { //stats are only accurate to 3 decimal places
        return Math.round(value * 1000) / 1000f;
    }
}
