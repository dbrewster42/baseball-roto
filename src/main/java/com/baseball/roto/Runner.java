package com.baseball.roto;

import com.baseball.roto.service.RotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class Runner {
    @Autowired private RotoService rotoService;

    @PostConstruct
    public void postConstruct() {
        log.info("PostConstruct");
        rotoService.printRoto();
    }
}
