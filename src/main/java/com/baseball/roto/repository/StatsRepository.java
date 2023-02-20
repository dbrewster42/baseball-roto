package com.baseball.roto.repository;

import com.baseball.roto.model.Stats;
import com.baseball.roto.model.StatsId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StatsRepository extends CrudRepository<Stats, StatsId> {
    List<Stats> findAllByWeek(int week);
}
