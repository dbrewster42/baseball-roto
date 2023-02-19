package com.baseball.roto.service;

import com.baseball.roto.model.Roto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class ChangeService {
    public void calculateChanges(Collection<Roto> lastWeeksRanks, List<Roto> currentRoto){
        List<Roto> unmatchedRotos = new ArrayList<>();
        for (Roto roto : currentRoto){
            lastWeeksRanks.stream().filter(oldRoto -> oldRoto.getName().equals(roto.getName())).findAny()
                .ifPresentOrElse(
                    oldRoto -> calculateChangeInPlayer(roto, oldRoto),
                    () -> unmatchedRotos.add(roto));
        }
        if (unmatchedRotos.size() == 1){
            resolveUnmatchedPlayer(unmatchedRotos.get(0), lastWeeksRanks);
        }
    }

    private void calculateChangeInPlayer(Roto roto, Roto oldRoto){
        roto.setTotal_change(roto.getTotal() - oldRoto.getTotal());
        roto.setHitting_change(roto.getHitting() - oldRoto.getHitting());
        roto.setPitching_change(roto.getPitching() - oldRoto.getPitching());
        oldRoto.setTotal_change(1);
    }

    private void resolveUnmatchedPlayer(Roto unmatchedPlayer, Collection<Roto> lastWeeksRanks){
        lastWeeksRanks.stream().filter(v -> v.getTotal_change() == .11).findAny()
            .ifPresent(o -> calculateChangeInPlayer(unmatchedPlayer, o));
    }
//    private void resolveUnmatchedPlayer(List<Player> finalPlayerRanks, List<Player> lastWeeksRanks) {
//        for (Player oldPlayer : lastWeeksRanks) {
//            if ()
//        }
//    }
}
