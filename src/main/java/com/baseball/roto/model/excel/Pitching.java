package com.baseball.roto.model.excel;

import com.ebay.xcelite.annotations.Column;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Pitching {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(name="N")
    private String name;
    @Column(name="W")
    private int wins;
    @Column(name="K")
    private int strikeouts;
    @Column(name="ERA")
    private float era;
    @Column(name="WHIP")
    private float whip;
    @Column(name="QS")
    private int qualityStarts;
    @Column(name="NSV")
    private int netSaves;

}
