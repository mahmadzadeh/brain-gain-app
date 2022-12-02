package com.monkeyladder.game;

public enum CellData implements Comparable<CellData> {
    EmptyCellData( -1 ),
    One( 1 ),
    Two( 2 ),
    Three( 3 ),
    Four( 4 ),
    Five( 5 ),
    Six( 6 ),
    Seven( 7 ),
    Eight( 8 ),
    Nine( 9 ),
    Ten( 10 ),
    Eleven( 11 ),
    Twelve( 12 ),
    Thirteen( 13 ),
    Fourteen( 14 ),
    Fifteen( 15 ),
    Sixteen( 16 );

    int data;

    CellData( int i ) {
        data = i;
    }

    public int getData( ) {
        return data;
    }

    public static CellData from( Integer cellDataValue ) {
        for ( CellData data : CellData.values() ) {
            if ( data.data == cellDataValue ) {
                return data;
            }
        }

        return EmptyCellData;
    }
}
