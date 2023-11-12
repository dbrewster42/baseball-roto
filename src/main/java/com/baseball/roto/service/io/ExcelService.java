package com.baseball.roto.service.io;

import com.baseball.roto.model.LeagueStats;
import com.baseball.roto.model.entity.Stats;
import com.baseball.roto.model.excel.CategoryRank;
import com.baseball.roto.model.excel.Roto;
import com.baseball.roto.service.LeagueService;
import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.options.XceliteOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;


@Service
@Slf4j
public class ExcelService implements Reader, Writer {
    private final LeagueService leagueService;
    private final static String FILE_SUFFIX = ".xlsx";
    private final Xcelite statsXcel;
    private final Xcelite rotoXcel;
    private final String folder;
    private String sheetName;
    private File outputFile;

    public ExcelService(LeagueService leagueService, @Value("${stats.folder}") String folder) {
        this.leagueService = leagueService;
        this.folder = folder;
        this.rotoXcel = new Xcelite(new XceliteOptions());
        this.statsXcel = new Xcelite(new File(folder + "stats" + FILE_SUFFIX));
    }

    public LeagueStats readStats() {
        return new LeagueStats((List<Stats>) statsXcel.getSheet(leagueService.getLeagueName()).getBeanReader(leagueService.getLeague().getEntity()).read());
    }
    public void writeRoto(List<Roto> rotoList){
        outputFile = new File(folder + "results/" + leagueService.getLeagueName() + FILE_SUFFIX);
        writeRoto(rotoList, "Overall ");
    }

    public void writeRecentRoto(List<Roto> rotoList){
        writeRoto(rotoList, "Recent ");
    }

    public void writeRanks(List<CategoryRank> categoryRanks) {
        log.info("writing ranks");
        prepareFileForSecondTable(categoryRanks.size());

        rotoXcel.getSheet(sheetName).getBeanWriter(CategoryRank.class).write(categoryRanks);
        rotoXcel.write(outputFile);
    }

    private void writeRoto(List<Roto> rotoList, String sheetType){
        log.info("writing results");
        sheetName = sheetType + leagueService.getLeagueName();

        rotoXcel.getOptions().setHeaderRowIndex(0);
        rotoXcel.createSheet(sheetName).getBeanWriter(Roto.class).write(rotoList);
        rotoXcel.write(outputFile);
    }


    private void prepareFileForSecondTable(int leagueSize) {
        rotoXcel.getOptions().setHeaderRowIndex(leagueSize + 2);
    }

}
