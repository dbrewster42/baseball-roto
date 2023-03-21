package com.baseball.roto.model;

import lombok.Getter;

@Getter
public enum League {
    CHAMPIONS("Champs", 14, 6, 4, 4),
    PSD("PSD", 10, 5, 3, 4);

    private final String name;
    private final int size;
    private final int statColumns;
    private final int hitCounterCol;
    private final int pitchCounterCol;

    League(String name, int size, int statColumns, int hitCounterCol, int pitchCounterCol) {
        this.name = name;
        this.size = size;
        this.statColumns = statColumns;
        this.hitCounterCol = hitCounterCol;
        this.pitchCounterCol = pitchCounterCol;
    }

}
