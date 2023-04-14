package com.baseball.roto;

import com.baseball.roto.controller.Runner;
import com.baseball.roto.service.ExcelService;
import com.baseball.roto.service.LeagueService;
import com.baseball.roto.service.RotoService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class RunnerTest {
    private Runner sut;
    private final RotoService rotoService = mock(RotoService.class);
    private final ExcelService excelService = mock(ExcelService.class);
    private final LeagueService leagueService = mock(LeagueService.class);


    @Test
    void run0() {
        sut = new Runner(rotoService, excelService, leagueService, "everything");

        sut.run();

        verify(rotoService, times(1)).limitRotoToIncludedWeeks(4);
    }

    @Test
    void run1() {
        sut = new Runner(rotoService, excelService, leagueService, "recent");

        sut.run();

        verify(rotoService, times(1)).limitRotoToIncludedWeeks(4);
    }

    @Test
    void run2() {
        sut = new Runner(rotoService,  excelService, leagueService, "everything - asdf");

        sut.run();

        verify(rotoService, times(1)).limitRotoToIncludedWeeks(4);
    }

    @Test
    void run3() {
        sut = new Runner(rotoService, excelService, leagueService, "recent - 3");

        sut.run();

        verify(rotoService, times(1)).limitRotoToIncludedWeeks(3);
    }

    @Test
    void run4() {
        sut = new Runner(rotoService, excelService, leagueService, "everything - ");

        sut.run();

        verify(rotoService, times(1)).limitRotoToIncludedWeeks(4);
    }

    @Test
    void run5() {
        sut = new Runner(rotoService, excelService, leagueService, "everything - 1");

        sut.run();

        verify(rotoService, times(1)).limitRotoToIncludedWeeks(1);
    }

    @Test
    void changeName() {
        sut = new Runner(rotoService, excelService, leagueService, "change - George,Harry");

        sut.run();

        verify(rotoService, times(1)).updatePlayerName("George", "Harry");
    }

}