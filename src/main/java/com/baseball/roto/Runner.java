package com.baseball.roto;

import com.baseball.roto.model.excel.Roto;
import com.baseball.roto.service.ExcelService;
import com.baseball.roto.service.RotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class Runner {
    private final RotoService rotoService;
    private final ExcelService excelService;
    private final String actions;
    private static final String SPLITTER = " - ";
    private static final int DEFAULT_INCLUDED_WEEKS = 4;

    public Runner(RotoService rotoService, ExcelService excelService, @Value("${actions}") String actions) {
        this.rotoService = rotoService;
        this.excelService = excelService;
        this.actions = actions;
    }

    @PostConstruct
    public void run() {
        log.info("running {}", actions);
        switch (actions.split(SPLITTER)[0]) {
            case "everything":
                totalAndRecentRoto();
                break;
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
        log.info("wrote stats");
        excelService.writeRanks(rotoService.getCategoryRanks(rotoList));
        log.info("wrote ranks");
    }

    private void totalAndRecentRoto() {
        generateRoto();
        recent();
    }

    private void recent() {
        int includedWeeks = getIncludedWeeks();
        log.info("limiting the previous calculated roto to past {} weeks", includedWeeks);
        excelService.writeRecentRoto(rotoService.limitRotoToIncludedWeeks(includedWeeks));
        log.info("wrote recent stats");
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
            return Optional.of(actions)
                .map(w ->  w.split(SPLITTER)[1])
                .map(Integer::parseInt)
                .orElseThrow();
        } catch (Exception e) {
            return DEFAULT_INCLUDED_WEEKS;
        }
    }
}
