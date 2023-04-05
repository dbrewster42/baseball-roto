package com.baseball.roto.service;

import com.baseball.roto.model.League;
import com.baseball.roto.model.LeagueStats;
import com.baseball.roto.model.entity.Stats;
import com.baseball.roto.model.excel.CategoryRank;
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
    private final static String FILE_SUFFIX = ".xlsx";
    private final Xcelite statsXcel;
    private final Xcelite rotoXcel;
    private final File outputFile;
    private final League league;

    public ExcelService(@Value("${stats.folder}") String folder, League league) {
        this.rotoXcel = new Xcelite();
        this.statsXcel = new Xcelite(new File(folder + "stats" + FILE_SUFFIX));
        this.outputFile = new File(folder + "results/" + league + FILE_SUFFIX);
        this.league = league;

    }

    public LeagueStats readStats() {
        return new LeagueStats((List<Stats>) statsXcel.getSheet(league.name()).getBeanReader(league.getEntity()).read());
    }
    public void writeRoto(List<Roto> rotoList){
        writeRoto(rotoList, league.name());
    }
    public void writeRecentRoto(List<Roto> rotoList) {
        rotoXcel.setOptions(null);
        writeRoto(rotoList, "Recent " + league.name());
    }
    public void writeRoto(List<Roto> rotoList, String sheetName){
        log.info("writing results");
        rotoXcel.createSheet(sheetName).getBeanWriter(Roto.class).write(rotoList);
        rotoXcel.write(outputFile);
    }

    public void writeRanks(List<CategoryRank> categoryRanks) {
        log.info("writing ranks");
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(categoryRanks.size() + 2);
        rotoXcel.setOptions(options);

        rotoXcel.getSheet(league.name()).getBeanWriter(CategoryRank.class).write(categoryRanks);
        rotoXcel.write(outputFile);
    }
}
