package com.baseball.roto.mother;

import com.baseball.roto.model.excel.Roto;

import java.util.ArrayList;
import java.util.List;

public class RotoMother {
    public static List<Roto> buildRotoListWithHitting() {
        List<Roto> rotoList = new ArrayList<>();
        rotoList.add(buildRotoWithHitting("name1", 30));
        rotoList.add(buildRotoWithHitting("name2", 35));
        rotoList.add(buildRotoWithHitting("name3", 35));
        rotoList.add(buildRotoWithHitting("name4", 20));
        rotoList.add(buildRotoWithHitting("name5", 20));
        rotoList.add(buildRotoWithHitting("name6", 20));
        rotoList.add(buildRotoWithHitting("name7", 20));
        rotoList.add(buildRotoWithHitting("name8", 50));
        rotoList.add(buildRotoWithHitting("name9", 40));
        rotoList.add(buildRotoWithHitting("name10", 40));
        rotoList.add(buildRotoWithHitting("name11", 40));
        rotoList.add(buildRotoWithHitting("name12", 40.5f));

        return rotoList;
    }

        public static Roto buildRotoWithHitting(String name, float hitting) {
        Roto roto = new Roto();
        roto.setName(name);
        roto.setHitting(hitting);
        return roto;
    }
}
