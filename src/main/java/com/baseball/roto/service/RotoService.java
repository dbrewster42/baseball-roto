package com.baseball.roto.service;

import com.baseball.roto.io.ExcelReader;
import com.baseball.roto.model.Hitting;
import com.baseball.roto.model.Pitching;
import com.baseball.roto.model.Stats;
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

    public RotoService(ExcelReader excelReader) {
        this.excelReader = excelReader;
    }

    public void printRoto(){
        Collection<Hitting> hitters = excelReader.readHitting();
        hitters.forEach(v -> log.info(v.toString()));
        rankHitting(hitters);

        Collection<Pitching> pitchers = excelReader.readPitching();

    }
    public void rankHitting(Collection<Hitting> hitters) {
//        hitters.forEach(Hitting::gatherStats);
        //todo list of lists of doubles, keep it in hitters or convert to players?
        //todo proba
        for (int i = 0; i < 6; i++){
            rankColumn(hitters, i, false);
        }
    }

    public void rankAllColumns(boolean isPitching){
        if (isPitching){
            for (int i = 0; i < 6; i++){
                rankColumn(i, i == 2 || i == 3);
            }
        } else {
            for (int i = 0; i < 6; i++){
                rankColumn(i, false);
            }
        }
    }
    public void rankColumn(Collection<Stats> stats, int columnNumber, boolean isReversed){
        List<Double> values = new ArrayList<>();
        List<List<Double>> masterList = stats.stream().map(Stats::gatherStats).collect(Collectors.toList());
        for (List<Double> each : masterList){
            values.add(each.get(columnNumber));
        }
        if (isReversed){
            values.sort((o1, o2) -> Double.compare(o2, o1));
        } else {
            values.sort(Double::compare);
        }

        List<Integer> ties = new ArrayList<>();
        double previous = -1.0;
        for (double value : values){
            if (previous != -1.0){
                //FIXME  doubles should not allow ties ie  if (previous % 1 == 0 && previous == value){
                if (previous == value){
                    ties.add(values.indexOf(value) + 1);
                }
            }
            previous = value;
        }
        for (List<Double> each : thePlayers.values()){
            each.set(columnNumber, (double) 1 + values.indexOf(each.get(columnNumber)));

        }
        applyTies(columnNumber, ties);
    }

}
