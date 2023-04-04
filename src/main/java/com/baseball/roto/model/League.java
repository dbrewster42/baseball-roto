package com.baseball.roto.model;

import com.baseball.roto.mapper.ChampStatsMapper;
import com.baseball.roto.mapper.PsdStatsMapper;
import com.baseball.roto.mapper.StandardStatsMapper;
import com.baseball.roto.mapper.StatsMapper;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum League {
    CHAMPIONS(14, 6, 4, 4, ChampStatsMapper.class),
    PSD(10, 5, 4, 3, PsdStatsMapper.class),
    STANDARD(12, 5, 4, 3, StandardStatsMapper.class);

    private final int numberOfTeams;
    private final int statColumns;
    private final int hitCountingStats;
    private final int pitchCountingStats;
    private final Class<? extends StatsMapper<?>> mapper;

    League(int numberOfTeams, int statColumns, int hitCountingStats, int pitchCountingStats, Class<? extends StatsMapper<?>> mapper) {
        this.numberOfTeams = numberOfTeams;
        this.statColumns = statColumns;
        this.hitCountingStats = hitCountingStats;
        this.pitchCountingStats = pitchCountingStats;
        this.mapper = mapper;
    }

    public static List<String> leagueNames() {
        return Arrays.stream(League.values()).map(Enum::name).collect(Collectors.toList());
    }
}
