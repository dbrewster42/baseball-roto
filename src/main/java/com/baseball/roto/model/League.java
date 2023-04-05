package com.baseball.roto.model;

import com.baseball.roto.model.entity.ChampStats;
import com.baseball.roto.model.entity.PsdStats;
import com.baseball.roto.model.entity.StandardStats;
import com.baseball.roto.model.entity.Stats;
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
    CHAMPIONS(14, 6, 4, 4, ChampStats.class, ChampRepository.class),
    PSD(10, 5, 4, 3, PsdStats.class, PsdRepository.class),
    STANDARD(12, 5, 4, 3, StandardStats.class, StandardRepository.class);

    private final int numberOfTeams;
    private final int statColumnsPerSide;
    private final int hitCountingStats;
    private final int pitchCountingStats;
    private final Class<? extends Stats> entity;
    private final Class<? extends StatsRepository<?>> repository;

    League(int numberOfTeams, int statColumnsPerSide, int hitCountingStats, int pitchCountingStats,
           Class<? extends Stats> entity, Class<? extends StatsRepository<?>> repository) {
        this.numberOfTeams = numberOfTeams;
        this.statColumnsPerSide = statColumnsPerSide;
        this.hitCountingStats = hitCountingStats;
        this.pitchCountingStats = pitchCountingStats;
        this.entity = entity;
        this.repository = repository;
    }

    public static List<String> leagueNames() {
        return Arrays.stream(League.values()).map(Enum::name).collect(Collectors.toList());
    }
}
