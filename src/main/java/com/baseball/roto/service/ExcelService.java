package com.baseball.roto.service;

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
    private final LeagueService leagueService;
    private final static String FILE_SUFFIX = ".xlsx";
    private final Xcelite statsXcel;
    private final String folder;
    private Xcelite rotoXcel;
    private File outputFile;


    public ExcelService(LeagueService leagueService, @Value("${stats.folder}") String folder) {
        this.leagueService = leagueService;
        this.statsXcel = new Xcelite(new File(folder + "stats" + FILE_SUFFIX));
        this.folder = folder;
    }

    public LeagueStats readStats() {
        return new LeagueStats((List<Stats>) statsXcel.getSheet(leagueService.getLeagueName()).getBeanReader(leagueService.getLeague().getEntity()).read());
    }
    public void writeRoto(List<Roto> rotoList){
        writeRoto(rotoList, leagueService.getLeagueName());
    }
    public void writeRecentRoto(List<Roto> rotoList) {
        rotoXcel.setOptions(null);
        writeRoto(rotoList, "Recent " + leagueService.getLeagueName());
    }
    public void writeRoto(List<Roto> rotoList, String sheetName){
        this.outputFile = new File(folder + "results/" + sheetName + FILE_SUFFIX);
        this.rotoXcel = new Xcelite();
        log.info("writing results");
        rotoXcel.createSheet(sheetName).getBeanWriter(Roto.class).write(rotoList);
        rotoXcel.write(outputFile);
    }

    public void writeRanks(List<CategoryRank> categoryRanks) {
        log.info("writing ranks");
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(categoryRanks.size() + 2);
        rotoXcel.setOptions(options);


        rotoXcel.getSheet(leagueService.getLeagueName()).getBeanWriter(CategoryRank.class).write(categoryRanks);
        rotoXcel.write(outputFile);
    }
}
