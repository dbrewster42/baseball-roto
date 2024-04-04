package com.baseball.roto.controller;

import com.baseball.roto.configuration.RunProperties;
import com.baseball.roto.exception.BadInput;
import com.baseball.roto.model.League;
import com.baseball.roto.model.excel.Roto;
import com.baseball.roto.service.RotoService;
import com.baseball.roto.service.io.ReadWrite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;

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
            case "recent":
                generateRotoAndRecent();
                break;
            case "only recent":
                recent();
                break;
            case "change":
                changeName();
                break;
            case "delete":
                delete();
                break;
            case "delete year":
                deleteYear();
                break;
            case "rerun":
                delete();
            default:
                generateRoto();
        }
        log.info("completed");
    }


    private void generateRotoAndRecent() {
        for (League league : extractLeague()) {
            try {
                generateRoto(league);
                recent(league);
            } catch (BadInput e) {
                log.error("Could not generate roto for {}", league.name());
            }
        }
    }

    private League[] extractLeague() {
        return StringUtils.hasText(runProperties.getLeague())
            ? new League[] { getLeague() }
            : League.values();
    }

    private League getLeague() {
        League league = League.valueOf(runProperties.getLeague().trim().toUpperCase());
        if (runProperties.getNumberOfPlayers() != null) { league.setNumberOfTeams(runProperties.getNumberOfPlayers()); }
        return league;
    }

    private void generateRoto() {
        for (League league : extractLeague()) {
            try {
                generateRoto(league);
            } catch (BadInput e) {
                log.error("Could not generate roto for {}", league.name());
            }
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
            try {
                recent(league);
            } catch (BadInput e) {
                log.error("Could not generate recent roto for {}", league.name());
            }
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
    private void deleteYear() {
        for (League league : extractLeague()) {
            rotoService.deleteAllStatsFor(league);
        }
    }

    private void changeName() {
        log.info("changing {} to new name of {}", runProperties.getOldName(), runProperties.getNewName());
        rotoService.updatePlayerName(runProperties.getOldName(), runProperties.getNewName());
    }

    private int getIncludedWeeks() {
        try {
            return runProperties.getWeeks();
        } catch (Exception e) {
            log.info("error setting weeks to {}. Will use default", runProperties.getWeeks());
            return DEFAULT_INCLUDED_WEEKS;
        }
    }
}
