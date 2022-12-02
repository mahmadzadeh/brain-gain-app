package com.monkeyladder.game;

public class NullCell extends Cell {
    private static NullCell instance;

    private NullCell( ) {
        super( Location.NonExistentLocation, CellData.from( -1 ));
    }

    public static NullCell getInstance( ) {
        if ( instance == null ) {
            instance = new NullCell();
        }

        return instance;
    }
}
