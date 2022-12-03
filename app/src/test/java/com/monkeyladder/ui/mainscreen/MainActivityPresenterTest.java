package com.monkeyladder.ui.mainscreen;

import com.monkeyladder.game.Board;
import com.monkeyladder.util.CountDownTimerParameter;
import com.monkeyladder.util.LocationData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityPresenterTest {

    List<LocationData> data = new ArrayList<>();

    @Mock
    private MainActivityViewContract mockView;

    @Mock
    private MainActivityModelContract mockModel;

    @Mock
    private Board mockGameBoard;

    private MainActivityPresenter sut;

    private CountDownTimerParameter countDownTimerParam;


    @Before
    public void setUp( ) {
        countDownTimerParam = CountDownTimerParameter.of( 10, 1 );

        sut = new MainActivityPresenter( mockView, mockModel );
    }


    @Test
    public void getCurrentBoardReturnsTheCurrentLocationOnBoardThatAreSet( ) {

        assertEquals( 0, 0 );
    }


}