package com.baseball.roto.io;

import com.baseball.roto.model.Hitting;
import com.baseball.roto.model.Pitching;
import com.baseball.roto.model.Rank;
import com.baseball.roto.model.Roto;
import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
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
        this.outputFile = new File(filename);
        this.statsXcel = new Xcelite(new File(statsFile));
        this.rotoXcel = new Xcelite();
    }

    public Collection<Hitting> readHitting() {
        return statsXcel.getSheet("Sheet1").getBeanReader(Hitting.class).read();
    }

    public Collection<Pitching> readPitching() {
        return statsXcel.getSheet("Sheet2").getBeanReader(Pitching.class).read();
    }

    public void writeRoto(List<Roto> rotos){
        log.info("writing results");
//        XceliteSheet sheet = rotoXcel.createSheet("Sheet");
//        SheetWriter<Roto> writer = sheet.getBeanWriter(Roto.class);
//
//        writer.write(rotos);
//        rotoXcel.write(outputFile);
        rotoXcel.createSheet("Sheet").getBeanWriter(Roto.class).write(rotos);
        rotoXcel.write(outputFile);
        log.info("done");
    }

    public void writeRanks(List<Rank> ranks) {
        rotoXcel.getOptions().setHeaderRowIndex(18);

        XceliteSheet sheet = rotoXcel.getSheet("Sheet");
        SheetWriter writer = sheet.getBeanWriter(Rank.class);

        writer.write(ranks);
        rotoXcel.write(outputFile);
    }
}
