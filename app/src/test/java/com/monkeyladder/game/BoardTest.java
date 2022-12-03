package com.monkeyladder.game;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoardTest {


    private List<Cell> onCells = new ArrayList<>();

    @Test
    public void givenBoardSize_thenConstructorBuildsBoardCorrectSize( ) {
        BoardSize boardSize = BoardSize.FourByFive;
        Board board = new Board( boardSize );

        List<Cell> grid = board.getCellsThatAreNotEmpty();

        assertEquals( 0, grid.size() );
    }

    @Test
    public void givenBoardSizeAndOnCellsWithNothingInIt_thenConstructorBuildsBoardWWithAllDefaultCellData( ) {
        BoardSize boardSize = BoardSize.FourByFive;

        onCells = new ArrayList<>();

        Board board = new Board( boardSize, onCells );

        List<Cell> notEmptyCells = board.getCellsThatAreNotEmpty();

        assertTrue( notEmptyCells.isEmpty() );
    }

    @Test
    public void givenBoardSizeAndOnCells_thenGetCellsThatAreNotEmptyWillReturnCellsThatAreSet( ) {
        BoardSize boardSize = BoardSize.FourByFive;

        Cell expectedZerZeroCell = new Cell( Location.ZeroZero, CellData.Eight );
        Cell expectedZeroOneCell = new Cell( Location.ZeroOne, CellData.Nine );
        Cell expectedThreeFourCell = new Cell( Location.ThreeFour, CellData.Ten );

        onCells = Arrays.asList( expectedZerZeroCell, expectedZeroOneCell, expectedThreeFourCell );

        List<Cell> actualNonEmptyCells = new Board( boardSize, onCells ).getCellsThatAreNotEmpty();

        assertTrue( actualNonEmptyCells.contains( expectedZerZeroCell ) );
        assertTrue( actualNonEmptyCells.contains( expectedZeroOneCell ) );
        assertTrue( actualNonEmptyCells.contains( expectedThreeFourCell ) );

    }

    @Test
    public void givenBoardSizeAndOnCells_thenGetCellsThatAreNotEmptyWillReturnCellsThatAreSetInOrder( ) {
        BoardSize boardSize = BoardSize.FourByFive;

        Cell expectedZerZeroCell = new Cell( Location.ZeroZero, CellData.Eight );
        Cell expectedZeroOneCell = new Cell( Location.ZeroOne, CellData.Nine );
        Cell expectedThreeFourCell = new Cell( Location.ThreeFour, CellData.Ten );

        onCells = Arrays.asList( expectedZerZeroCell, expectedZeroOneCell, expectedThreeFourCell );

        List<Cell> actualNonEmptyCells = new Board( boardSize, onCells ).getCellsThatAreNotEmpty();

        // cells are ordered based on the data contained in them in ASC order
        assertEquals( expectedZerZeroCell, actualNonEmptyCells.get( 0 ) );
        assertEquals( expectedZeroOneCell, actualNonEmptyCells.get( 1 ) );
        assertEquals( expectedThreeFourCell, actualNonEmptyCells.get( 2 ) );
    }

}