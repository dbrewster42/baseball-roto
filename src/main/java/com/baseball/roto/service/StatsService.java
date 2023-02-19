package com.baseball.roto.service;

import com.baseball.roto.mapper.StatsMapper;
import com.baseball.roto.model.Hitting;
import com.baseball.roto.model.Pitching;
import com.baseball.roto.model.Stats;
import com.baseball.roto.repository.StatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatsService {
//    private final RotoService rotoService;
    private final StatsRepository repository;
    private final StatsMapper statsMapper;
    private final int week;

    public StatsService(StatsRepository repository, StatsMapper statsMapper, @Value("${week}") int week) {
//        this.rotoService = rotoService;
        this.repository = repository;
        this.statsMapper = statsMapper;
        this.week = week;
    }

    public List<Stats> saveStats(Collection<Hitting> hitting, Collection<Pitching> pitching) {
        return hitting.stream()
            .map(hit -> statsMapper.toStats(hit, pitching.stream().filter(pitch -> pitch.getName().equals(hit.getName())).findAny().orElseThrow(), week))
            .peek(v -> log.info(v.toString()))
            .map(repository::save)
            .collect(Collectors.toList());
//        return hitting.stream()
//            .map(hit -> new Stats(hit, pitching.stream().filter(pitch -> pitch.getName().equals(hit.getName())).findAny().orElseThrow()))
//            .map(repository::save)
//            .collect(Collectors.toList());
    }

//    private List<Roto> applyRotoCalculations(Map<String, List<Double>> stats, List<Roto> rotos) {
//        rotos.forEach(roto -> {
//            roto.setPitching(stats.get(roto.getName()).stream().mapToDouble(v -> v).sum());
//            roto.setTotal(roto.getHitting() + roto.getPitching());
//        });
//        rotos.sort((o1, o2) -> Double.compare(o2.getTotal(), o1.getTotal()));
//        calculateRank(rotos);
//        return rotoService.calculateRank(rotos);
//    }
//
//    private List<Stats> calculateHittingStats(Collection<Hitting> hitting) {
//        Map<String, List<Double>> allStats = hitting.stream()
//            .map(Hitting::gatherStats)
//            .reduce((left, right) -> {
//                left.putAll(right);
//                return left;
//            }).orElseThrow(CalculationException::new);
//        for (int i = 0; i < 6; i++){
//            rotoService.rankColumn(allStats, i, false);
//        }
//        return allStats.entrySet().stream()
//            .map(roto -> new Stats(roto.getKey(), roto.getValue().stream().mapToDouble(v -> v).sum()))
//            .collect(Collectors.toList());
//    }
//    private Map<String, List<Double>> calculatePitchingStats(Collection<Pitching> pitching) {
//        Map<String, List<Double>> allStats = pitching.stream()
//            .map(Pitching::gatherStats)
//            .reduce((left, right) -> {
//                left.putAll(right);
//                return left;
//            }).orElseThrow(CalculationException::new);
//        for (int i = 0; i < 6; i++){
//            rotoService.rankColumn(allStats, i, i > 3);
//        }
//        return allStats;
//    }
}
