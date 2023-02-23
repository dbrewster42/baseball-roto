package com.baseball.roto.service;

import com.baseball.roto.model.Stats;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.baseball.roto.mother.StatsMother.buildEvenStatsList;
import static com.baseball.roto.mother.StatsMother.buildEvenWeek12StatsList;
import static com.baseball.roto.mother.StatsMother.buildVariedStatsList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StatCalculationServiceTest {
    StatCalculationService sut = new StatCalculationService();


    @Test
    void calculateStats() {
    }

    @Test
    void subtractOldStats1() {
        List<Stats> recentStats = sut.subtractOldStats(buildEvenStatsList(), buildVariedStatsList(), 8);
//        assertThat(recentStats.get(0).getName()).isEqualTo("player1");
    }

    @Test
    void subtractOldStatsWeighted() {
        List<Stats> recentStats = sut.subtractOldStats(buildEvenWeek12StatsList(), buildEvenStatsList(), 12);
        assertThat(recentStats.get(0).getEra()).isEqualTo(3.1);
        assertThat(recentStats.get(0).getWhip()).isEqualTo(1.35);
        assertThat(recentStats.get(0).getAvg()).isEqualTo(.295);
        assertThat(recentStats.get(0).getOps()).isEqualTo(.872);
    }

    @Test
    void customTest() {
        int week = 8;
        float value1 = 3.0f;
        float value2 = 4.0f;

        assertThat(asdf(week, value1, value2)).isEqualTo(5.0f);
        assertThat(asdf(week, value2, value1)).isEqualTo(2.0f);

        week = 12;
        assertThat(asdf(week, value1, value2)).isEqualTo(6.0f);
        assertThat(asdf(week, value2, value1)).isEqualTo(1.0f);

        week = 16;
        assertThat(asdf(week, value1, value2)).isEqualTo(7.0f);
        assertThat(asdf(week, value2, value1)).isEqualTo(0.0f);
    }
    private float asdf(int week, float oldValue, float newValue) {
        float weight = (week - 4) / 4f;
        float diff = newValue - oldValue;
        return newValue + (diff * weight);
    }
    @Test
    void customTest2() {
        assertThat(divideInts(12, 8)).isEqualTo(1f);
        assertThat(divideInts(12, 6)).isEqualTo(1.5f);
        assertThat(divideInts(12, 3)).isEqualTo(2.25f);

    }
    private float divideInts(int first, int second) {
        return (first - second) / 4f;
    }
}