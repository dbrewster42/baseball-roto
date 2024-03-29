package com.baseball.roto.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StatsQualifierTest {

    RotoCalculator rotoCalculator = mock(RotoCalculator.class);
    @Captor
    ArgumentCaptor<Map<String, List<Float>>> hittingStatsCaptor;
    @Captor
    ArgumentCaptor<Map<String, List<Float>>> pitchingStatsCaptor;
//    StatsSubtracter sut = new StatsSubtracter(LeagueSettings.CHAMPIONS, rotoCalculator);
//todo move to StatsSubtraction
//    @Test
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
//    @Test
//    void subtractOldStatsWithMultipleChangedNames() {
//        assertThrows(RuntimeException.class, () -> sut.calculateRecentStats(buildEvenStatsList(), buildVariedStatsListWith2NameChanges(), 5, 4));
//    }
}
