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
    public void writeRoto() {
        List<Roto> rotoList = rotoService.calculateRoto(excelService.readStats());
        excelService.writeRoto(rotoList);
        excelService.writeRanks(rankService.getCategoryRanks(rotoList));
    }

    @PostMapping("/calculate{includedWeeks}weeks")
    public void limitCalculatedRotoToIncludedWeeks(@PathVariable int includedWeeks) {
        excelService.writeRecentRoto(rotoService.limitRotoToIncludedWeeks(includedWeeks));
    }

    @DeleteMapping
    public void deleteLastWeek() {
        rotoService.deleteThisWeeksStats();
    }

    @PutMapping("/{oldName}/{newName}")
    public void updateName(@PathVariable String oldName, @PathVariable String newName) {
        rotoService.updatePlayerName(oldName, newName);
    }
}
