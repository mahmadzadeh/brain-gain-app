package com.chart.filesystem.dao;

/**
 * Game identifiers used as keys in the unified stats file.
 */
public enum GameKey {

    DUAL_N_BACK( "dualnback" ),
    MONKEY_LADDER( "monkeyladder" ),
    STROOP( "stroop" ),
    SHAPE_MATCH( "shapematch" ),
    SUDOKU( "sudoku" ),
    TOKEN_SEARCH( "tokensearch" );

    private final String key;

    GameKey( String key ) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
