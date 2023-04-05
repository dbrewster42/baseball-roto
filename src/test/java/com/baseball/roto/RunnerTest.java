package com.baseball.roto;

import com.baseball.roto.service.ExcelService;
import com.baseball.roto.service.RotoService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class RunnerTest {
    private Runner sut;
    private final RotoService rotoService = mock(RotoService.class);
    private final ExcelService excelService = mock(ExcelService.class);

    @Test
    void run0() {
        sut = new Runner(rotoService, excelService, "everything");

        sut.run();

        verify(rotoService, times(1)).limitRotoToIncludedWeeks(4);
    }

    @Test
    void run1() {
        sut = new Runner(rotoService, excelService, "recent");

        sut.run();

        verify(rotoService, times(1)).limitRotoToIncludedWeeks(4);
    }

    @Test
    void run2() {
        sut = new Runner(rotoService,  excelService, "everything - asdf");

        sut.run();

        verify(rotoService, times(1)).limitRotoToIncludedWeeks(4);
    }

    @Test
    void run3() {
        sut = new Runner(rotoService, excelService, "recent - 3");

        sut.run();

        verify(rotoService, times(1)).limitRotoToIncludedWeeks(3);
    }

    @Test
    void run4() {
        sut = new Runner(rotoService, excelService, "everything - ");

        sut.run();

        verify(rotoService, times(1)).limitRotoToIncludedWeeks(4);
    }

    @Test
    void run5() {
        sut = new Runner(rotoService, excelService, "everything - 1");

        sut.run();

        verify(rotoService, times(1)).limitRotoToIncludedWeeks(1);
    }

    @Test
    void changeName() {
        sut = new Runner(rotoService, excelService, "change - George,Harry");

        sut.run();

        verify(rotoService, times(1)).updatePlayerName("George", "Harry");
    }

}