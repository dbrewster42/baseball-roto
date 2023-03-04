package com.baseball.roto.service;

import com.baseball.roto.model.Stats;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.baseball.roto.mother.StatsMother.buildEvenStatsList;
import static com.baseball.roto.mother.StatsMother.buildEvenWeek12StatsList;
import static com.baseball.roto.mother.StatsMother.buildVariedStatsListWith2NameChanges;
import static org.assertj.core.api.Assertions.assertThat;

public class StatsQualifierTest {
    StatsSubtracter sut = new StatsSubtracter(new RotoCalculator());

    @Test
    void subtractOldStats1() {
//        List<Stats> recentStats = sut.subtractOldStats(buildEvenStatsList(), buildVariedStatsListWith1NameChange(), 9, 4);
        List<Stats> recentStats2 = sut.calculateRecentStats(buildEvenStatsList(), buildVariedStatsListWith2NameChanges(), 5, 4);
//        assertThat(recentStats.get(0).getName()).isEqualTo("player1");
    }

    @Test
    void subtractOldStatsWeighted() {
        List<Stats> recentStats = sut.calculateRecentStats(buildEvenWeek12StatsList(), buildEvenStatsList(), 12, 4);
        assertThat(recentStats.get(0).getEra()).isEqualTo(3.1);
        assertThat(recentStats.get(0).getWhip()).isEqualTo(1.35);
        assertThat(recentStats.get(0).getAvg()).isEqualTo(.295);
        assertThat(recentStats.get(0).getOps()).isEqualTo(.872);
    }
}
