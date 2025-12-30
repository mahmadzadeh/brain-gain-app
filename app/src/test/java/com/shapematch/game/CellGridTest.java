package com.shapematch.game;

import com.shapematch.data.shapes.FourSquare;
import com.shapematch.data.shapes.HollowSquare;
import com.shapematch.data.shapes.ShapeSlotPair;
import com.shapematch.data.shapes.SolidSquare;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.shapematch.game.CellGrid.GRID_COL_CNT;
import static com.shapematch.game.CellGrid.GRID_ROW_CNT;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;

public class CellGridTest {

    @Test
    public void givenEmptyListOfShapeAndSlotsThenPopulateGridCellsCreatesGridOfEmptyCells() {
        List<ShapeSlotPair> pairs = new ArrayList<>();

        List<List<Cell>> grid = new CellGrid(pairs).populateGridCells();

        assertEquals(GRID_ROW_CNT, grid.size());

        for(List<Cell> oneRow : grid) {
            assertEquals(GRID_COL_CNT, oneRow.size());
            for(Cell cell : oneRow){
                assertTrue(cell instanceof EmptyCell);
            }
        }
    }

    @Test
    public void givenListShapeAndSlotsSizeOneThenPopulateGridCellsCreatesGridWithOneCellPopulated() {
        List<ShapeSlotPair> pairs = new ArrayList<>();
        pairs.add(new ShapeSlotPair(new HollowSquare(), 0));

        List<List<Cell>> grid = new CellGrid(pairs).populateGridCells();

        Cell populatedCell = grid.get(0).get(0);
        assertTrue(populatedCell.getShape() instanceof HollowSquare);
    }

    @Test
    public void givenListShapeAndSlotsThenPopulateGridCellsCreatesGridWithOneCellPopulated() {
        List<ShapeSlotPair> pairs = new ArrayList<>();
        pairs.add(new ShapeSlotPair(new HollowSquare(), 0));

        int lastGridIndex = (GRID_COL_CNT * GRID_ROW_CNT) - 1 ;

        pairs.add(new ShapeSlotPair(new SolidSquare(), lastGridIndex));

        List<List<Cell>> grid = new CellGrid(pairs).populateGridCells();

        Cell populatedCell = grid.get(0).get(0);
        assertTrue(populatedCell.getShape() instanceof HollowSquare);

        Cell lastCell = grid.get(GRID_ROW_CNT - 1 ).get(GRID_COL_CNT - 1);
        assertTrue(lastCell.getShape() instanceof SolidSquare);

        assertEquals((GRID_COL_CNT * GRID_ROW_CNT) - 2, filterEmptyCellCount(grid));
    }

    @Test
    public void givenFullGridThenGridContainsNoEmptyCells() {
        List<ShapeSlotPair> pairs = getShapeSlotPairsOfSize((GRID_COL_CNT * GRID_ROW_CNT));

        List<List<Cell>> grid = new CellGrid(pairs).populateGridCells();

        assertEquals(0 , filterEmptyCellCount(grid));
    }

    @Test
    public void givenShouldAlterOneShapeThenMaybeAlterShapesReturnsNewInstanceOfCellGrid(){
        List<ShapeSlotPair> pairs = getShapeSlotPairsOfSize((GRID_COL_CNT * GRID_ROW_CNT));

        CellGrid originalGrid = new CellGrid(pairs);
        int originalShapeCount = originalGrid.getShapeSlotPairCount();

        CellGrid newGrid = originalGrid.maybeAlterShapes(true);

        assertNotSame(originalGrid, newGrid);

        assertEquals( originalShapeCount , newGrid.getShapeSlotPairCount());
    }

    @Test
    public void isEqualReturnsTrueWhenTwoGridsAreEqual() {
        List<ShapeSlotPair> pairs1 = Arrays.asList(
            new ShapeSlotPair(new HollowSquare(), 0),
            new ShapeSlotPair(new HollowSquare(), 0)
        );
        List<ShapeSlotPair> pairs2 = Arrays.asList(
            new ShapeSlotPair(new HollowSquare(), 0),
            new ShapeSlotPair(new HollowSquare(), 0)
        );

        CellGrid grid = new CellGrid(pairs1);
        CellGrid anotherGrid = new CellGrid(pairs2);

        assertTrue(grid.equals(anotherGrid));
    }

    @Test
    public void isEqualReturnsFalseWhenTwoGridsAreNotEqual() {
        List<ShapeSlotPair> pairs1 = Arrays.asList(
            new ShapeSlotPair(new HollowSquare(), 0),
            new ShapeSlotPair(new HollowSquare(), 0)
        );
        List<ShapeSlotPair> pairs2 = Arrays.asList(
            new ShapeSlotPair(new HollowSquare(), 0),
            new ShapeSlotPair(new HollowSquare(), 1)
        );

        CellGrid grid = new CellGrid(pairs1);
        CellGrid anotherGrid = new CellGrid(pairs2);

        assertFalse(grid.equals(anotherGrid));
    }

    @Test
    public void isEqualReturnsFalseWhenOneGridHasShapeThatDoesNotMatch() {
        List<ShapeSlotPair> pairs1 = Arrays.asList(
            new ShapeSlotPair(new HollowSquare(), 0),
            new ShapeSlotPair(new HollowSquare(), 0)
        );
        List<ShapeSlotPair> pairs2 = Arrays.asList(
            new ShapeSlotPair(new HollowSquare(), 0),
            new ShapeSlotPair(new FourSquare(), 0)
        );

        CellGrid grid = new CellGrid(pairs1);
        CellGrid anotherGrid = new CellGrid(pairs2);

        assertFalse(grid.equals(anotherGrid));
    }

    private int  filterEmptyCellCount(List<List<Cell>> grid ) {
        int count = 0;
        for(List<Cell> oneRow : grid ) {
            for(Cell cell: oneRow ){
                if( cell instanceof EmptyCell ){
                    count++;
                }
            }
        }
        return count;
    }

    private List<ShapeSlotPair> getShapeSlotPairsOfSize( int size) {
        List<ShapeSlotPair> pairs = new ArrayList<>();

        for(int i= 0; i<  size ; i++) {
            pairs.add(i, new ShapeSlotPair(new HollowSquare(),i));
        }
        return pairs;
    }
}
