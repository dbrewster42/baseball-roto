package com.baseball.roto.service;

import com.baseball.roto.mapper.RotoStatsMapper;
import com.baseball.roto.model.League;
import com.baseball.roto.model.LeagueStats;
import com.baseball.roto.model.RawStats;
import com.baseball.roto.model.Stats;
import com.baseball.roto.model.excel.Roto;
import com.baseball.roto.repository.StatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.baseball.roto.service.StatsSubtraction.getRecentLeagueStats;

@Service
@Slf4j
public class RotoService {
    private final StatsRepository repository;
    private final RotoStatsMapper rotoStatsMapper;
    private final RotoCalculator rotoCalculator;
    private final RankService rankService;
    private final League league;
    private final int week;

    public RotoService(StatsRepository repository, RotoStatsMapper rotoStatsMapper, RotoCalculator rotoCalculator,
                       RankService rankService, League league, @Value("${week}") int week) {
        this.repository = repository;
        this.rotoStatsMapper = rotoStatsMapper;
        this.rotoCalculator = rotoCalculator;
        this.rankService = rankService;
        this.league = league;
        this.week = week;
    }

    public List<Roto> calculateRoto(RawStats rawStats) {
        if (!getStatsFromWeek(week).isEmpty()) { throw new RuntimeException("the given week is incorrect");}
        List<Stats> statsList = rawStats.convertToStatsList(rotoStatsMapper, week, league);
        repository.saveAll(rotoCalculator.calculateRotoPoints(new LeagueStats(statsList)));
        return withWeeklyChanges(convertToSortedRoto(statsList));
    }

    public List<Roto> limitCalculatedRotoToIncludedWeeks(int includedWeeks){
        List<Stats> excludedStats = getStatsFromWeek(week - includedWeeks);
        LeagueStats recentStats = getRecentLeagueStats(getThisWeeksStats(), excludedStats, league, calculateWeight(includedWeeks));
        List<Stats> statsList = rotoCalculator.calculateRotoPoints(recentStats);
        return withChanges(convertToSortedRoto(statsList), excludedStats);
    }

    public List<Stats> getThisWeeksStats() {
        return getStatsFromWeek(week);
    }
    public List<Stats> getStatsFromWeek(int week) {
        return repository.findAllByWeekAndLeague(week, league.getName());
    }

    public void deleteThisWeeksStats() {
        deleteStatsByWeek(week);
    }
    public void deleteStatsByWeek(int week) {
        repository.deleteAll(getStatsFromWeek(week));
    }

    public void updatePlayerName(String newName, String oldName) {
        List<Stats> statsForOldName = repository.findAllByNameAndLeague(oldName, league.getName());
        repository.deleteAll(statsForOldName);
        statsForOldName.forEach(stats -> stats.setName(newName));
        repository.saveAll(statsForOldName);
    }

    private List<Roto> convertToSortedRoto(List<Stats> statsList) {
        return rankService.rankRoto(rotoStatsMapper.toRotoList(statsList));
    }

    private float calculateWeight(int includedWeeks) {
        return (week - includedWeeks) / (float) includedWeeks;
    }

    private List<Roto> withWeeklyChanges(List<Roto> currentRoto) {
        return withChanges(currentRoto, getStatsFromWeek(week - 1));
    }
    private List<Roto> withChanges(List<Roto> currentRoto, List<Stats> priorStats){
        List<Roto> unmatchedRotos = new ArrayList<>();
        for (Roto roto : currentRoto){
            priorStats.stream()
                .filter(oldStats -> oldStats.getName().equals(roto.getName()))
                .findAny()
                .ifPresentOrElse(
                    roto::setChangesFromGiven,
                    () -> unmatchedRotos.add(roto));
        }
        if (unmatchedRotos.size() == 1){
            priorStats.stream()
                .filter(lw -> currentRoto.stream()
                    .noneMatch(roto -> lw.getName().equals(roto.getName())))
                .findAny()
                .ifPresent(lw -> unmatchedRotos.get(0).setChangesFromGiven(lw));
        }
        currentRoto.forEach(roto -> log.info(roto.toString()));
        return currentRoto;
    }

}
