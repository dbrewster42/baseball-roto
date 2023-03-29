package com.baseball.roto;

import com.baseball.roto.controller.RotoController;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class RunnerTest {
    private Runner sut;
    RotoController mock = mock(RotoController.class);

    @Test
    void run1() {
        sut = new Runner(mock, "recent");

        sut.run();

        verify(mock, times(1)).limitCalculatedRotoToIncludedWeeks(4);
    }
//
//    @Test
//    void run2() {
//        sut = new Runner(mock, "everything - asdf");
//
//        sut.run();
//
//        verify(mock, times(1)).runTotalAndRecentRotoForChampAndPsd(4);
//    }

    @Test
    void run4() {
        sut = new Runner(mock, "everything - ");

        sut.run();

        verify(mock, times(1)).runTotalAndRecentRotoForChampAndPsd(4);
    }


    @Test
    void run3() {
        sut = new Runner(mock, "recent - 3");

        sut.run();

        verify(mock, times(1)).limitCalculatedRotoToIncludedWeeks(3);
    }

    @Test
    void changeName() {
        sut = new Runner(mock, "change - George,Harry");

        sut.run();

        verify(mock, times(1)).changeName("George", "Harry");
    }

}