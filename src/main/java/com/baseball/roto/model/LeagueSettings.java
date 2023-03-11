package com.baseball.roto.model;

import lombok.Getter;

@Getter
public enum LeagueSettings {
    CHAMPIONS("n", 14, 6, 4, 4),
    OTHER("o", 12, 5, 3, 4);

    private final String name;
    private final int playersNo;
    private final int statColumns;
    private final int hCountingStats;
    private final int pCountingStats;
    LeagueSettings(String name, int playersNo, int statColumns, int hCountingStats, int pCountingStats) {
        this.name = name;
        this.playersNo = playersNo;
        this.statColumns = statColumns;
        this.hCountingStats = hCountingStats;
        this.pCountingStats = pCountingStats;
    }

}
