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
    private final League league;
    private final StatsRepository repository;
    private final RotoStatsMapper rotoStatsMapper;
    private final RotoCalculator rotoCalculator;
    private final RankService rankService;
    private final int week;

    public RotoService(League league, StatsRepository repository, RotoStatsMapper rotoStatsMapper, RotoCalculator rotoCalculator, RankService rankService, @Value("${week}") int week) {
        this.league = league;
        this.rankService = rankService;
        this.week = week;
        this.repository = repository;
        this.rotoStatsMapper = rotoStatsMapper;
        this.rotoCalculator = rotoCalculator;
    }

    public List<Roto> calculateRoto(RawStats rawStats){
        List<Stats> statsList = calculateRotoStats(rawStats);
        List<Roto> rotoList = convertToSortedRoto(statsList);
        return withWeeklyChanges(rotoList, getLastWeeksStats());
    }

    public List<Roto> calculateRotoForPastXWeeks(int includedWeeks){
        float weight = (week - includedWeeks) / (float) includedWeeks;
        LeagueStats recentLeagueStats = getRecentLeagueStats(getStatsFromWeek(week), getStatsFromWeek(week - includedWeeks), league, weight);
        List<Stats> statsList = rotoCalculator.calculateRotoPoints(recentLeagueStats);
        return convertToSortedRoto(statsList);
    }

    private List<Stats> calculateRotoStats(RawStats rawStats){
        List<Stats> statsList = rawStats.convertToStatsList(rotoStatsMapper, week, league.getName());
        repository.saveAll(rotoCalculator.calculateRotoPoints(new LeagueStats(statsList)));
        return statsList;
    }

    public List<Stats> getLastWeeksStats() {
        return repository.findAllByWeek(week - 1);
    }
    public List<Stats> getStatsFromWeek(int x) {
        return repository.findAllByWeek(x);
    }

    public void deleteLastWeek() {
        deleteByWeek((int) repository.count() / league.getPlayersNo());
    }
    public void deleteByWeek(int x) {
        repository.deleteAll(repository.findAllByWeek(x));
    }

    public void updatePlayerName(String newName, String oldName) {
        List<Stats> statsForOldName = repository.findAllByName(oldName);
        repository.deleteAll(statsForOldName);
        statsForOldName.forEach(stats -> stats.setName(newName));
        repository.saveAll(statsForOldName);
    }

    private List<Stats> limitStatsToPastXWeeks(int includedWeeks) {
        float weight = (week - includedWeeks) / (float) includedWeeks;
        LeagueStats recentLeagueStats = getRecentLeagueStats(getStatsFromWeek(week), getStatsFromWeek(week - includedWeeks), league, weight);
        return rotoCalculator.calculateRotoPoints(recentLeagueStats);
    }

    private List<Roto> convertToSortedRoto(List<Stats> statsList) {
        return rankService.rankRoto(rotoStatsMapper.toRotoList(statsList));
    }

    private List<Roto> withWeeklyChanges(List<Roto> currentRoto, List<Stats> lastWeeksRanks){
        List<Roto> unmatchedRotos = new ArrayList<>();
        for (Roto roto : currentRoto){
            lastWeeksRanks.stream()
                .filter(oldStats -> oldStats.getName().equals(roto.getName()))
                .findAny()
                .ifPresentOrElse(
                    roto::setChangesFromGiven,
                    () -> unmatchedRotos.add(roto));
        }
        if (unmatchedRotos.size() == 1){
            lastWeeksRanks.stream()
                .filter(lw -> currentRoto.stream()
                    .noneMatch(roto -> lw.getName().equals(roto.getName())))
                .findAny()
                .ifPresent(lw -> unmatchedRotos.get(0).setChangesFromGiven(lw));
        }
        currentRoto.forEach(roto -> log.info(roto.toString()));
        log.info("changes calculated with {} unmatched players", unmatchedRotos.size());
        return currentRoto;
    }

}
