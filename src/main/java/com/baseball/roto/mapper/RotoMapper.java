package com.baseball.roto.mapper;

import com.baseball.roto.model.entity.Stats;
import com.baseball.roto.model.excel.Roto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RotoMapper {

    Roto toRoto(Stats source);

    List<Roto> toRotoList(List<Stats> source);

}
