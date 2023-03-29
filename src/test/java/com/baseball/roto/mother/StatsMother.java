package com.baseball.roto.mother;

import com.baseball.roto.model.entity.ChampStats;

import java.util.ArrayList;
import java.util.List;

public class StatsMother {

    public static List<ChampStats> buildVariedStatsList() {
        List<ChampStats> statsList = new ArrayList<>();
        int playerNumber = 1;
        statsList.add(buildStats("player" + playerNumber++, 0));
        statsList.add(buildStats("player" + playerNumber++, 2));
        statsList.add(buildStats("player" + playerNumber++, 5));
        statsList.add(buildStats("player" + playerNumber++, -2));
        statsList.add(buildStats("player" + playerNumber++, 0));
        statsList.add(buildStats("player" + playerNumber++, -2));
        statsList.add(buildStats("player" + playerNumber++, 0));
        statsList.add(buildStats("player" + playerNumber, -1));
        return statsList;
    }

    public static List<ChampStats> buildEvenStatsList() {
        int playerNumber = 1;
        List<ChampStats> statsList = new ArrayList<>();
        statsList.add(buildWeek8Stats("player" + playerNumber++));
        statsList.add(buildWeek8Stats("player" + playerNumber++));
        statsList.add(buildWeek8Stats("player" + playerNumber++));
        statsList.add(buildWeek8Stats("player" + playerNumber++));
        statsList.add(buildWeek8Stats("player" + playerNumber++));
        statsList.add(buildWeek8Stats("player" + playerNumber++));
        statsList.add(buildWeek8Stats("player" + playerNumber++));
        statsList.add(buildWeek8Stats("player" + playerNumber));
        return statsList;
    }

    public static List<ChampStats> buildEvenWeek12StatsList() {
        int playerNumber = 1;
        List<ChampStats> statsList = new ArrayList<>();
        statsList.add(buildWeek12Stats("player" + playerNumber++));
        statsList.add(buildWeek12Stats("player" + playerNumber++));
        statsList.add(buildWeek12Stats("player" + playerNumber++));
        statsList.add(buildWeek12Stats("player" + playerNumber++));
        statsList.add(buildWeek12Stats("player" + playerNumber++));
        statsList.add(buildWeek12Stats("player" + playerNumber++));
        statsList.add(buildWeek12Stats("player" + playerNumber++));
        statsList.add(buildWeek12Stats("player" + playerNumber));
        return statsList;
    }

    public static List<ChampStats> buildWeek12StatsList() {
        List<ChampStats> statsList = buildEvenWeek12StatsList();
        statsList.get(0).setRuns(80);
        statsList.get(1).setRbis(80);
        statsList.get(2).setAvg(.270f);
        statsList.get(2).setOps(.850f);
        statsList.get(2).setEra(2.75f);
        statsList.get(2).setQualityStarts(15);
        return statsList;
    }


    public static ChampStats buildWeek8Stats(String name) {
        ChampStats stats = new ChampStats();
        stats.setName(name);

        stats.setEra(2.5f);
        stats.setWhip(1.05f);
        stats.setStrikeouts(50);
        stats.setWins(10);
        stats.setQualityStarts(10);
        stats.setNetSaves(7);

        stats.setRuns(50);
        stats.setHomeRuns(19);
        stats.setRbis(55);
        stats.setAvg(.25f);
        stats.setOps(.8f);
        stats.setSbs(9);
        return stats;
    }
    public static ChampStats buildWeek12Stats(String name) {
        ChampStats stats = new ChampStats();
        stats.setName(name);

        stats.setEra(2.8f);
        stats.setWhip(1.15f);
        stats.setStrikeouts(100);
        stats.setWins(16);
        stats.setQualityStarts(18);
        stats.setNetSaves(8);

        stats.setRuns(70);
        stats.setHomeRuns(29);
        stats.setRbis(85);
        stats.setAvg(.265f);
        stats.setOps(.824f);
        stats.setSbs(12);
        return stats;
    }

    public static ChampStats buildStats(String name, int diff) {
        ChampStats stats = new ChampStats();
        stats.setName(name);

//        stats.setTotal(50);
//        stats.setHitting(25);
//        stats.setPitching(25);
//        stats.setWeek(1);

        stats.setEra(2.5f - diff / 10f);
        stats.setWhip(1.05f - diff / 10f);
        stats.setStrikeouts(25 + diff);
        stats.setWins(5 + diff);
        stats.setQualityStarts(3 + diff);
        stats.setNetSaves(4 + diff);

        stats.setRuns(20 + diff);
        stats.setHomeRuns(8 + diff);
        stats.setRbis(28 + diff);
        stats.setAvg(.25f + diff / 40f);
        stats.setOps(.8f + diff / 30f);
        stats.setSbs(3 + diff);
        return stats;
    }

    public static List<ChampStats> buildVariedStatsListWith1NameChange() {
        List<ChampStats> statsList = buildVariedStatsList();
        statsList.get(1).setName("problemCauser");
        return statsList;
    }
    public static List<ChampStats> buildVariedStatsListWith2NameChanges() {
        List<ChampStats> statsList = buildVariedStatsList();
        statsList.get(1).setName("problemCauser");
        statsList.get(3).setName("rocking the boat");
        return statsList;
    }
}
