package com.monkeyladder.game;

import java.util.Arrays;
import java.util.List;

class MonkeyLadderGameTestDataProvider {

    public List<TestBoardLocationData> levelFourTestBoardData( ) {
        return Arrays.asList(
                new TestBoardLocationData( Location.ZeroZero, CellData.One ),
                new TestBoardLocationData( Location.ZeroOne, CellData.Two ),
                new TestBoardLocationData( Location.ThreeZero, CellData.Three ),
                new TestBoardLocationData( Location.TwoOne, CellData.Four ),
                new TestBoardLocationData( Location.OneFour, CellData.Five ) );
    }

    public List<TestBoardLocationData> levelTwoTestBoardData( ) {
        return Arrays.asList(
                new TestBoardLocationData( Location.ThreeFour, CellData.One ),
                new TestBoardLocationData( Location.ThreeTwo, CellData.Two ) );
    }

    public List<TestBoardLocationData> levelOneTestBoardData( ) {
        return Arrays.asList(
                new TestBoardLocationData( Location.ThreeFour, CellData.One ) );
    }

    public class TestBoardLocationData {

        private final Location location;
        private final CellData data;

        private TestBoardLocationData( Location location, CellData data ) {
            this.location = location;
            this.data = data;
        }

        public Location getLocation( ) {
            return location;
        }

        public CellData getData( ) {
            return data;
        }
    }
}
