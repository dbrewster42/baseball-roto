package com.baseball.roto.controller;

import com.baseball.roto.model.excel.Roto;
import com.baseball.roto.service.ExcelService;
import com.baseball.roto.service.RankService;
import com.baseball.roto.service.RotoService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RotoController {
    private final RotoService rotoService;
    private final ExcelService excelService;
    private final RankService rankService;

    public RotoController(RotoService rotoService, ExcelService excelService, RankService rankService) {
        this.rotoService = rotoService;
        this.excelService = excelService;
        this.rankService = rankService;
    }

    @PostMapping
    public void writeRotoToExcel() {
//        List<Stats> statsList = statsService.prepareStats(excelService.readHitting(), excelService.readPitching())
        List<Roto> rotoList = rotoService.calculateRoto(excelService.readStats());
        excelService.writeRoto(rotoList);
//        excelService.writeRanks(rotoService.getCategoryRanks(rotoList));
        excelService.writeRanks(rankService.getCategoryRanks(rotoList));
    }

//    @PostMapping("/calculate{includedWeeks}weeks")
//    public List<Stats> writeLastXWeeks(@PathVariable int includedWeeks) {
//        List<Stats> statsList = rotoService.limitStatsToPastXWeeks(includedWeeks);
//        excelService.writeLastXWeeks(rotoService.convertToSortedRoto(statsList));
//        return statsList;
//    }
    @PostMapping("/calculate{includedWeeks}weeks")
    public void writeLastXWeeks(@PathVariable int includedWeeks) {
        excelService.writeLastXWeeks(rotoService.calculateRotoForPastXWeeks(includedWeeks));
    }

    @DeleteMapping
    public void deleteLastWeek() {
        rotoService.deleteLastWeek();
    }

    @PutMapping("/{newName}/{oldName}")
    public String updateName(@PathVariable String newName, @PathVariable String oldName) {
        rotoService.updatePlayerName(newName, oldName);
        return "Done";
    }
}
