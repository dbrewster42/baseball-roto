package com.baseball.roto.repository;

import com.baseball.roto.model.entity.PsdStats;
import com.baseball.roto.model.entity.StatsId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PsdRepository extends StatsRepository<PsdStats> { }
