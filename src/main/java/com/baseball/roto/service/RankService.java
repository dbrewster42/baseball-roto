package com.baseball.roto.service;

import com.baseball.roto.model.excel.CategoryRank;
import com.baseball.roto.model.excel.Roto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RankService {
    public List<CategoryRank> getCategoryRanks(List<Roto> rotoList) {
        List<CategoryRank> categoryRanks = rankByGetter(rotoList, Roto::getHitting)
            .stream()
            .map(CategoryRank::new)
            .collect(Collectors.toList());

        List<Roto> sortedPitchers = rankByGetter(rotoList, Roto::getPitching);
        IntStream.range(0, rotoList.size()).forEach(i -> categoryRanks.get(i).setPitchingCategories(sortedPitchers.get(i)));
        return categoryRanks;
    }

    public List<Roto> rankRoto(List<Roto> rotoList) {
        return rankByGetter(rotoList, Roto::getTotal);
    }

    private List<Roto> rankByGetter(List<Roto> rotoList, Function<Roto, Float> getter){
        rotoList.sort((o1, o2) -> Float.compare(getter.apply(o2), getter.apply(o1)));
        for (int i = 0; i < rotoList.size(); i++){
            float rank = i + 1;
            int tiesCount = 1;
            while (i + tiesCount < rotoList.size() && getter.apply(rotoList.get(i)).equals(getter.apply(rotoList.get(i + tiesCount)))){
                rank += .5;
                tiesCount++;
            }
            rotoList.get(i).setRank(rank);
            while (tiesCount > 1){ //set ranks for rest of tied players
                i++;
                rotoList.get(i).setRank(rank);
                tiesCount--;
            }
        }
        return rotoList;
    }
}
