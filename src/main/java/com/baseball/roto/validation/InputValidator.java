package com.baseball.roto.validation;

import com.baseball.roto.exception.BadInput;
import com.baseball.roto.model.League;
import com.baseball.roto.model.RawStats;
import com.baseball.roto.model.entity.Stats;

import java.util.List;

import static com.baseball.roto.model.League.leagueNames;
import static java.lang.String.format;

public class InputValidator {

    public static League validatedLeague(String leagueName) {
        try {
            return League.valueOf(leagueName);
        } catch (Exception e) {
            throw new BadInput(format("The given league [%s] must be one of the following supported options - %s", leagueName, leagueNames()));
        }
    }

    public static void validateWeek(List<Stats> thisWeeksStats) {
        if (!thisWeeksStats.isEmpty()) {
            throw new BadInput("The stats have already been calculated for the given week in this league");
        }
    }

    public static void validateRotoHasBeenPrepped(List<Stats> thisWeeksStats) {
        if (thisWeeksStats.isEmpty()) {
            throw new BadInput("Roto must be calculated before the stats are limited to only include recent performance");
        }
    }

    public static int validatedLeagueSize(RawStats rawStats, League league) {
        int size = rawStats.getHittingList().size();
        if (size != rawStats.getPitchingList().size()) {
            throw new BadInput("The Stats are invalid. Inconsistent number of pitching and hitting columns.");
        }
        if (size != league.getNumberOfTeams()) {
            throw new BadInput(format("The Stats and League have been mismatched. %s teams were expected but %s were provided",
                league.getNumberOfTeams(), size));
        }
        return size;
    }
}
