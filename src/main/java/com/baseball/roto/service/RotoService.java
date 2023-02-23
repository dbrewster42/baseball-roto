package com.baseball.roto.service;

import com.baseball.roto.mapper.StatsMapper;
import com.baseball.roto.model.Stats;
import com.baseball.roto.model.excel.CategoryRank;
import com.baseball.roto.model.excel.Hitting;
import com.baseball.roto.model.excel.Pitching;
import com.baseball.roto.model.excel.Roto;
import com.baseball.roto.repository.StatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class RotoService {
    private final StatsRepository repository;
    private final StatsMapper statsMapper;
    private final StatCalculationService statCalculationService;
    private static final int NUMBER_OF_PLAYERS = 14; //needs to be property if kept
    private List<Stats> statsList;
    private int week;

    public RotoService(StatsRepository repository, StatsMapper statsMapper, StatCalculationService statCalculationService) {
        this.repository = repository;
        this.statsMapper = statsMapper;
        this.statCalculationService = statCalculationService;
    }

    public List<Roto> calculateRoto(Collection<Hitting> hitting, Collection<Pitching> pitching){
        week = (int) (repository.count() / NUMBER_OF_PLAYERS) + 1; //todo better way to get week? audit column?
        statsList = hitting.stream()
            .map(hit -> statsMapper.toStats(hit, matchStats(pitching, hit.getName(), Pitching::getName), week))
            .collect(Collectors.toList());
        repository.saveAll(statCalculationService.calculateStats(statsList));

        List<Roto> rotoList = rankByGetter(statsList.stream().map(statsMapper::toRoto).collect(Collectors.toList()), Roto::getTotal);

        List<Stats> lastWeekStats = repository.findAllByWeek(week - 1);
        return withWeeklyChanges(lastWeekStats, rotoList);
    }

    public List<CategoryRank> rankCategories(List<Roto> rotoList) {
        List<CategoryRank> categoryRanks = rankByGetter(sortByGetter(rotoList, Roto::getHitting), Roto::getHitting)
            .stream()
            .map(CategoryRank::new)
            .collect(Collectors.toList());

        List<Roto> sortedPitchers = rankByGetter(sortByGetter(rotoList, Roto::getPitching), Roto::getPitching);
        IntStream.range(0, sortedPitchers.size()).forEach(i -> categoryRanks.get(i).setPitchingCategories(sortedPitchers.get(i)));
        return categoryRanks;
    }

    public List<Roto> calculateLastMonth() {
        List<Stats> lastMonthsStats = repository.findAllByWeek(week - 4);
        statsList = statCalculationService.subtractOldStats(statsList, lastMonthsStats, week);

        return rankByGetter(statsList.stream().map(statsMapper::toRoto).collect(Collectors.toList()), Roto::getTotal);
    }

    private <T> T matchStats(Collection<T> collection, String name, Function<T, String> getter){
        return collection.stream()
            .filter(pitch -> getter.apply(pitch).equals(name))
            .findAny().orElseThrow(() -> new RuntimeException("player not found"));
    }

    protected List<Roto> rankByGetter(List<Roto> rotos, Function<Roto, Float> getter){
        for (int i = 0; i < NUMBER_OF_PLAYERS; i++){
            float rank = i + 1;
            int tiesCount = 0;
            while (i + tiesCount + 1 < NUMBER_OF_PLAYERS && getter.apply(rotos.get(i)).equals(getter.apply(rotos.get(i + 1 + tiesCount)))){
                rank += .5;
                tiesCount++;
            }
            rotos.get(i).setRank(rank);
            while (tiesCount > 0){ //set ranks for rest of tied players
                i++;
                rotos.get(i).setRank(rank);
                tiesCount--;
            }
        }
        return rotos;
    }

    private List<Roto> sortByGetter(List<Roto> players, Function<Roto, Float> getter){
        return players.stream()
            .sorted((o1, o2) -> Float.compare(getter.apply(o2), getter.apply(o1)))
            .collect(Collectors.toList());
    }

    private List<Roto> withWeeklyChanges(List<Stats> lastWeeksRanks, List<Roto> currentRoto){
        List<Roto> unmatchedRotos = new ArrayList<>();
        for (Roto roto : currentRoto){
            lastWeeksRanks.stream()
                .filter(oldRoto -> oldRoto.getName().equals(roto.getName())).findAny()
                .ifPresentOrElse(
                    oldRoto -> calculateChangeInPlayer(roto, oldRoto),
                    () -> unmatchedRotos.add(roto));
        }
        if (unmatchedRotos.size() == 1){
            lastWeeksRanks.stream()
                .filter(lw -> currentRoto.stream().noneMatch(roto -> lw.getName().equals(roto.getName())))
                .findAny()
                .ifPresent(lw -> calculateChangeInPlayer(unmatchedRotos.get(0), lw));
        }
        currentRoto.forEach(roto -> log.info(roto.toString()));
        log.info("changes calculated with {} unmatched players", unmatchedRotos.size());
        return currentRoto;
    }

    private void calculateChangeInPlayer(Roto roto, Stats oldStats){;
        roto.setTotalChange(roto.getTotal() - oldStats.getTotal());
        roto.setHittingChange(roto.getHitting() - oldStats.getHitting());
        roto.setPitchingChange(roto.getPitching() - oldStats.getPitching());
    }
}
