package com.baseball.roto.mapper;

import com.baseball.roto.model.Stats;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class StatFieldsMapper {
    public static Map<String, List<Float>> convertStatFieldsToMap(List<Stats> stats, Function<Stats, Map<String, List<Float>>> statsGetter) {
        return stats.stream()
            .map(statsGetter)
            .reduce((left, right) -> {
                left.putAll(right);
                return left;
            })
            .orElseThrow(() -> new RuntimeException("error combining stat lists"));
    }
}
