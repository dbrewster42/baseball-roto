package com.baseball.roto.service;

import com.baseball.roto.model.League;
import com.baseball.roto.model.LeagueStats;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.baseball.roto.mother.StatsMother.buildEvenStatsList;
import static com.baseball.roto.mother.StatsMother.buildEvenWeek12StatsList;
import static com.baseball.roto.mother.StatsMother.buildVariedStatsListWith2NameChanges;
import static com.baseball.roto.mother.StatsMother.buildWeek12StatsList;
import static com.baseball.roto.service.StatsSubtraction.getRecentLeagueStats;
import static org.junit.jupiter.api.Assertions.*;

class StatsSubtractionTest {

    @Test
    void getRecentLeagueStatsTest() {
        LeagueStats leagueStats = getRecentLeagueStats(buildWeek12StatsList(), buildEvenStatsList(), League.CHAMPIONS, 1f);

        List<Float> player1HittingStats = leagueStats.getHittingStats().get("player1");
        List<Float> player1PitchingStats = leagueStats.getPitchingStats().get("player1");
        List<Float> player2HittingStats = leagueStats.getHittingStats().get("player2");
        List<Float> player3HittingStats = leagueStats.getHittingStats().get("player3");
        List<Float> player3PitchingStats = leagueStats.getPitchingStats().get("player3");


        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(player1HittingStats.get(0)).isEqualTo(30);
            soft.assertThat(player1HittingStats.get(1)).isEqualTo(10);
            soft.assertThat(player1HittingStats.get(4)).isEqualTo(.28f);
            soft.assertThat(player1HittingStats.get(5)).isEqualTo(.848f);
            soft.assertThat(player1PitchingStats.get(4)).isEqualTo(3.1f);
            soft.assertThat(player1PitchingStats.get(5)).isEqualTo(1.25f);

            soft.assertThat(player2HittingStats.get(0)).isEqualTo(20);
            soft.assertThat(player2HittingStats.get(2)).isEqualTo(25);
            soft.assertThat(player2HittingStats.get(4)).isEqualTo(.28f);
            soft.assertThat(player2HittingStats.get(5)).isEqualTo(.848f);

            soft.assertThat(player3HittingStats.get(0)).isEqualTo(20);
            soft.assertThat(player3HittingStats.get(2)).isEqualTo(30);
            soft.assertThat(player3HittingStats.get(4)).isEqualTo(.29f);
            soft.assertThat(player3HittingStats.get(5)).isEqualTo(.9f);
            soft.assertThat(player3PitchingStats.get(4)).isEqualTo(3.0f);
            soft.assertThat(player3PitchingStats.get(5)).isEqualTo(1.25f);
        });
    }

    @Test
    void getRecentLeagueStatsTestWeighted() {
        LeagueStats leagueStats = getRecentLeagueStats(buildWeek12StatsList(), buildEvenStatsList(), League.CHAMPIONS, 2f);

        List<Float> player1HittingStats = leagueStats.getHittingStats().get("player1");
        List<Float> player1PitchingStats = leagueStats.getPitchingStats().get("player1");
        List<Float> player3HittingStats = leagueStats.getHittingStats().get("player3");
        List<Float> player3PitchingStats = leagueStats.getPitchingStats().get("player3");

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(player1HittingStats.get(0)).isEqualTo(30);
            soft.assertThat(player1HittingStats.get(1)).isEqualTo(10);
            soft.assertThat(player1HittingStats.get(4)).isEqualTo(.295f);
            soft.assertThat(player1HittingStats.get(5)).isEqualTo(.872f);

            soft.assertThat(player1PitchingStats.get(4)).isEqualTo(3.4f);
            soft.assertThat(player1PitchingStats.get(5)).isEqualTo(1.35f);

            soft.assertThat(player3HittingStats.get(0)).isEqualTo(20);
            soft.assertThat(player3HittingStats.get(2)).isEqualTo(30);
            soft.assertThat(player3HittingStats.get(4)).isEqualTo(.31f);
            soft.assertThat(player3HittingStats.get(5)).isEqualTo(.95f);

            soft.assertThat(player3PitchingStats.get(4)).isEqualTo(3.25f);
            soft.assertThat(player3PitchingStats.get(5)).isEqualTo(1.35f);

        });
    }
//    void subtractOldStatsEven() {
//        sut.calculateRecentStats(buildEvenWeek12StatsList(), buildEvenStatsList(), 8, 4);
//        verify(rotoCalculator).calculateRotoPoints(any(), hittingStatsCaptor.capture(), pitchingStatsCaptor.capture());
//
//        List<Float> hittingStats = hittingStatsCaptor.getValue().get("player1");
//        List<Float> pitchingStats = pitchingStatsCaptor.getValue().get("player1");
//        assertThat(hittingStats.get(4)).isEqualTo(.28f);
//        assertThat(hittingStats.get(5)).isEqualTo(.848f);
//        assertThat(pitchingStats.get(4)).isEqualTo(3.1f);
//        assertThat(pitchingStats.get(5)).isEqualTo(1.25f);
//    }
//
//    @Test
//    void subtractOldStatsWeighted() {
//        sut.calculateRecentStats(buildEvenWeek12StatsList(), buildEvenStatsList(), 12, 4);
//        verify(rotoCalculator).calculateRotoPoints(any(), hittingStatsCaptor.capture(), pitchingStatsCaptor.capture());
//
//        List<Float> hittingStats = hittingStatsCaptor.getValue().get("player1");
//        List<Float> pitchingStats = pitchingStatsCaptor.getValue().get("player1");
//        assertThat(hittingStats.get(4)).isEqualTo(.295f);
//        assertThat(hittingStats.get(5)).isEqualTo(.872f);
//        assertThat(pitchingStats.get(4)).isEqualTo(3.4f);
//        assertThat(pitchingStats.get(5)).isEqualTo(1.35f);
//    }
    @Test
    void subtractOldStatsWithMultipleChangedNames() {
        assertThrows(RuntimeException.class,
            () -> getRecentLeagueStats(buildWeek12StatsList(), buildVariedStatsListWith2NameChanges(), League.CHAMPIONS, 2f));
    }

}