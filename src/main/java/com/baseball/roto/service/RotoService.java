package com.baseball.roto.service;

import com.baseball.roto.mapper.StatsMapper;
import com.baseball.roto.model.LeagueSettings;
import com.baseball.roto.model.RawStats;
import com.baseball.roto.model.Stats;
import com.baseball.roto.model.excel.CategoryRank;
import com.baseball.roto.model.excel.Roto;
import com.baseball.roto.repository.StatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class RotoService {
    private final LeagueSettings league;
    private final StatsRepository repository;
    private final StatsMapper statsMapper;
    private final RotoCalculator statCalculator;
    private final StatsSubtracter qualifiedRoto;
    private final int week;

    public RotoService(LeagueSettings league, StatsRepository repository, StatsMapper statsMapper, RotoCalculator statCalculator, StatsSubtracter qualifiedRoto, @Value("${week}") int week) {
        this.league = league;
        this.week = week;
        this.repository = repository;
        this.statsMapper = statsMapper;
        this.statCalculator = statCalculator;
        this.qualifiedRoto = qualifiedRoto;
    }

    public List<Roto> calculateRoto(RawStats rawStats){
        List<Stats> statsList = prepareStats(rawStats);
        List<Roto> rotoList = convertToSortedRoto(statsList);
        return withWeeklyChanges(rotoList);
    }

    public List<Stats> prepareStats(RawStats rawStats){
        List<Stats> statsList = rawStats.convertToStatsList(statsMapper, week, league.getName());
        repository.saveAll(statCalculator.calculateRotoPoints(statsList));
        return statsList;
    }

    public List<CategoryRank> rankCategories(List<Roto> rotoList) {
        List<CategoryRank> categoryRanks = rankByGetter(rotoList, Roto::getHitting)
            .stream()
            .map(CategoryRank::new)
            .collect(Collectors.toList());

        List<Roto> sortedPitchers = rankByGetter(rotoList, Roto::getPitching);
        IntStream.range(0, league.getPlayersNo()).forEach(i -> categoryRanks.get(i).setPitchingCategories(sortedPitchers.get(i)));
        return categoryRanks;
    }

    protected List<Roto> rankByGetter(List<Roto> rotos, Function<Roto, Float> getter){
        rotos.sort((o1, o2) -> Float.compare(getter.apply(o2), getter.apply(o1)));
        for (int i = 0; i < rotos.size(); i++){
            float rank = i + 1;
            int tiesCount = 1;
            while (i + tiesCount < rotos.size() && getter.apply(rotos.get(i)).equals(getter.apply(rotos.get(i + tiesCount)))){
                rank += .5;
                tiesCount++;
            }
            rotos.get(i).setRank(rank);
            while (tiesCount > 1){ //set ranks for rest of tied players
                i++;
                rotos.get(i).setRank(rank);
                tiesCount--;
            }
        }
        return rotos;
    }

    public List<Roto> convertToSortedRoto(List<Stats> statsList) {
        return rankByGetter(statsMapper.toRotoList(statsList), Roto::getTotal);
    }

    public List<Stats> getLastWeeksStats() {
        return repository.findAllByWeek(week - 1);
    }
    public List<Stats> getStatsFromWeek(int x) {
        return repository.findAllByWeek(x);
    }
    public List<Stats> limitStatsToPastXWeeks(int x) {
        return qualifiedRoto.calculateRecentStats(getStatsFromWeek(week), getStatsFromWeek(week - x), week, x);
    }

    public void deleteLastWeek() {
        deleteByWeek((int) repository.count() / league.getPlayersNo());
    }
    public void deleteByWeek(int x) {
        repository.deleteAll(repository.findAllByWeek(x));
    }

    public void updatePlayerName(String newName, String oldName) {
        List<Stats> statsList = repository.findAllByName(oldName);
        repository.deleteAll(statsList);
        statsList.forEach(stats -> stats.setName(newName));
        repository.saveAll(statsList);
    }

    private List<Roto> withWeeklyChanges(List<Roto> currentRoto){
        List<Stats> lastWeeksRanks = getLastWeeksStats();
        List<Roto> unmatchedRotos = new ArrayList<>();
        for (Roto roto : currentRoto){
            lastWeeksRanks.stream()
                .filter(oldRoto -> oldRoto.getName().equals(roto.getName()))
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
