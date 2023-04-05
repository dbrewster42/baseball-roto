package com.baseball.roto.model;

import com.baseball.roto.mapper.ChampStatsMapper;
import com.baseball.roto.mapper.PsdStatsMapper;
import com.baseball.roto.mapper.StandardStatsMapper;
import com.baseball.roto.mapper.StatsMapper;
import com.baseball.roto.repository.ChampRepository;
import com.baseball.roto.repository.PsdRepository;
import com.baseball.roto.repository.StandardRepository;
import com.baseball.roto.repository.StatsRepository;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum League {
    CHAMPIONS(14, 6, 4, 4, ChampStatsMapper.class, ChampRepository.class),
    PSD(10, 5, 4, 3, PsdStatsMapper.class, PsdRepository.class),
    STANDARD(12, 5, 4, 3, StandardStatsMapper.class, StandardRepository.class);

    private final int numberOfTeams;
    private final int statColumns;
    private final int hitCountingStats;
    private final int pitchCountingStats;
    private final Class<? extends StatsMapper<?>> mapper;
    private final Class<? extends StatsRepository<?>> repository;

    League(int numberOfTeams, int statColumns, int hitCountingStats, int pitchCountingStats,
           Class<? extends StatsMapper<?>> mapper, Class<? extends StatsRepository<?>> repository) {
        this.numberOfTeams = numberOfTeams;
        this.statColumns = statColumns;
        this.hitCountingStats = hitCountingStats;
        this.pitchCountingStats = pitchCountingStats;
        this.mapper = mapper;
        this.repository = repository;
    }

    public static List<String> leagueNames() {
        return Arrays.stream(League.values()).map(Enum::name).collect(Collectors.toList());
    }
}
