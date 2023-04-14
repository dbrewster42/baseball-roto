package com.baseball.roto.service;

import com.baseball.roto.mapper.RotoMapper;
import com.baseball.roto.model.League;
import com.baseball.roto.model.LeagueStats;
import com.baseball.roto.model.entity.Stats;
import com.baseball.roto.model.excel.CategoryRank;
import com.baseball.roto.model.excel.Roto;
import com.baseball.roto.repository.StatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.baseball.roto.service.StatsSubtraction.getRecentLeagueStats;

@Service
@Slf4j
public class RotoService {
    private final RotoMapper rotoMapper;
    private final RotoCalculator rotoCalculator;
    private final RankService rankService;
    private final LeagueService leagueService;
    private League league;
    private StatsRepository<Stats> repository;
    private int week;

    public RotoService(RotoMapper rotoMapper, RotoCalculator rotoCalculator, RankService rankService, LeagueService leagueService) {
        this.rotoMapper = rotoMapper;
        this.rotoCalculator = rotoCalculator;
        this.rankService = rankService;
        this.leagueService = leagueService;
    }

    public List<Roto> calculateRoto(LeagueStats leagueStats) {
        setup(leagueService);
        List<Stats> statsList = rotoCalculator.calculateRotoPoints(leagueStats, league);
        statsList.forEach(stats -> stats.setWeek(week));
        repository.saveAll(statsList);
        return withWeeklyChanges(convertToSortedRoto(statsList));
    }

    public List<CategoryRank> getCategoryRanks(List<Roto> rotoList) {
        return rankService.getCategoryRanks(rotoList);
    }

    public List<Roto> limitRotoToIncludedWeeks(int includedWeeks){
        List<Stats> excludedStats = getStatsFromWeek(week - includedWeeks);
        LeagueStats recentStats = getRecentLeagueStats(getThisWeeksStats(), excludedStats, league, week, includedWeeks);
        List<Stats> statsList = rotoCalculator.calculateRotoPoints(recentStats, league);
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

    private void setup(LeagueService leagueService) {
        this.league = leagueService.getLeague();
        this.repository = leagueService.repository();
        this.week = determineWeek();
    }

    private List<Roto> convertToSortedRoto(List<Stats> statsList) {
        return rankService.rankRoto(rotoMapper.toRotoList(statsList));
    }

    private int determineWeek() {
        return (int) (repository.count() / league.getNumberOfTeams()) + 1;
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
