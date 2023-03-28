package com.baseball.roto.repository;

import com.baseball.roto.model.entity.ChampStats;
import org.springframework.stereotype.Repository;

@Repository
public interface ChampRepository extends StatsRepository<ChampStats> { }
