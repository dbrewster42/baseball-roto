package com.baseball.roto;

import com.baseball.roto.controller.RotoController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class Runner {
    @Autowired private RotoController rotoController;
    @Value("${actions}") private String actions;

    @PostConstruct
    public void run() {
        switch (actions.split(" - ")[0]) {
            case "plus":
                standardPlus();
                break;
            case "change":
                String[] names = actions.split(" - ")[1].split(",");
                changeName(names[0], names[1]);
                break;
            case "delete":
                delete();
            default:
                standard();
        }
    }

    public void standard() {
        log.info("PostConstruct");
        rotoController.writeRoto();
        log.info("wrote stats");
    }

    public void standardPlus() {
        rotoController.writeOverallAndRecentRoto(4);
        log.info("wrote recent stats");
    }

    public void delete() {
        log.info("deleting");
        rotoController.deleteLastWeek();
    }

    public void changeName(String newName, String oldName) {
        log.info("changing name");
        rotoController.updateName(newName, oldName);
    }
}
