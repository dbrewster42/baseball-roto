package com.baseball.roto.service.io;

import com.baseball.roto.exception.BadInput;
import com.baseball.roto.model.LeagueStats;
import com.baseball.roto.model.excel.CategoryRank;
import com.baseball.roto.model.excel.Roto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ReadWrite implements Reader, Writer {
    private Reader reader;
    private Writer writer;

    @Override
    public LeagueStats readStats() {
        try {
            return reader.readStats();
        } catch (Exception e) {
            throw new BadInput("There was an error reading the stats - " + e.getMessage());
        }
    }

    @Override
    public void writeRoto(List<Roto> rotoList) {
        writer.writeRoto(rotoList);
    }

    @Override
    public void writeRecentRoto(List<Roto> rotoList) {
        writer.writeRecentRoto(rotoList);
    }

    @Override
    public void writeRanks(List<CategoryRank> categoryRanks) {
        writer.writeRanks(categoryRanks);
    }
}
