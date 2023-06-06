package com.baseball.roto.controller;

import com.baseball.roto.model.League;
import com.baseball.roto.model.excel.Roto;
import com.baseball.roto.service.ExcelService;
import com.baseball.roto.service.RotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

import static java.lang.Integer.parseInt;

@Component
@Slf4j
public class RotoRunner {
    private static final String DELIMITER = "-";
    private static final int DEFAULT_INCLUDED_WEEKS = 4;
    private final RotoService rotoService;
    private final ExcelService excelService;
    private final String actions;


    public RotoRunner(RotoService rotoService, ExcelService excelService, @Value("${actions}") String actions) {
        this.rotoService = rotoService;
        this.excelService = excelService;
        this.actions = actions;
    }

    @PostConstruct
    public void run() {
        log.info("running [{}]", actions);
        switch (actions.split(DELIMITER)[0]) {
            case "everything":
                generateEverything();
                break;
            case "recent":
                generateAllRecent();
                break;
            case "roto":
                generateRoto(League.valueOf(actions.split(DELIMITER)[1]));
                break;
            case "delete":
                delete(League.valueOf(actions.split(DELIMITER)[1]));
                break;
            case "change":
                changeName(actions.split(DELIMITER)[1].split(","));
                break;
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

    private void generateEverything() {
        log.info("running standard and recent roto");
        for (League league : League.values()) {
            generateRoto(league);
            recent();
        }
    }

    private void generateAllRoto() {
        log.info("running standard roto for all leagues");
        rotoService.setLeague(League.CHAMPIONS);
        for (League league : League.values()) {
            generateRoto(league);
        }
    }
    private void generateRoto(League league) {
        rotoService.setLeague(league);
        List<Roto> rotoList = rotoService.calculateRoto(excelService.readStats());
        log.info("calculated roto for {}", league.name());
        excelService.writeRoto(rotoList);
        excelService.writeRanks(rotoService.getCategoryRanks(rotoList));
    }

    private void generateAllRecent() {
        log.info("running recent roto for all leagues");
        for (League league : League.values()) {
            rotoService.setLeague(league);
            recent();
        }
    }
    private void recent() {
        int includedWeeks = getIncludedWeeks();
        log.info("limiting the previous calculated roto to past {} weeks", includedWeeks);
        excelService.writeRecentRoto(rotoService.limitRotoToIncludedWeeks(includedWeeks));
    }

    private void deleteAll() {
        for (League league : League.values()) {
            rotoService.deleteLatestWeeksStatsFor(league);
        }
    }

    private void changeName(String[] names) {
        log.info("changing {} to {}", names[0], names[1]);
        rotoService.updatePlayerName(names[0], names[1]);
    }

    private void delete(League league) {
        log.info("deleting this weeks stats");
        rotoService.deleteLatestWeeksStatsFor(league);
    }

    private int getIncludedWeeks() {
        try {
            return parseInt(actions.split(DELIMITER)[1]);
        } catch (Exception e) {
            return DEFAULT_INCLUDED_WEEKS;
        }
    }
}
