package com.baseball.roto.service;

import com.baseball.roto.model.League;
import com.baseball.roto.model.LeagueStats;
import com.baseball.roto.model.entity.Stats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RotoCalculator {
    private final League league;

    public RotoCalculator(League league) {
        this.league = league;
    }

    protected List<Stats> calculateRotoPoints(LeagueStats leagueStats) {
        for (int i = 0; i < league.getStatColumns(); i++){
            rankColumn(leagueStats.getHittingStats(), i, true);
            rankColumn(leagueStats.getPitchingStats(), i, i < league.getPitchCountingStats());
        }
        leagueStats.determineTotals();
        return leagueStats.getStatsList();
    }

    private void rankColumn(Map<String, List<Float>> stats, int columnNumber, boolean isDescending){
        List<Float> statColumn = new ArrayList<>();
        for (List<Float> eachStatList : stats.values()){
            statColumn.add(eachStatList.get(columnNumber));
        }
        if (isDescending){
            statColumn.sort(Float::compare);
        } else {
            statColumn.sort((o1, o2) -> Float.compare(o2, o1));
        }
        List<Integer> ties = recordTies(statColumn);
        overwriteStatsWithRotoPoints(stats, columnNumber, statColumn);
        applyTies(stats, columnNumber, ties);
    }

    private List<Integer> recordTies(List<Float> statColumn) {
        List<Integer> ties = new ArrayList<>();
        float previous = -.1f;
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
