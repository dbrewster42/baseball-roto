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
    private List<Stats> statsList;
    private int week;
    private int numberOfPlayers;

    public RotoService(StatsRepository repository, StatsMapper statsMapper, StatCalculationService statCalculationService) {
        this.repository = repository;
        this.statsMapper = statsMapper;
        this.statCalculationService = statCalculationService;
    }

    public List<Roto> calculateRoto(Collection<Hitting> hitting, Collection<Pitching> pitching){
        numberOfPlayers = hitting.size();
        week = (int) (repository.count() / numberOfPlayers) + 1;
        statsList = hitting.stream()
            .map(hit -> statsMapper.toStats(hit, matchStats(pitching, hit.getName()), week))
            .collect(Collectors.toList());
        repository.saveAll(statCalculationService.calculateStats(statsList)); //todo save later

        List<Roto> rotoList = rankByGetter(statsMapper.toRotoList(statsList), Roto::getTotal);

        List<Stats> lastWeekStats = repository.findAllByWeek(week - 1);
        return withWeeklyChanges(lastWeekStats, rotoList);
    }

    private Pitching matchStats(Collection<Pitching> pitching, String name) {
        return pitching.stream()
            .filter(p -> p.getName().equals(name))
            .findAny().orElseThrow(() -> new RuntimeException("player not found"));
    }

    public List<CategoryRank> rankCategories(List<Roto> rotoList) {
        List<CategoryRank> categoryRanks = rankByGetter(rotoList, Roto::getHitting)
            .stream()
            .map(CategoryRank::new)
            .collect(Collectors.toList());

        List<Roto> sortedPitchers = rankByGetter(rotoList, Roto::getPitching);
        IntStream.range(0, numberOfPlayers).forEach(i -> categoryRanks.get(i).setPitchingCategories(sortedPitchers.get(i)));
        return categoryRanks;
    }

    public List<Roto> calculateLastMonth() {
        List<Stats> lastMonthsStats = repository.findAllByWeek(week - 4);
        statsList = statCalculationService.subtractOldStats(statsList, lastMonthsStats, week);

        return rankByGetter(statsMapper.toRotoList(statsList), Roto::getTotal);
    }

    protected List<Roto> rankByGetter(List<Roto> rotos, Function<Roto, Float> getter){
        rotos.sort((o1, o2) -> Float.compare(getter.apply(o2), getter.apply(o1))); //todo needs to be new list?
        for (int i = 0; i < numberOfPlayers; i++){
            float rank = i + 1;
            int tiesCount = 1;
            while (i + tiesCount < numberOfPlayers && getter.apply(rotos.get(i)).equals(getter.apply(rotos.get(i + tiesCount)))){
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

    private List<Roto> withWeeklyChanges(List<Stats> lastWeeksRanks, List<Roto> currentRoto){
        List<Roto> unmatchedRotos = new ArrayList<>();
        for (Roto roto : currentRoto){
            lastWeeksRanks.stream()
                .filter(oldRoto -> oldRoto.getName().equals(roto.getName())).findAny()
                .ifPresentOrElse(
                    roto::setChangesFromGiven,
                    () -> unmatchedRotos.add(roto));

        }
        if (unmatchedRotos.size() == 1){
            lastWeeksRanks.stream()
                .filter(lw -> currentRoto.stream().noneMatch(roto -> lw.getName().equals(roto.getName())))
                .findAny()
                .ifPresent(lw -> unmatchedRotos.get(0).setChangesFromGiven(lw));
        }
        currentRoto.forEach(roto -> log.info(roto.toString()));
        log.info("changes calculated with {} unmatched players", unmatchedRotos.size());
        return currentRoto;
    }
}
