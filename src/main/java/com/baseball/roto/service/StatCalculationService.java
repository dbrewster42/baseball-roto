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
            if (previous == singleStat){
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

    //TODO more testing
    private void applyTies(Map<String, List<Float>> stats, int columnNumber, List<Integer> ties){
        for (int i = 0; i < ties.size(); i++){
            if (i < ties.size() - 1 && ties.get(i) == ties.get(i + 1)){
                int multiTie = ties.get(i);
                float specialModifier = .5F;
                while (i < ties.size() - 1 && ties.get(i) == ties.get(i + 1)){
                    specialModifier += .5;
                    i++;
                }
                for (List<Float> each : stats.values()) {
                    if (each.get(columnNumber).intValue() == multiTie) {
                        each.set(columnNumber, each.get(columnNumber) + specialModifier);
                    }
                }
            }
            else {
                for (List<Float> each : stats.values()){
                    if (each.get(columnNumber).intValue()  == ties.get(i)){ //todo ??
                        each.set(columnNumber,(each.get(columnNumber) + 0.5f));
                    }
                }
            }
        }
    }

}
