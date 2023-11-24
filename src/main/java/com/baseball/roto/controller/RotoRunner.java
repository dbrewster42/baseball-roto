package com.baseball.roto.controller;

import com.baseball.roto.configuration.RunProperties;
import com.baseball.roto.model.League;
import com.baseball.roto.model.excel.Roto;
import com.baseball.roto.service.RotoService;
import com.baseball.roto.service.io.ReadWrite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;

import static java.lang.Integer.parseInt;

@Component
@Slf4j
public class RotoRunner {
    private static final int DEFAULT_INCLUDED_WEEKS = 4;
    private final RotoService rotoService;
    private final ReadWrite readWrite;
    private final RunProperties runProperties;


    public RotoRunner(RotoService rotoService, ReadWrite excelService, RunProperties runProperties) {
        this.rotoService = rotoService;
        this.readWrite = excelService;
        this.runProperties = runProperties;
    }

    @PostConstruct
    public void run() {
        log.info("running [{}]", runProperties);
        switch (runProperties.getAction()) {
            case "roto":
                generateRoto();
                break;
            case "everything":
                generateEverything();
                break;
            case "recent":
                recent();
                break;
            case "change":
                changeName();
                break;
            case "delete":
                delete();
                break;
            case "rerun":
                delete();
            default:
                generateRoto();
        }
        log.info("completed");
    }


    private void generateEverything() {
        for (League league : extractLeague()) {
            generateRoto(league);
            recent(league);
        }
    }

    private League[] extractLeague() {
        return StringUtils.hasText(runProperties.getLeague())
            ? new League[] { League.valueOf(runProperties.getLeague().toUpperCase()) }
            : League.values();
    }

    private void generateRoto() {
        for (League league : extractLeague()) {
            generateRoto(league);
        }
    }

    private void generateRoto(League league) {
        log.info("calculating roto for {}", league.name());
        rotoService.setLeague(league);
        List<Roto> rotoList = rotoService.calculateRoto(readWrite.readStats());
        readWrite.writeRoto(rotoList);
        readWrite.writeRanks(rotoService.getCategoryRanks(rotoList));
    }

    private void recent() {
        for (League league : extractLeague()) {
            recent(league);
        }
    }
    private void recent(League league) {
        rotoService.setLeague(league);
        int includedWeeks = getIncludedWeeks();
        log.info("running recent roto for {} for past {} weeks", league, includedWeeks);
        List<Roto> rotoList = rotoService.limitRotoToIncludedWeeks(includedWeeks);
        readWrite.writeRecentRoto(rotoList);
        readWrite.writeRanks(rotoService.getCategoryRanks(rotoList));
    }

    private void delete() {
        for (League league : extractLeague()) {
            rotoService.deleteLatestWeeksStatsFor(league);
        }
    }

    private void changeName() {
        log.info("changing {} to new name of {}", runProperties.getOldName(), runProperties.getNewName());
        rotoService.updatePlayerName(runProperties.getOldName(), runProperties.getNewName());
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
