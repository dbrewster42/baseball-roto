package com.baseball.roto.io;

import com.baseball.roto.model.Player;
import com.baseball.roto.model.Rank;
import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@Slf4j
public class ExcelWriter {
    private File file;
    private Xcelite xcelite;

    public ExcelWriter(@Value("${file.output}") String outputFile) {
        this.xcelite = new Xcelite();
        this.file = new File(outputFile);
    }

    public void writeRoto(List<Player> players){
        log.info("writing results");
        XceliteSheet sheet = xcelite.createSheet("Sheet");
        SheetWriter<Player> writer = sheet.getBeanWriter(Player.class);

        writer.write(players);
        xcelite.write(file);
        log.info("done");
    }

    public void writeRanks(List<Rank> ranks) {
        XceliteOptions xceliteOptions = xcelite.getOptions();
        xceliteOptions.setHeaderRowIndex(18);
        XceliteSheet sheet = xcelite.getSheet("Sheet");
        SheetWriter writer = sheet.getBeanWriter(Rank.class);

        writer.write(ranks);
        xcelite.write(file);
    }
}
