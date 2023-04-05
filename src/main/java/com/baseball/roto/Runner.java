package com.baseball.roto;

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
public class Runner {
    private static final String SPLITTER = " - ";
    private static final int DEFAULT_INCLUDED_WEEKS = 4;
    private final RotoService rotoService;
    private final ExcelService excelService;
    private final String actions;

    public Runner(RotoService rotoService, ExcelService excelService, @Value("${actions}") String actions) {
        this.rotoService = rotoService;
        this.excelService = excelService;
        this.actions = actions;
    }

    @PostConstruct
    public void run() {
        log.info("running [{}]", actions);
        switch (actions.split(SPLITTER)[0]) {
            case "everything":
                generateRoto();
            case "recent":
                recent();
                break;
            case "change":
                changeName(actions.split(SPLITTER)[1].split(","));
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

    private void generateRoto() {
        log.info("running standard roto");
        List<Roto> rotoList = rotoService.calculateRoto(excelService.readStats());
        log.info("calculated roto");
        excelService.writeRoto(rotoList);
        excelService.writeRanks(rotoService.getCategoryRanks(rotoList));
    }

    private void recent() {
        int includedWeeks = getIncludedWeeks();
        log.info("limiting the previous calculated roto to past {} weeks", includedWeeks);
        excelService.writeRecentRoto(rotoService.limitRotoToIncludedWeeks(includedWeeks));
    }

    private void changeName(String[] names) {
        log.info("changing {} to {}", names[0], names[1]);
        rotoService.updatePlayerName(names[0], names[1]);
    }

    private void delete() {
        log.info("deleting this weeks stats");
        rotoService.deleteThisWeeksStats();
    }

    private int getIncludedWeeks() {
        try {
            return parseInt(actions.split(SPLITTER)[1]);
        } catch (Exception e) {
            return DEFAULT_INCLUDED_WEEKS;
        }
    }
}
