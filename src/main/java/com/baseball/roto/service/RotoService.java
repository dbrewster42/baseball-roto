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
    private final ChangeService changeService;
//    @Value("${calculatePastMonth}") int week;
    private static final int NUMBER_OF_PLAYERS = 14;
    private List<Stats> statsList;
    private int week;

    public RotoService(StatsRepository repository, StatsMapper statsMapper, StatCalculationService statCalculationService, ChangeService changeService) {
        this.repository = repository;
        this.statsMapper = statsMapper;
        this.statCalculationService = statCalculationService;
        this.changeService = changeService;
    }

    public List<Roto> calculateRoto(Collection<Hitting> hitting, Collection<Pitching> pitching){
        week = (int) (repository.count() / NUMBER_OF_PLAYERS) + 1;
        statsList = hitting.stream()
            .map(hit -> statsMapper.toStats(hit, matchStats(pitching, hit.getName(), Pitching::getName), week))
            .collect(Collectors.toList());
        repository.saveAll(statCalculationService.calculateStats(statsList));

        List<Roto> rotoList = rankByGetter(statsList.stream().map(statsMapper::toRoto).collect(Collectors.toList()), Roto::getTotal);

        List<Stats> lastWeekStats = repository.findAllByWeek(week - 1);
        return changeService.calculateChanges(lastWeekStats, rotoList);
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

    private <T> T matchStats(Collection<T> collection, String name, Function<T, String> getter){
        return collection.stream()
            .filter(pitch -> getter.apply(pitch).equals(name))
            .findAny().orElseThrow(() -> new RuntimeException("player not found"));
    }

    protected List<Roto> rankByGetter(List<Roto> rotos, Function<Roto, Float> getter){
        for (int i = 0; i < rotos.size(); i++){
            float rank = i + 1;
            int tiesCount = 0;
            while (i + tiesCount + 1 < rotos.size() && getter.apply(rotos.get(i)).equals(getter.apply(rotos.get(i + 1 + tiesCount)))){
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

    public List<Roto> calculateLastMonth() {
        List<Stats> lastMonthsStats = repository.findAllByWeek(week - 4);
        statsList = statCalculationService.subtractOldStats(statsList, lastMonthsStats, week);

        return rankByGetter(statsList.stream().map(statsMapper::toRoto).collect(Collectors.toList()), Roto::getTotal);
    }
}
