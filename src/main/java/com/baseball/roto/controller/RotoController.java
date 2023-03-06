package com.baseball.roto.controller;

import com.baseball.roto.model.Stats;
import com.baseball.roto.model.excel.Roto;
import com.baseball.roto.service.ExcelService;
import com.baseball.roto.service.StatsService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RotoController {
    private final StatsService statsService;
    private final ExcelService excelService;

    public RotoController(StatsService statsService, ExcelService excelService) {
        this.statsService = statsService;
        this.excelService = excelService;
    }

    @PostMapping
    public void writeExcelRotoStats() {
//        List<Stats> statsList = statsService.prepareStats(excelService.readHitting(), excelService.readPitching())
        List<Roto> rotoList = statsService.calculateRoto(excelService.readStats());
        excelService.writeRoto(rotoList);
        excelService.writeRanks(statsService.rankCategories(rotoList));
    }
    @PostMapping("/{league}/{week}")
    public void writeExcelRotoStats(@PathVariable String league, @PathVariable int week) {
        List<Roto> rotoList = statsService.calculateRoto(excelService.readStats(league));
        excelService.writeRoto(rotoList);
        excelService.writeRanks(statsService.rankCategories(rotoList));
    }

    @PostMapping("/calculate{pastWeeks}weeks")
    public List<Stats> writeLastXWeeks(@PathVariable int pastWeeks) {
        List<Stats> statsList = statsService.limitStatsToPastXWeeks(pastWeeks);
        excelService.writeLastXWeeks(statsService.convertToSortedRoto(statsList));
        return statsList;
    }

    @DeleteMapping
    public void deleteLastWeek() {
        statsService.deleteLastWeek();
    }

    @PutMapping("/{newName}/{oldName}")
    public String updateName(@PathVariable String newName, @PathVariable String oldName) {
        statsService.updatePlayerName(newName, oldName);
        return "Done";
    }
}
