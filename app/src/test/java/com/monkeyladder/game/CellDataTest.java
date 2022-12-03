package com.monkeyladder.game;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static com.monkeyladder.game.CellData.EmptyCellData;
import static org.junit.Assert.*;

public class CellDataTest {

    @Test
    public void givenInvalidCellData_thenReturnInvalidCellDataValue( ) {
        assertEquals( EmptyCellData, CellData.from( -1111));
    }

    @Test
    public void givenValidCellData_thenReturnCellDataRepresentingValue( ) {

        Map<Integer, CellData> map = new HashMap<Integer, CellData>() {{
            put( 1, CellData.One );
            put( 2, CellData.Two );
            put( 3, CellData.Three );
            put( 4, CellData.Four );
            put( 5, CellData.Five );
            put( 6, CellData.Six );
            put( 7, CellData.Seven );
            put( 8, CellData.Eight );
            put( 9, CellData.Nine );
            put( 10, CellData.Ten );
            put( 11, CellData.Eleven );
            put( 12, CellData.Twelve);
            put( 13, CellData.Thirteen );
            put( 14, CellData.Fourteen );
            put( 15, CellData.Fifteen );
            put( 16, CellData.Sixteen );

        }};
        IntStream
                .range( 1, 16 )
                .forEach( value -> assertEquals( map.get(value), CellData.from( value ) ) );
    }

    @Test
    public void CellDataCanBeCompared() {
        assertEquals( -1, CellData.One.compareTo( CellData.Two ));
        assertEquals( 0, CellData.Two.compareTo( CellData.Two ));
        assertEquals( 1, CellData.Two.compareTo( CellData.One ));
    }

}