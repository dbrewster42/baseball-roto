package com.baseball.roto;

import com.baseball.roto.controller.RotoController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
@Slf4j
public class Runner {
    private final RotoController rotoController;
    private final String actions;
    private static final String BREAKER = " - ";

    public Runner(RotoController rotoController, @Value("${actions}") String actions) {
        this.rotoController = rotoController;
        this.actions = actions;
    }

    @PostConstruct
    public void run() {
        log.info("running {}", actions);
        switch (actions.split(BREAKER)[0]) {
            case "premium":
                standardPlus();
                break;
            case "recent":
                recent(actions);
                break;
            case "change":
                changeName(actions.split(BREAKER)[1].split(","));
                break;
            case "delete":
                delete();
                break;
            case "rerun":
                delete();
            default:
                standard();
        }
        log.info("completed");
    }

    private void standard() {
        rotoController.generateRoto();
        log.info("wrote stats");
    }

    private void standardPlus() {
        rotoController.writeTotalAndRecentRoto(4);
        log.info("wrote recent stats");
    }

    private void delete() {
        log.info("deleting");
        rotoController.deleteLastWeek();
    }

    private void changeName(String[] names) {
        log.info("changing {} to {}", names[0], names[1]);
        rotoController.changeName(names[0], names[1]);
    }

    private void recent(String weeks) {
        log.info("running only recent roto for already calculated week");
        rotoController.limitCalculatedRotoToIncludedWeeks(convertToInt(weeks));
    }

    private int convertToInt(String weeks) {
        return Optional.of(weeks)
            .filter(w -> w.split(BREAKER).length > 1)
            .map(w ->  w.split(BREAKER)[1])
            .map(Integer::parseInt)
            .orElse(4);
    }
}
