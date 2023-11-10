package com.baseball.roto.service.io;

import com.baseball.roto.model.excel.CategoryRank;
import com.baseball.roto.model.excel.Roto;

import java.util.List;

public interface Writer {
    void writeRoto(List<Roto> rotoList);
    void writeRanks(List<CategoryRank> categoryRanks);
}
