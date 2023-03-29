package com.baseball.roto.controller;

import com.baseball.roto.model.excel.Roto;
import com.baseball.roto.service.ExcelService;
import com.baseball.roto.service.RankService;
import com.baseball.roto.service.RotoService;
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

    public void generateRoto() {
        List<Roto> rotoList = rotoService.calculateRoto(excelService.readStats());
        excelService.writeRoto(rotoList);
        excelService.writeRanks(rankService.getCategoryRanks(rotoList));
    }

    public void writeTotalAndRecentRoto(int includedWeeks) {
        generateRoto();
        limitCalculatedRotoToIncludedWeeks(includedWeeks);
    }
    public void limitCalculatedRotoToIncludedWeeks(int includedWeeks) {
        excelService.writeRecentRoto(rotoService.limitRotoToIncludedWeeks(includedWeeks));
    }

    public void deleteLastWeek() {
        rotoService.deleteThisWeeksStats();
    }

    public void changeName(String oldName, String newName) {
        rotoService.updatePlayerName(oldName, newName);
    }
}
