package com.baseball.roto.service;

import com.baseball.roto.model.RawStats;
import com.baseball.roto.model.excel.Hitting;
import com.baseball.roto.model.excel.Pitching;
import com.baseball.roto.model.excel.CategoryRank;
import com.baseball.roto.model.excel.Roto;
import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.options.XceliteOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class ExcelService {
    private final File outputFile;
    private final Xcelite statsXcel;
    private final Xcelite rotoXcel;

    public ExcelService(@Value("${file.stats}") String statsFile, @Value("${file.output}") String filename) {
        this.statsXcel = new Xcelite(new File(statsFile));
        this.rotoXcel = new Xcelite();
        this.outputFile = new File(filename);
    }

    public RawStats readStats(String league) {
        return RawStats.builder()
            .hittingList(readHitting(league))
            .pitchingList(readPitching(league))
            .build();
    }
    public RawStats readStats() {
        return RawStats.builder()
            .hittingList(readHitting())
            .pitchingList(readPitching())
            .build();
    }
    private Collection<Hitting> readHitting(String league) {
        return statsXcel.getSheet(league + " Hitting").getBeanReader(Hitting.class).read();
    }
    private Collection<Pitching> readPitching(String league) {
        return statsXcel.getSheet(league + " Pitching").getBeanReader(Pitching.class).read();
    }

    public Collection<Hitting> readHitting() {
        return statsXcel.getSheet("Sheet1").getBeanReader(Hitting.class).read();
    }

    public Collection<Pitching> readPitching() {
        return statsXcel.getSheet("Sheet2").getBeanReader(Pitching.class).read();
    }

    public void writeRoto(List<Roto> rotoList){
        writeRoto(rotoList, "Sheet");
    }
    public void writeLastXWeeks(List<Roto> rotoList) {
        rotoXcel.setOptions(null);
        writeRoto(rotoList, "Recent");
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

        rotoXcel.getSheet("Sheet").getBeanWriter(CategoryRank.class).write(categoryRanks);
        rotoXcel.write(outputFile);
    }

}
