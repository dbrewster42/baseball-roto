package com.baseball.roto.service;

import com.baseball.roto.model.Rank;
import com.baseball.roto.model.Roto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RankService {
    public List<Rank> convertToRanks(List<Roto> rotoList) {
        List<Roto> sortedHitters = sortByGetter(rotoList, Roto::getHitting);
        List<Roto> sortedPitchers = sortByGetter(rotoList, Roto::getPitching);

        List<Rank> ranks = new ArrayList<>();
        for (int i = 0; i < sortedHitters.size(); i++) {
            ranks.add(new Rank(i + 1, sortedHitters.get(i), sortedPitchers.get(i)));
        }
        return ranks;
    }

    private List<Roto> sortByGetter(List<Roto> players, Function<Roto, Float> getter){
        return players.stream()
            .sorted((o1, o2) -> Float.compare(getter.apply(o2), getter.apply(o1)))
            .collect(Collectors.toList());
    }

}
