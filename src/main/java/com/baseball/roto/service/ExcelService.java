package com.baseball.roto.service;

import com.baseball.roto.model.RawStats;
import com.baseball.roto.model.excel.CategoryRank;
import com.baseball.roto.model.excel.Hitting;
import com.baseball.roto.model.excel.Pitching;
import com.baseball.roto.model.excel.Roto;
import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.options.XceliteOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@Slf4j
public class ExcelService {
    private final File outputFile;
    private final Xcelite statsXcel;
    private final Xcelite rotoXcel;
    private final String league;
    private final static String FILE_SUFFIX = ".xlsx";

    public ExcelService(@Value("${folder}") String folder, @Value("${league}") String league) {
        this.statsXcel = new Xcelite(new File(folder + "stats" + FILE_SUFFIX));
        this.rotoXcel = new Xcelite();
        this.outputFile = new File(folder + league + FILE_SUFFIX);
        this.league = league;
    }

    public RawStats readStats() {
        return new RawStats(readHitting(), readPitching());
    }
    private List<Hitting> readHitting() {
        return (List<Hitting>) statsXcel.getSheet(league + " Hitting").getBeanReader(Hitting.class).read();
    }
    private List<Pitching> readPitching() {
        return (List<Pitching>) statsXcel.getSheet(league + " Pitching").getBeanReader(Pitching.class).read();
    }

    public void writeRoto(List<Roto> rotoList){
        writeRoto(rotoList, league);
    }
    public void writeRecentRoto(List<Roto> rotoList) {
        rotoXcel.setOptions(null);
        writeRoto(rotoList, "Recent " + league);
    }
    public void writeRoto(List<Roto> rotoList, String sheetName){
        log.info("writing results");
        rotoXcel.createSheet(sheetName).getBeanWriter(Roto.class).write(rotoList);
        rotoXcel.write(outputFile);
        log.info("done");
    }

    public void writeRanks(List<CategoryRank> categoryRanks) {
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(categoryRanks.size() + 2);
        rotoXcel.setOptions(options);

        rotoXcel.getSheet(league).getBeanWriter(CategoryRank.class).write(categoryRanks);
        rotoXcel.write(outputFile);
    }
}
