package com.baseball.roto.validation;

import com.baseball.roto.exception.BadInput;
import com.baseball.roto.model.League;
import com.baseball.roto.model.RawStats;

import static com.baseball.roto.model.League.leagueNames;
import static java.lang.String.format;

public class InputValidator {
    private InputValidator(){}

    public static League validatedLeague(String leagueName) {
        try {
            return League.valueOf(leagueName);
        } catch (Exception e) {
            throw new BadInput(format("The given league [%s] must be one of the following supported options - %s", leagueName, leagueNames()));
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
