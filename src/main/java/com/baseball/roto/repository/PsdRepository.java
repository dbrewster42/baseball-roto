package com.baseball.roto.repository;

import com.baseball.roto.model.entity.PsdStats;
import org.springframework.stereotype.Repository;

@Repository
public interface PsdRepository extends StatsRepository<PsdStats> { }
