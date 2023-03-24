package com.baseball.roto;

import com.baseball.roto.configuration.LeagueConfiguration;
import com.baseball.roto.controller.RotoController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class Runner {
    @Autowired private RotoController rotoController;
    @Autowired private ApplicationContext applicationContext;
    @Value("${actions}") private String actions;

    @PostConstruct
    public void run() {
        switch (actions.split(" - ")[0]) {
            case "run both":
                runChampionsThenPSD();
            case "plus":
                standardPlus();
                break;
            case "change":
                changeName(actions.split(" - ")[1].split(","));
                break;
            case "delete":
                delete();
                break;
            case "rerun":
                delete();
            default:
                standard();
        }
    }

    private void runChampionsThenPSD() {
        standard();
        DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) applicationContext.getAutowireCapableBeanFactory();
        registry.destroySingleton("League");
        registry.registerSingleton("League", new LeagueConfiguration().league("PSD"));
        standard();
    }

    private void standard() {
        rotoController.writeRoto();
        log.info("wrote stats");
    }

    private void standardPlus() {
        standard();
        rotoController.limitCalculatedRotoToIncludedWeeks(4);
        log.info("wrote recent stats");
    }

    private void delete() {
        log.info("deleting");
        rotoController.deleteLastWeek();
    }

    private void changeName(String[] names) {
        log.info("changing {} to {}", names[0], names[1]);
        rotoController.updateName(names[0], names[1]);
    }
}
