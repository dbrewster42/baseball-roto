package com.baseball.roto.service;

import com.baseball.roto.model.Stats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class StatCalculationService {

    public List<Stats> calculateStats(List<Stats> statsList) {
        Map<String, List<Float>> hittingStats = combineStatLists(statsList, Stats::gatherHittingStats);
        Map<String, List<Float>> pitchingStats = combineStatLists(statsList, Stats::gatherPitchingStats);
        for (int i = 0; i < 6; i++){
            rankColumn(hittingStats, i, false);
            rankColumn(pitchingStats, i, i > 3);
        }
        for (Stats stats : statsList) {
            stats.setHitting((float) hittingStats.get(stats.getName()).stream().mapToDouble(v -> v).sum());
            stats.setPitching((float) pitchingStats.get(stats.getName()).stream().mapToDouble(v -> v).sum());
            stats.setTotal(stats.getHitting() + stats.getPitching());
        }
        statsList.sort((o1, o2) -> Float.compare(o2.getTotal(), o1.getTotal()));
        log.info("stats calculated and sorted");
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
}
