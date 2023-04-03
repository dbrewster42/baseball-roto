package com.baseball.roto.model;

import com.baseball.roto.model.entity.ChampStats;
import com.baseball.roto.model.entity.PsdStats;
import com.baseball.roto.model.entity.StandardStats;
import lombok.Getter;

@Getter
public enum League {
    CHAMPIONS("Champs", 14, 6, 4, 4, ChampStats.class),
    PSD("PSD", 10, 5, 4, 3, PsdStats.class),
    STANDARD("STANDARD", 12, 5, 4, 3, StandardStats.class);

    private final String name;
    private final int size;
    private final int statColumns;
    private final int hitCountingStats;
    private final int pitchCountingStats;
    private final Class<?> pojo;

    League(String name, int size, int statColumns, int hitCountingStats, int pitchCountingStats, Class<?> pojo) {
        this.name = name;
        this.size = size;
        this.statColumns = statColumns;
        this.hitCountingStats = hitCountingStats;
        this.pitchCountingStats = pitchCountingStats;
        this.pojo = pojo;
    }

}
