package com.baseball.roto;

import com.baseball.roto.controller.RotoRunner;
import com.baseball.roto.service.RotoService;
import com.baseball.roto.service.io.ReadWrite;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class RotoRunnerTest {
    private RotoRunner sut;
    private final RotoService rotoService = mock(RotoService.class);
    private final ReadWrite excelService = mock(ReadWrite.class);


//    @Test
//    void run0() {
//        sut = new RotoRunner(rotoService, excelService, "everything");
//
//        sut.run();
//
//        verify(rotoService, times(2)).limitRotoToIncludedWeeks(4);
//        verify(rotoService, times(2)).calculateRoto(any());
//    }

//    @Test
//    void run1() {
//        sut = new RotoRunner(rotoService, excelService, "recent");
//
//        sut.run();
//
//        verify(rotoService, times(1)).limitRotoToIncludedWeeks(4);
//    }
//
//    @Test
//    void run2() {
//        sut = new RotoRunner(rotoService,  excelService, "everything - asdf");
//
//        sut.run();
//
//        verify(rotoService, times(1)).limitRotoToIncludedWeeks(4);
//    }
//
//    @Test
//    void run3() {
//        sut = new RotoRunner(rotoService, excelService, "recent - 3");
//
//        sut.run();
//
//        verify(rotoService, times(1)).limitRotoToIncludedWeeks(3);
//    }
//
//    @Test
//    void run4() {
//        sut = new RotoRunner(rotoService, excelService, "everything - ");
//
//        sut.run();
//
//        verify(rotoService, times(1)).limitRotoToIncludedWeeks(4);
//    }
//
//    @Test
//    void run5() {
//        sut = new RotoRunner(rotoService, excelService, "everything - 1");
//
//        sut.run();
//
//        verify(rotoService, times(1)).limitRotoToIncludedWeeks(1);
//    }

//    @Test
//    void changeName() {
//        sut = new RotoRunner(rotoService, excelService, "change - George,Harry");
//
//        sut.run();
//
//        verify(rotoService, times(1)).updatePlayerName("George", "Harry");
//    }

}