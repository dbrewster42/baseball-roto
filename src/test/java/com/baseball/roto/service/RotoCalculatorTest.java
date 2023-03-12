package com.baseball.roto.service;

import com.baseball.roto.model.LeagueSettings;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RotoCalculatorTest {
    RotoCalculator sut = new RotoCalculator(LeagueSettings.CHAMPIONS);


    @Test
    void calculateStats() {
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

//    @Test
//    void debug(){
//        Map<String, List<Float>> currentStats = new HashMap<>();
//        currentStats.put("player1", List.of(2f, 4f, 6f, 8f));
//        currentStats.put("player2", List.of(3f, 5f, 7f, 9f));
//        sut.debug(currentStats);
//    }
}