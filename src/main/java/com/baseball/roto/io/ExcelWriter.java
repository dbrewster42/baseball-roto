package com.baseball.roto.io;

import com.baseball.roto.model.Roto;
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
    private final File file;
    private final Xcelite xcelite;

    public ExcelWriter(@Value("${file.output}") String outputFile) {
        this.xcelite = new Xcelite();
        this.file = new File(outputFile);
    }

    public void writeRoto(List<Roto> rotos){
        log.info("writing results");
        XceliteSheet sheet = xcelite.createSheet("Sheet");
        SheetWriter<Roto> writer = sheet.getBeanWriter(Roto.class);

        writer.write(rotos);
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
