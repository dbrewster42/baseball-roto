package com.baseball.roto.service.io;

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
        return reader.readStats();
    }

    @Override
    public void writeRoto(List<Roto> rotoList) {
        writer.writeRoto(rotoList);
    }

    @Override
    public void writeRanks(List<CategoryRank> categoryRanks) {
        writer.writeRanks(categoryRanks);
    }
}
