package com.baseball.roto.controller;

import com.baseball.roto.model.Roto;
import com.baseball.roto.service.ExcelService;
import com.baseball.roto.service.RankService;
import com.baseball.roto.service.RotoService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RotoController {
    private final RotoService rotoService;
    private final ExcelService excelService;
    private final RankService rankService;

    public RotoController(RotoService rotoService, ExcelService excelService, RankService rankService) {
        this.rotoService = rotoService;
        this.excelService = excelService;
        this.rankService = rankService;
    }

    public void generateRotoStats() {
        List<Roto> rotoList = rotoService.calculateRoto(excelService.readHitting(), excelService.readPitching());
        excelService.writeRoto(rotoList);
        excelService.writeRanks(rankService.convertToRanks(rotoList));
    }
}
