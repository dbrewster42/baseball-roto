package com.baseball.roto.service;

import com.baseball.roto.exception.CalculationException;
import com.baseball.roto.io.ExcelReader;
import com.baseball.roto.io.ExcelWriter;
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
    private final ExcelReader excelReader;
    private final ExcelWriter excelWriter;

    public RotoService(ExcelReader excelReader, ExcelWriter excelWriter) {
        this.excelReader = excelReader;
        this.excelWriter = excelWriter;
    }

    public void calculateRotoRanks(){
        List<Player> players = calculateHittingRoto(excelReader.readHitting());
        finishRotoCalculations(excelReader.readPitching(), players);
        calculateRank(players);
        excelWriter.writeRoto(players);
    }

    private void sortByTotalRank(List<Player> players) {
        players.forEach(v -> v.setTotal(v.getHitting() + v.getPitching()));
        players.sort((o1, o2) -> Double.compare(o2.getTotal(), o1.getTotal()));
    }

    public List<Player> calculateHittingRoto(Collection<Hitting> hitting) {
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
    public void finishRotoCalculations(Collection<Pitching> pitching, List<Player> players) {
        Map<String, List<Double>> allStats = pitching.stream()
            .map(Pitching::gatherStats)
            .reduce((left, right) -> {
                left.putAll(right);
                return left;
            }).orElseThrow(CalculationException::new);
        for (int i = 0; i < 6; i++){
            rankColumn(allStats, i, i > 3);
        }
        players.forEach(player -> {
            player.setPitching(allStats.get(player.getName()).stream().mapToDouble(v -> v).sum());
            player.setTotal(player.getHitting() + player.getPitching());
        });
    }

    public void rankColumn(Map<String, List<Double>> stats, int columnNumber, boolean isReversed){
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
        overwriteStatWithRank(stats, columnNumber, statColumn);
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

    private void overwriteStatWithRank(Map<String, List<Double>> stats, int columnNumber, List<Double> statColumn) {
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
    public void calculateRank(List<Player> players){
        players.sort((o1, o2) -> Double.compare(o2.getTotal(), o1.getTotal()));
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
