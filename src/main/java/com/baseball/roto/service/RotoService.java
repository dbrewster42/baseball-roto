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

@Service
@Slf4j
public class RotoService {
    private final StatsRepository repository;
    private final StatsMapper statsMapper;
    private final StatCalculationService statCalculationService;
    private final ChangeService changeService;

    public RotoService(StatsRepository repository, StatsMapper statsMapper, StatCalculationService statCalculationService, ChangeService changeService) {
        this.repository = repository;
        this.statsMapper = statsMapper;
        this.statCalculationService = statCalculationService;
        this.changeService = changeService;
    }

    public List<Roto> calculateRoto(Collection<Hitting> hitting, Collection<Pitching> pitching){
        int week = (int) (repository.count() / 14) + 1;
        List<Stats> statsList = hitting.stream()
            .map(hit -> statsMapper.toStats(hit, matchStats(pitching, hit.getName(), Pitching::getName), week))
            .collect(Collectors.toList());
        repository.saveAll(statCalculationService.calculateStats(statsList));

        List<Roto> rotos = withRank(statsList.stream().map(statsMapper::toRoto).collect(Collectors.toList()));

        List<Stats> lastWeekStats = repository.findAllByWeek(week - 1);
        return changeService.calculateChanges(lastWeekStats, rotos);
    }

    public List<CategoryRank> rankCategories(List<Roto> rotoList) {
        List<Roto> sortedHitters = sortByGetter(rotoList, Roto::getHitting);
        List<Roto> sortedPitchers = sortByGetter(rotoList, Roto::getPitching);

        List<CategoryRank> categoryRanks = new ArrayList<>();
        for (int i = 0; i < sortedHitters.size(); i++) {
            categoryRanks.add(new CategoryRank(i + 1, sortedHitters.get(i), sortedPitchers.get(i)));
        }
        return categoryRanks;
    }

    private <T> T matchStats(Collection<T> collection, String name, Function<T, String> getter){
        return collection.stream()
            .filter(pitch -> getter.apply(pitch).equals(name))
            .findAny().orElseThrow(() -> new RuntimeException("player not found"));
    }


    //TODO more testing and can use for categoryRanks by adding getter as parameter
    protected List<Roto> withRank(List<Roto> rotos){
        for (int i = 0; i < rotos.size(); i++){
            float rank = i + 1;
            int tiesCount = 0;
            while (i + tiesCount + 1 < rotos.size() && rotos.get(i).getTotal() == rotos.get(i + 1 + tiesCount).getTotal()){
                rank += .5;
                tiesCount++;
            }
            rotos.get(i).setRank(rank);
            while (tiesCount > 0){
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
}
