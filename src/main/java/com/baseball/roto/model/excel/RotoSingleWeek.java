package com.baseball.roto.model.excel;

import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;
import lombok.Data;

@Data
@Row(colsOrder = {"rank", "name", "total", "hitting", "pitching"})
public class RotoSingleWeek {
    @Column
    private float rank;
    @Column
    private String name;
    @Column
    private float total;
    @Column
    private float hitting;
    @Column
    private float pitching;


    public RotoSingleWeek() {}

}