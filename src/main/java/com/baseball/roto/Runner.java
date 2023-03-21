package com.baseball.roto;

import com.baseball.roto.controller.RotoController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class Runner {
    @Autowired private RotoController rotoController;

    @PostConstruct
    public void run() {
        log.info("PostConstruct");
        rotoController.writeRotoToExcel();
        log.info("wrote stats");
    }

//    @PostConstruct
//    public void test() {
//        log.info("PostConstruct");
////        log.info(rotoController.updateName("Old Fart", "Mac"));
//        rotoController.deleteLastWeek();
//    }
}
