package com.baseball.roto.service;

import com.baseball.roto.exception.BadInput;
import com.baseball.roto.mapper.RotoMapper;
import com.baseball.roto.mapper.StatsMapper;
import com.baseball.roto.model.League;
import com.baseball.roto.model.LeagueStats;
import com.baseball.roto.model.RawStats;
import com.baseball.roto.model.entity.Stats;
import com.baseball.roto.model.excel.CategoryRank;
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
    private final StatsMapper statsMapper;
    private final RotoMapper rotoMapper;
    private final RotoCalculator rotoCalculator;
    private final RankService rankService;
    private final int week;
    private final League league;

    public RotoService(StatsRepository repository, StatsMapper statsMapper, RotoMapper rotoMapper, RotoCalculator rotoCalculator,
                       RankService rankService, League league, @Value("${week}") int week) {
        this.repository = repository;
        this.statsMapper = statsMapper;
        this.rotoMapper = rotoMapper;
        this.rotoCalculator = rotoCalculator;
        this.rankService = rankService;
        this.league = league;
        this.week = week;
    }

    public List<Roto> calculateRoto(RawStats rawStats) {
        if (!getThisWeeksStats().isEmpty()) { throw new BadInput("The stats have already been calculated for the given week in this league");}
        List<Stats> statsList = convertToStatsList(rawStats);
        repository.saveAll(rotoCalculator.calculateRotoPoints(new LeagueStats(statsList)));
        return withWeeklyChanges(convertToSortedRoto(statsList));
    }

    public List<CategoryRank> getCategoryRanks(List<Roto> rotoList) {
        return rankService.getCategoryRanks(rotoList);
    }

    public List<Roto> limitRotoToIncludedWeeks(int includedWeeks){
        if (getThisWeeksStats().isEmpty()) { throw new BadInput("Roto must be calculated before it is limited to included weeks");}
        List<Stats> excludedStats = getStatsFromWeek(week - includedWeeks);
        LeagueStats recentStats = getRecentLeagueStats(getThisWeeksStats(), excludedStats, league, calculateWeight(includedWeeks));
        List<Stats> statsList = rotoCalculator.calculateRotoPoints(recentStats);
        return withChanges(convertToSortedRoto(statsList), excludedStats);
    }

    public List<Stats> getThisWeeksStats() {
        return getStatsFromWeek(week);
    }
    public List<Stats> getStatsFromWeek(int week) {
        return repository.findAllByWeek(week);
    }

    public void deleteThisWeeksStats() {
        deleteStatsByWeek(week);
    }
    public void deleteStatsByWeek(int week) {
        repository.deleteAll(getStatsFromWeek(week));
    }

    public void updatePlayerName(String oldName, String newName) {
        List<Stats> statsForOldName = repository.findAllByName(oldName);
        repository.deleteAll(statsForOldName);
        statsForOldName.forEach(stats -> stats.setName(newName));
        repository.saveAll(statsForOldName);
    }

    private List<Roto> convertToSortedRoto(List<Stats> statsList) {
        return rankService.rankRoto(rotoMapper.toRotoList(statsList));
    }

    private List<Stats> convertToStatsList(RawStats rawStats) {
        List<Stats> statsList = new ArrayList<>();
        for (int i = 0; i < league.getSize(); i++) {
            statsList.add(statsMapper.toStats(rawStats.getHittingList().get(i), rawStats.getPitchingList().get(i), week));
        }
        return statsList;
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
