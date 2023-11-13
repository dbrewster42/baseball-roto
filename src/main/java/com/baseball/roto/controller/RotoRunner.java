package com.baseball.roto.controller;

import com.baseball.roto.configuration.RunProperties;
import com.baseball.roto.model.League;
import com.baseball.roto.model.excel.Roto;
import com.baseball.roto.service.io.ExcelService;
import com.baseball.roto.service.RotoService;
import com.baseball.roto.service.io.ReadWrite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.lang.Integer.parseInt;

@Component
@Slf4j
public class RotoRunner {
    private static final String DELIMITER = "-";
    private static final int DEFAULT_INCLUDED_WEEKS = 4;
    private final RotoService rotoService;
    private final ReadWrite readWrite;
    private final RunProperties runProperties;


//    public RotoRunner(RotoService rotoService, ReadWrite excelService, @Value("${actions}") String actions) {
//        this.rotoService = rotoService;
//        this.readWrite = excelService;
//        this.actions = actions;
//    }
    public RotoRunner(RotoService rotoService, ReadWrite excelService, RunProperties runProperties) {
        this.rotoService = rotoService;
        this.readWrite = excelService;
        this.runProperties = runProperties;
    }

    @PostConstruct
    public void run() {
        log.info("running [{}]", runProperties);
        switch (runProperties.getAction()) {
            case "everything":
                generateEverything();
                break;
            case "recent":
                generateAllRecent();
                break;
//            case "roto":
//                generateRoto(League.valueOf(actions.split(DELIMITER)[1]));
//                break;
//            case "delete":
//                delete(League.valueOf(actions.split(DELIMITER)[1]));
//                break;
//            case "change":
//                changeName(actions.split(DELIMITER)[1].split(","));
//                break;
            case "deleteAll":
                deleteAll();
                break;
            case "rerun":
                deleteAll();
            default:
                generateAllRoto();
        }
        log.info("completed");
    }

    private void rotoAndRecent() {
        runActionForOneOrAllLeagues(this::generateRoto, this::generateEverything);
    }

    private void generateRoto() {
        runActionForOneOrAllLeagues(this::generateRoto, this::generateAllRoto);
//        Optional.ofNullable(runProperties.getLeague())
//            .map(League::valueOf)
//            .ifPresentOrElse(
//                this::generateRoto,
//                this::generateAllRoto
//            );
    }
    private void recent() {
        runActionForOneOrAllLeagues(this::recent, this::generateAllRecent);
    }


    private void rotoAndRecent(League league) {
        generateRoto(league);
        recent(league);
    }


    private void generateEverything() {
        log.info("running standard and recent roto");
        for (League league : League.values()) {
            generateRoto(league);
            recent(league);
        }
    }

    private void runActionForOneOrAllLeagues(Consumer<League> singleLeague, Runnable allLeagues) {
        Optional.ofNullable(runProperties.getLeague())
            .map(League::valueOf)
            .ifPresentOrElse(
                singleLeague,
                allLeagues
            );
    }

    private void generateAllRoto() {
        log.info("running standard roto for all leagues");
        for (League league : League.values()) {
            generateRoto(league);
        }
    }

    private void generateRoto(League league) {
        rotoService.setLeague(league);
        List<Roto> rotoList = rotoService.calculateRoto(readWrite.readStats());
        log.info("calculated roto for {}", league.name());
        readWrite.writeRoto(rotoList);
        readWrite.writeRanks(rotoService.getCategoryRanks(rotoList));
    }

    private void generateAllRecent() {
        log.info("running recent roto for all leagues");
        for (League league : League.values()) {
            recent(league);
        }
    }
    private void recent(League league) {
        rotoService.setLeague(league);
        int includedWeeks = getIncludedWeeks();
        log.info("limiting the previous calculated roto to past {} weeks", includedWeeks);
        List<Roto> rotoList = rotoService.limitRotoToIncludedWeeks(includedWeeks);
        readWrite.writeRecentRoto(rotoList);
        readWrite.writeRanks(rotoService.getCategoryRanks(rotoList));
    }

    private void deleteAll() {
        for (League league : League.values()) {
            rotoService.deleteLatestWeeksStatsFor(league);
        }
    }

    private void changeName(String[] names) {
        log.info("changing {} to {}", names[0], names[1]);
        rotoService.updatePlayerName(names[0].trim(), names[1].trim());
    }

    private void delete(League league) {
        log.info("deleting this weeks stats");
        rotoService.deleteLatestWeeksStatsFor(league);
    }

    private int getIncludedWeeks() {
        try {
            return parseInt(runProperties.getAction());
        } catch (Exception e) {
            log.info("error getting weeks from {}. Setting to default", runProperties.getAction());
            return DEFAULT_INCLUDED_WEEKS;
        }
    }
}
