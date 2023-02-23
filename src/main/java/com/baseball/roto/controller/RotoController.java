package com.baseball.roto.controller;

import com.baseball.roto.model.excel.Roto;
import com.baseball.roto.service.ExcelService;
import com.baseball.roto.service.RotoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RotoController {
    private final RotoService rotoService;
    private final ExcelService excelService;
    @Value("${week}") private boolean calculatePastMonth;

    public RotoController(RotoService rotoService, ExcelService excelService) {
        this.rotoService = rotoService;
        this.excelService = excelService;
    }

    public void generateRotoStats() {
        List<Roto> rotoList = rotoService.calculateRoto(excelService.readHitting(), excelService.readPitching());
        excelService.writeRoto(rotoList);
        excelService.writeRanks(rotoService.rankCategories(rotoList));
        if (calculatePastMonth) {
            excelService.writeLastMonth(rotoService.calculateLastMonth());
        }
    }
}
