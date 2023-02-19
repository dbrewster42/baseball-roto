package com.baseball.roto.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class StatsId implements Serializable {
    @Id
    private int week;
    @Id
    private String name;
}