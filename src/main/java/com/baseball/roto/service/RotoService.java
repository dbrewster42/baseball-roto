package com.baseball.roto.service;

import com.baseball.roto.exception.CalculationException;
import com.baseball.roto.model.Hitting;
import com.baseball.roto.model.Pitching;
import com.baseball.roto.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RotoService {

    public List<Player> calculateRotoScores(Collection<Hitting> hitting, Collection<Pitching> pitching){
        return applyRotoCalculations(calculatePitchingStats(pitching), calculateHittingStats(hitting));
    }

    private List<Player> applyRotoCalculations(Map<String, List<Double>> stats, List<Player> players) {
        players.forEach(player -> {
            player.setPitching(stats.get(player.getName()).stream().mapToDouble(v -> v).sum());
            player.setTotal(player.getHitting() + player.getPitching());
        });
        players.sort((o1, o2) -> Double.compare(o2.getTotal(), o1.getTotal()));
        calculateRank(players);
        return players;
    }

    private List<Player> calculateHittingStats(Collection<Hitting> hitting) {
        Map<String, List<Double>> allStats = hitting.stream()
            .map(Hitting::gatherStats)
            .reduce((left, right) -> {
                left.putAll(right);
                return left;
            }).orElseThrow(CalculationException::new);
        for (int i = 0; i < 6; i++){
            rankColumn(allStats, i, false);
        }
        return allStats.entrySet().stream()
            .map(player -> new Player(player.getKey(), player.getValue().stream().mapToDouble(v -> v).sum()))
            .collect(Collectors.toList());
    }
    private Map<String, List<Double>> calculatePitchingStats(Collection<Pitching> pitching) {
        Map<String, List<Double>> allStats = pitching.stream()
            .map(Pitching::gatherStats)
            .reduce((left, right) -> {
                left.putAll(right);
                return left;
            }).orElseThrow(CalculationException::new);
        for (int i = 0; i < 6; i++){
            rankColumn(allStats, i, i > 3);
        }
        return allStats;
    }

    private void rankColumn(Map<String, List<Double>> stats, int columnNumber, boolean isReversed){
        List<Double> statColumn = new ArrayList<>();
        for (List<Double> eachStatList : stats.values()){
            statColumn.add(eachStatList.get(columnNumber));
        }
        if (isReversed){
            statColumn.sort((o1, o2) -> Double.compare(o2, o1));
        } else {
            statColumn.sort(Double::compare);
        }

        List<Integer> ties = recordTies(statColumn);
        overwriteStatsWithRotoPoints(stats, columnNumber, statColumn);
        applyTies(stats, columnNumber, ties);
    }

    private List<Integer> recordTies(List<Double> statColumn) {
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

    private void overwriteStatsWithRotoPoints(Map<String, List<Double>> stats, int columnNumber, List<Double> statColumn) {
        for (List<Double> eachStatList : stats.values()){
            eachStatList.set(columnNumber, 1.0 + statColumn.indexOf(eachStatList.get(columnNumber)));
        }
    }

    //TODO more testing
    private void applyTies(Map<String, List<Double>> stats, int columnNumber, List<Integer> ties){
        for (int i = 0; i < ties.size(); i++){
            if (i < ties.size() - 1 && ties.get(i) == ties.get(i + 1)){
                int multiTie = ties.get(i);
                double specialModifier = .5;
                while (i < ties.size() - 1 && ties.get(i) == ties.get(i + 1)){
                    specialModifier += .5;
                    i++;
                }
                for (List<Double> each : stats.values()) {
                    if (each.get(columnNumber).intValue() == multiTie) {
                        each.set(columnNumber, each.get(columnNumber) + specialModifier);
                    }
                }
            }
            else {
                for (List<Double> each : stats.values()){
                    if (each.get(columnNumber).intValue()  == ties.get(i)){
                        each.set(columnNumber, each.get(columnNumber) + 0.5);
                    }
                }
            }
        }
    }

    //TODO more testing
    private void calculateRank(List<Player> players){
        for (int i = 0; i < players.size(); i++){
            double start = i + 1;
            int ties = 0;
            while (i + ties + 1 < players.size() && players.get(i).getTotal() == players.get(i + 1 + ties).getTotal()){
                start += .5;
                ties++;
            }
            players.get(i).setRank(start);
            while (ties > 0){
                i++;
                players.get(i).setRank(start);
                ties--;
            }
        }
    }
}
