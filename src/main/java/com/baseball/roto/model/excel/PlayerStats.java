package com.baseball.roto.model.excel;

import lombok.Getter;

@Getter
public class PlayerStats {
    private final Hitting hitting;
    private final Pitching pitching;
    private final int week;

    public PlayerStats(Hitting hitting, Pitching pitching, int week) {
        this.hitting = hitting;
        this.pitching = pitching;
        this.week = week;
    }
}
