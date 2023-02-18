package com.baseball.roto.service;

import com.baseball.roto.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class ChangeService {
    public void calculateChanges(Collection<Player> lastWeeksRanks, List<Player> finalPlayerRanks){
        List<Player> unmatchedPlayers = new ArrayList<>();
        for (Player player : finalPlayerRanks){
            lastWeeksRanks.stream().filter(oldPlayer -> oldPlayer.getName().equals(player.getName())).findAny()
                .ifPresentOrElse(
                    oldPlayer -> calculateChangeInPlayer(player, oldPlayer),
                    () -> unmatchedPlayers.add(player));
        }
        if (unmatchedPlayers.size() == 1){
            resolveUnmatchedPlayer(unmatchedPlayers.get(0), lastWeeksRanks);
        }
    }

    private void calculateChangeInPlayer(Player player, Player oldPlayer){
        player.setTotal_change(player.getTotal() - oldPlayer.getTotal());
        player.setHitting_change(player.getHitting() - oldPlayer.getHitting());
        player.setPitching_change(player.getPitching() - oldPlayer.getPitching());
        oldPlayer.setTotal_change(1);
    }

    private void resolveUnmatchedPlayer(Player unmatchedPlayer, Collection<Player> lastWeeksRanks){
        lastWeeksRanks.stream().filter(v -> v.getTotal_change() == .11).findAny()
            .ifPresent(o -> calculateChangeInPlayer(unmatchedPlayer, o));
    }
//    private void resolveUnmatchedPlayer(List<Player> finalPlayerRanks, List<Player> lastWeeksRanks) {
//        for (Player oldPlayer : lastWeeksRanks) {
//            if ()
//        }
//    }
}
