package com.baseball.roto.repository;

import com.baseball.roto.model.Stats;
import com.baseball.roto.model.StatsId;
import org.springframework.data.repository.CrudRepository;

public interface StatsRepository extends CrudRepository<Stats, StatsId> {
}
