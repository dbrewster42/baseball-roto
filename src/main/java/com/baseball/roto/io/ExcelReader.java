package com.baseball.roto.io;

import com.baseball.roto.model.Hitting;
import com.baseball.roto.model.Pitching;
import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;

@Service
@Slf4j
public class ExcelReader {

    private final Xcelite statsXcel;

    public ExcelReader(@Value("${file.stats}") String statsFile) {
        this.statsXcel = new Xcelite(new File(statsFile));
    }

    public Collection<Hitting> readHitting() {
        XceliteSheet sheet = statsXcel.getSheet("Sheet1");

        SheetReader<Hitting> reader = sheet.getBeanReader(Hitting.class);
        return reader.read();
    }

    public Collection<Pitching> readPitching() {
        XceliteSheet sheet = statsXcel.getSheet("Sheet2");

        SheetReader<Pitching> reader = sheet.getBeanReader(Pitching.class);
        return reader.read();
    }
}
