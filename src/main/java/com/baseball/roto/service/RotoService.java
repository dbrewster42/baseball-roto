package com.baseball.roto.service;

import com.baseball.roto.mapper.StatsMapper;
import com.baseball.roto.model.Hitting;
import com.baseball.roto.model.Pitching;
import com.baseball.roto.model.Roto;
import com.baseball.roto.model.Stats;
import com.baseball.roto.repository.StatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RotoService {
    private final StatsRepository repository;
    private final StatsMapper statsMapper;
    private final ChangeService changeService;

    public RotoService(StatsRepository repository, StatsMapper statsMapper, ChangeService changeService) {
        this.repository = repository;
        this.statsMapper = statsMapper;
        this.changeService = changeService;
    }

    public List<Roto> calculateRoto(Collection<Hitting> hitting, Collection<Pitching> pitching){
        int week = (int) (repository.count() / 14) + 1;
        List<Stats> statsList = hitting.stream()
            .map(hit -> statsMapper.toStats(hit, matchStats(pitching, hit.getName(), Pitching::getName), week))
            .collect(Collectors.toList());
        repository.saveAll(calculateStats(statsList));

        List<Roto> rotos = includeRank(statsList.stream().map(statsMapper::toRoto).collect(Collectors.toList()));
        List<Stats> lastWeekStats = repository.findAllByWeek(week - 1);
        return changeService.calculateChanges(lastWeekStats, rotos);
    }

    private <T> T matchStats(Collection<T> collection, String name, Function<T, String> getter){
        return collection.stream()
            .filter(pitch -> getter.apply(pitch).equals(name))
            .findAny().orElseThrow(() -> new RuntimeException("player not found"));
    }

    private List<Stats> calculateStats(List<Stats> statsList) {
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

    protected void rankColumn(Map<String, List<Float>> stats, int columnNumber, boolean isReversed){
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
        double previous = -1.1;
        for (double singleStat : statColumn){
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
                    if (each.get(columnNumber).intValue()  == ties.get(i)){
                        each.set(columnNumber,(each.get(columnNumber) + 0.5f));
                    }
                }
            }
        }
    }

    //TODO more testing
    private List<Roto> includeRank(List<Roto> rotos){
        for (int i = 0; i < rotos.size(); i++){
            float start = i + 1;
            int ties = 0;
            while (i + ties + 1 < rotos.size() && rotos.get(i).getTotal() == rotos.get(i + 1 + ties).getTotal()){
                start += .5;
                ties++;
            }
            rotos.get(i).setRank(start);
            while (ties > 0){
                i++;
                rotos.get(i).setRank(start);
                ties--;
            }
        }
        return rotos;
    }
}
