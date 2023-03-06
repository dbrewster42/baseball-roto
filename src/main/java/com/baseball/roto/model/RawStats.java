package com.baseball.roto.model;

import com.baseball.roto.model.excel.Hitting;
import com.baseball.roto.model.excel.Pitching;
import lombok.Builder;
import lombok.Getter;

import java.util.Collection;

@Builder
@Getter
public class RawStats {
    private Collection<Hitting> hittingList;
    private Collection<Pitching> pitchingList;

    public int getNumberOfPlayers() {
        int size = hittingList.size();
        if (size != pitchingList.size()) {
            throw new RuntimeException("Stats are mismatched");
        }
        return size;
    }


//    public void sortCollections(){
//        hitting.stream().sorted((a, b) -> a.getHomeRuns()).collect(Collectors.toList());
//    }

    public Pitching matchPitching(Hitting hitting) {
        return pitchingList.stream()
            .filter(pitching -> pitching.getName().equals(hitting.getName()))
            .findAny().orElseThrow(() -> new RuntimeException("player not found"));
    }
}
