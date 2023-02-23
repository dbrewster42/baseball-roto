package com.baseball.roto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsId implements Serializable {
    @Id
    private int week;
    @Id
    private String name;
}