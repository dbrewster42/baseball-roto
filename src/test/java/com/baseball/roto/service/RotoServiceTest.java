package com.baseball.roto.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RotoServiceTest {
//    @Mock StatsRepository repository;
//    @Mock StatsMapper statsMapper;
//    @Mock
//    RotoCalculator statCalculator;
//    @InjectMocks
//    RotoService sut;

    @Test
    void calculateRoto() {
//        LeagueSettings leagueSettings = LeagueSettings.valueOf("Champions");
//        assertThat(leagueSettings.getStatColumns()).isEqualTo(6);
    }

    @Test
    void rankCategories() {
    }

//    @Test
//    @Disabled
//    void rankByGetter() {
////        RawStats rawStats = RawStats.builder().hittingList(List.of()).build();
////        sut.prepareStats() //todo set playerNo
//        List<Roto> testList = buildRotoListWithHitting().stream()
//            .sorted((o1, o2) -> Float.compare(o2.getHitting(), o1.getHitting()))
//            .collect(Collectors.toList());
//        List<Roto> actual = sut.rankByGetter(testList, Roto::getHitting);
//
//        assertThat(actual.stream().map(Roto::getRank).collect(Collectors.toList()))
//            .containsExactly(1f, 2f, 4f, 4f, 4f, 6.5f, 6.5f, 8f, 10.5f, 10.5f, 10.5f, 10.5f);
//
//        List<Float> expected = List.of(1f, 2f, 4f, 4f, 4f, 6.5f, 6.5f, 8f, 10.5f, 10.5f, 10.5f, 10.5f);
//        List<Float> actualRanks = sut.rankByGetter(testList, Roto::getHitting).stream().map(Roto::getRank).collect(Collectors.toList());
//
//        assertThat(actualRanks).isEqualTo(expected);
//    }
}