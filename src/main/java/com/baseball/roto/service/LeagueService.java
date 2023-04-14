package com.baseball.roto.service;

import com.baseball.roto.model.League;
import com.baseball.roto.repository.StatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class LeagueService {
    private League league;
    @Autowired
    private ApplicationContext applicationContext;


    public StatsRepository repository() {
        return applicationContext.getBean(league.getRepository());
    }

    public void setLeague(League league) {
        this.league = league;
    }
    public League getLeague() {
        return league;
    }
    public String getLeagueName() {
        return league.name();
    }
}
