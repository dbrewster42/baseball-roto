package com.baseball.roto.controller;

import com.baseball.roto.io.ExcelReader;
import com.baseball.roto.io.ExcelWriter;
import com.baseball.roto.service.RotoService;
import org.springframework.stereotype.Controller;

@Controller
public class RotoController {
    private final RotoService rotoService;
    private final ExcelReader excelReader;
    private final ExcelWriter excelWriter;

    public RotoController(RotoService rotoService, ExcelReader excelReader, ExcelWriter excelWriter) {
        this.rotoService = rotoService;
        this.excelReader = excelReader;
        this.excelWriter = excelWriter;
    }

    public void generateRotoStats() {
        excelWriter.writeRoto(rotoService.calculateRotoScores(excelReader.readHitting(), excelReader.readPitching()));
    }
}
