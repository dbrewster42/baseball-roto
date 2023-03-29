package com.baseball.roto.controller;

import com.baseball.roto.model.League;
import com.baseball.roto.model.entity.ChampStats;
import com.baseball.roto.model.entity.PsdStats;
import com.baseball.roto.model.excel.Roto;
import com.baseball.roto.service.ExcelService;
import com.baseball.roto.service.RankService;
import com.baseball.roto.service.RotoService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RotoController {
    private final RotoService<ChampStats> champService;
    private final RotoService<PsdStats> psdService;
    private final ExcelService excelService;
    private final RankService rankService;

    public RotoController(RotoService<ChampStats> champService, RotoService<PsdStats> psdService, ExcelService excelService, RankService rankService) {
        this.champService = champService;
        this.psdService = psdService;
        this.excelService = excelService;
        this.rankService = rankService;
    }

    public void runStandardRotoForChampAndPsd() {
        writeChampRoto();
        writePsdRoto();
//        writeRoto();
//        champService.switchLeague(League.PSD);
//        writeRoto();
    }

    public void runTotalAndRecentRotoForChampAndPsd(int includedWeeks) {
        writeChampRoto();
        limitCalculatedRotoToIncludedWeeks(includedWeeks);
        psdService.switchLeague(League.PSD);
        writePsdRoto();
        limitCalculatedRotoToIncludedWeeks(includedWeeks);
    }

    public void writeChampRoto() {
        List<Roto> rotoList = champService.calculateRoto(excelService.readStats());
        excelService.writeRoto(rotoList);
        excelService.writeRanks(rankService.getCategoryRanks(rotoList));
    }

    public void writePsdRoto() {
        List<Roto> rotoList = psdService.calculateRoto(excelService.readStats());
        excelService.writeRoto(rotoList);
        excelService.writeRanks(rankService.getCategoryRanks(rotoList));
    }

    public void writeTotalAndRecentRoto(int includedWeeks) {
        writeChampRoto();
        limitCalculatedRotoToIncludedWeeks(includedWeeks);
    }
    public void limitCalculatedRotoToIncludedWeeks(int includedWeeks) {
        excelService.writeRecentRoto(champService.limitRotoToIncludedWeeks(includedWeeks));
    }

    public void deleteLastWeek() {
        champService.deleteThisWeeksStats();
    }

    public void changeName(String oldName, String newName) {
        champService.updatePlayerName(oldName, newName);
    }
}
