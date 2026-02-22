package com.sudoku.ui.mainscreen;

import java.util.Set;

public interface SudokuMainViewContract {

    void updateCell( int row, int col, String value, boolean isFixed );

    void highlightSelectedCell( int row, int col );

    void showConflicts( Set<String> conflictKeys );

    void clearConflicts();

    void updateTimer( String time );

    void updateMoves( int moves );

    void onPuzzleSolved( long elapsedSeconds, int moves );

    void flashGridBorder( boolean correct );
}
