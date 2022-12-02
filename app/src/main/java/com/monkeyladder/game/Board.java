package com.monkeyladder.game;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board {
    private final List<List<Cell>> grid;
    private final BoardSize size;

    public Board( BoardSize size ) {
        this.grid = generateGrid( size );
        this.size = size;
    }

    public Board( BoardSize size, List<Cell> onCells ) {
        this.grid = generateGrid( size, onCells );
        this.size = size;
    }

    private List<List<Cell>> generateGrid( BoardSize size, List<Cell> onCells ) {
        final List<List<Cell>> defaultGrid = generateGrid( size );

        onCells.stream()
                .forEach( cell -> defaultGrid
                        .get( cell.getLocation().getRow() )
                        .set( cell.getLocation().getCol(), new Cell( cell ) ) );

        return defaultGrid;
    }

    private List<List<Cell>> generateGrid( BoardSize size ) {
        List<List<Cell>> g = new ArrayList<>();

        for ( int row = 0; row < size.rowCount(); row++ ) {
            List<Cell> columns = new ArrayList<>();
            for ( int col = 0; col < size.colCount(); col++ ) {
                columns.add( new Cell( Location.of( row, col ), CellData.EmptyCellData ) );
            }
            g.add( columns );
        }
        return g;
    }

    /**
     * Go through the cell grid and return all the cells, in ordered form, that are
     * set. Cells are ordered since when evaluating user input we rely on the cell
     * data to be in ASC order to evaluate user selected cells
     */
    public List<Cell> getCellsThatAreNotEmpty( ) {
        List<Cell> nonEmptyCells = new ArrayList<>();

        for ( int row = 0; row < size.rowCount(); row++ ) {
            for ( int col = 0; col < size.colCount(); col++ ) {
                Cell cell = grid.get( row ).get( col );
                if ( cell.getData() != CellData.EmptyCellData ) {
                    nonEmptyCells.add( cell );
                }
            }
        }


        Collections.sort( nonEmptyCells, ( o1, o2 ) -> o1.getData().compareTo( o2.getData() ) );

        assertAllCellAreInOrder( nonEmptyCells );

        return nonEmptyCells;
    }

    private void assertAllCellAreInOrder( List<Cell> nonEmptyCells ) {
        for ( int index = 0; index < nonEmptyCells.size() - 1; index++ ) {
            if ( nonEmptyCells.get( index ).getData().data + 1 != nonEmptyCells.get( index + 1 ).getData().data ) {
                dumpGrid(); // something funky happening
                throw new IllegalStateException( "cell " + nonEmptyCells.get( index ) + " and cell " + nonEmptyCells.get( index + 1 ) + " " +
                        " are not in order ... " );
            }
        }
    }

    private void dumpGrid( ) {
        for ( int row = 0; row < size.rowCount(); row++ ) {
            for ( int col = 0; col < size.colCount(); col++ ) {
                Cell cell = grid.get( row ).get( col );
                Log.e( "Board", "CELL " + cell );
            }
        }
    }
}
