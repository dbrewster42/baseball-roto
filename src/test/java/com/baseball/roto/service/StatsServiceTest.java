package com.baseball.roto.service;

import com.baseball.roto.mapper.StatsMapper;
import com.baseball.roto.model.excel.Roto;
import com.baseball.roto.repository.StatsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static com.baseball.roto.mother.RotoMother.buildRotoListWithHitting;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {
    @Mock StatsRepository repository;
    @Mock StatsMapper statsMapper;
    @Mock
    RotoCalculator statCalculator;
    @InjectMocks
    StatsService sut;

    @Test
    void calculateRoto() {
    }

    @Test
    void rankCategories() {
    }

    @Test
    void rankByGetter() {
        List<Roto> testList = buildRotoListWithHitting().stream()
            .sorted((o1, o2) -> Float.compare(o2.getHitting(), o1.getHitting()))
            .collect(Collectors.toList());
        List<Roto> actual = sut.rankByGetter(testList, Roto::getHitting);
        
        assertThat(actual.stream().map(Roto::getRank).collect(Collectors.toList()))
            .containsExactly(1f, 2f, 4f, 4f, 4f, 6.5f, 6.5f, 8f, 10.5f, 10.5f, 10.5f, 10.5f);

        List<Float> expected = List.of(1f, 2f, 4f, 4f, 4f, 6.5f, 6.5f, 8f, 10.5f, 10.5f, 10.5f, 10.5f);
        List<Float> actualRanks = sut.rankByGetter(testList, Roto::getHitting).stream().map(Roto::getRank).collect(Collectors.toList());

        assertThat(actualRanks).isEqualTo(expected);
    }
}