package com.baseball.roto.model;

import com.baseball.roto.model.entity.ChampStats;
import com.baseball.roto.model.entity.PsdStats;
import lombok.Getter;

@Getter
public enum League {
    CHAMPIONS("Champs", 14, 6, 4, 4, ChampStats.class),
    PSD("PSD", 10, 5, 3, 4, PsdStats.class);

    private final String name;
    private final int size;
    private final int statColumns;
    private final int hitCounterCol;
    private final int pitchCounterCol;
    private final Class<?> pojo;

    League(String name, int size, int statColumns, int hitCounterCol, int pitchCounterCol, Class<?> pojo) {
        this.name = name;
        this.size = size;
        this.statColumns = statColumns;
        this.hitCounterCol = hitCounterCol;
        this.pitchCounterCol = pitchCounterCol;
        this.pojo = pojo;
    }

}
