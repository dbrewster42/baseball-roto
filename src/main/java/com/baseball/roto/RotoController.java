package com.baseball.roto;

import com.baseball.roto.io.ExcelReader;
import com.baseball.roto.io.ExcelWriter;
import com.baseball.roto.model.Roto;
import com.baseball.roto.service.ChangeService;
import com.baseball.roto.service.RotoService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RotoController {
    private final RotoService rotoService;
    private final ChangeService changeService;
    private final ExcelReader excelReader;
    private final ExcelWriter excelWriter;

    public RotoController(RotoService rotoService, ChangeService changeService, ExcelReader excelReader, ExcelWriter excelWriter) {
        this.rotoService = rotoService;
        this.changeService = changeService;
        this.excelReader = excelReader;
        this.excelWriter = excelWriter;
    }

    public void generateRotoStats() {
        List<Roto> rotos = rotoService.calculateRotoScores(excelReader.readHitting(), excelReader.readPitching());
        changeService.calculateChanges(excelReader.readRoto(), rotos);
        excelWriter.writeRoto(rotos);
    }
}
