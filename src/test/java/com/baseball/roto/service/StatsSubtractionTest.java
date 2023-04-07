package com.baseball.roto.service;

import com.baseball.roto.exception.BadInput;
import com.baseball.roto.model.League;
import com.baseball.roto.model.LeagueStats;
import com.baseball.roto.model.entity.ChampStats;
import com.baseball.roto.model.entity.Stats;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static com.baseball.roto.mother.StatsMother.buildEvenStatsList;
import static com.baseball.roto.mother.StatsMother.buildVariedStatsListWith2NameChanges;
import static com.baseball.roto.mother.StatsMother.buildWeek12StatsList;
import static com.baseball.roto.service.StatsSubtraction.getRecentLeagueStats;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StatsSubtractionTest {

    @Test
    void getRecentLeagueStatsTest() {
        LeagueStats leagueStats = getRecentLeagueStats(cast(buildWeek12StatsList()), cast(buildEvenStatsList()), League.CHAMPIONS, 1f);

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
        LeagueStats leagueStats = getRecentLeagueStats(cast(buildWeek12StatsList()), cast(buildEvenStatsList()), League.CHAMPIONS, 2f);

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

    @Test
    void getRecentLeagueStatsTestWeightedDecimal() {
        LeagueStats leagueStats = getRecentLeagueStats(cast(buildWeek12StatsList()), cast(buildEvenStatsList()), League.CHAMPIONS, 4/3f);

        List<Float> player1HittingStats = leagueStats.getHittingStats().get("player1");
        List<Float> player3HittingStats = leagueStats.getHittingStats().get("player3");
        //week 8 stats .250 AVG and .800 OPS
        SoftAssertions.assertSoftly(soft -> {
            //week 12 player1 stats .265 .824
            soft.assertThat(player1HittingStats.get(4)).isEqualTo(.285f);
            soft.assertThat(player1HittingStats.get(5)).isEqualTo(.856f);
            //week 12 player3 stats .270 .850
            soft.assertThat(player3HittingStats.get(4)).isEqualTo(.297f);
            soft.assertThat(player3HittingStats.get(5)).isEqualTo(.917f);
        });
    }

    @Test
    void subtractOldStatsWithMultipleChangedNames() {
        assertThrows(BadInput.class,
            () -> getRecentLeagueStats(cast(buildWeek12StatsList()), cast(buildVariedStatsListWith2NameChanges()), League.CHAMPIONS, 2f));
    }


    private List<Stats> cast(List<ChampStats> champStats) {
        return champStats.stream().map(s -> (Stats) s).collect(Collectors.toList());
    }
}