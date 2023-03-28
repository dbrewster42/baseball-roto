package com.baseball.roto.repository;

import com.baseball.roto.model.entity.Stats;
import com.baseball.roto.model.entity.StatsId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface StatsRepository<T extends Stats> extends CrudRepository<T, StatsId> {
    List<T> findAllByWeek(int week);
    List<T> findAllByName(String name);
}

