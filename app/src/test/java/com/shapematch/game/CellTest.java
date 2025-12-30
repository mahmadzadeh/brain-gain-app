package com.shapematch.game;

import com.shapematch.data.shapes.HollowCircle;
import com.shapematch.data.shapes.HollowSquare;
import com.shapematch.data.shapes.NullShape;
import com.shapematch.data.shapes.SolidCircle;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CellTest {

    @Test
    public void canCreateCellWithShape() {
        Cell cell = new Cell(new HollowCircle());
        assertNotNull(cell);
    }

    @Test
    public void cellReturnsCorrectShape() {
        HollowCircle shape = new HollowCircle();
        Cell cell = new Cell(shape);

        assertEquals(shape, cell.getShape());
    }

    @Test
    public void cellReturnsCorrectShapeResourceId() {
        HollowCircle shape = new HollowCircle();
        Cell cell = new Cell(shape);

        assertEquals(shape.getResourceId().intValue(), cell.getShapeResourceId());
    }

    @Test
    public void twoCellsWithSameShapeAreEqual() {
        Cell cell1 = new Cell(new HollowCircle());
        Cell cell2 = new Cell(new HollowCircle());

        assertTrue(cell1.equals(cell2));
    }

    @Test
    public void twoCellsWithDifferentShapesAreNotEqual() {
        Cell cell1 = new Cell(new HollowCircle());
        Cell cell2 = new Cell(new SolidCircle());

        assertFalse(cell1.equals(cell2));
    }

    @Test
    public void cellEqualsItself() {
        Cell cell = new Cell(new HollowCircle());

        assertTrue(cell.equals(cell));
    }

    @Test
    public void cellNotEqualToNull() {
        Cell cell = new Cell(new HollowCircle());

        assertFalse(cell.equals(null));
    }

    @Test
    public void cellNotEqualToNonCellObject() {
        Cell cell = new Cell(new HollowCircle());

        assertFalse(cell.equals("not a cell"));
    }

    @Test
    public void cellHashCodeConsistentWithEquals() {
        Cell cell1 = new Cell(new HollowCircle());
        Cell cell2 = new Cell(new HollowCircle());

        assertEquals(cell1.hashCode(), cell2.hashCode());
    }

    @Test
    public void cellToStringContainsShapeInfo() {
        Cell cell = new Cell(new HollowCircle());
        String str = cell.toString();

        assertNotNull(str);
        assertTrue(str.contains("Cell"));
        assertTrue(str.contains("Shape"));
    }

    @Test
    public void emptyCellIsInstanceOfCell() {
        EmptyCell emptyCell = new EmptyCell();

        assertTrue(emptyCell instanceof Cell);
    }

    @Test
    public void emptyCellContainsNullShape() {
        EmptyCell emptyCell = new EmptyCell();

        assertTrue(emptyCell.getShape() instanceof NullShape);
    }

    @Test
    public void twoEmptyCellsAreEqual() {
        EmptyCell emptyCell1 = new EmptyCell();
        EmptyCell emptyCell2 = new EmptyCell();

        assertTrue(emptyCell1.equals(emptyCell2));
    }

    @Test
    public void emptyCellNotEqualToCellWithRealShape() {
        EmptyCell emptyCell = new EmptyCell();
        Cell cell = new Cell(new HollowSquare());

        assertFalse(emptyCell.equals(cell));
    }
}
