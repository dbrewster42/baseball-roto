package com.baseball.roto.service;

import com.baseball.roto.model.Roto;
import com.baseball.roto.model.Stats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ChangeService {
    public List<Roto> calculateChanges(List<Stats> lastWeeksRanks, List<Roto> currentRoto){
        List<Roto> unmatchedRotos = new ArrayList<>();
        for (Roto roto : currentRoto){
            lastWeeksRanks.stream()
                .filter(oldRoto -> oldRoto.getName().equals(roto.getName())).findAny()
                .ifPresentOrElse(
                    oldRoto -> calculateChangeInPlayer(roto, oldRoto),
                    () -> unmatchedRotos.add(roto));
        }
        if (unmatchedRotos.size() == 1){
            lastWeeksRanks.stream()
                .filter(lw -> currentRoto.stream().noneMatch(roto -> lw.getName().equals(roto.getName())))
                .findAny()
                .ifPresent(lw -> calculateChangeInPlayer(unmatchedRotos.get(0), lw));
        }
        currentRoto.forEach(roto -> log.info(roto.toString()));
        return currentRoto;
    }

    private void calculateChangeInPlayer(Roto roto, Stats oldStats){;
        roto.setTotalChange(roto.getTotal() - oldStats.getTotal());
        roto.setHittingChange(roto.getHitting() - oldStats.getHitting());
        roto.setPitchingChange(roto.getPitching() - oldStats.getPitching());
    }
}
