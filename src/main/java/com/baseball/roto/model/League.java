package com.baseball.roto.model;

import lombok.Getter;

@Getter
public enum League {
    CHAMPIONS("Champs", 14, 6, 4, 4),
    PSD("PSD", 12, 5, 3, 4);

    private final String name;
    private final int playersNo;
    private final int statColumns;
    private final int hitCounterCol;
    private final int pitchCounterCol;

    League(String name, int playersNo, int statColumns, int hitCounterCol, int pitchCounterCol) {
        this.name = name;
        this.playersNo = playersNo;
        this.statColumns = statColumns;
        this.hitCounterCol = hitCounterCol;
        this.pitchCounterCol = pitchCounterCol;
    }

}
