package com.monkeyladder.game;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {

    @Test
    public void twoCellsWithSameLocationAndDataAreEqual( ) {

        Cell one = new Cell( Location.ThreeFour, CellData.Eight );
        Cell two = new Cell( Location.ThreeFour, CellData.Eight );

        assertEquals( one, two );
    }

    @Test
    public void twoCellsWithDifferentLocationsButSameDataAreNotEqual( ) {

        Cell one = new Cell( Location.ThreeFour, CellData.Eight );
        Cell two = new Cell( Location.ZeroZero, CellData.Eight );

        assertNotEquals( one, two );
    }

    @Test
    public void twoUnequalCellsGenerateDifferentHashcodes( ) {

        Cell one = new Cell( Location.ThreeFour, CellData.Eight );
        Cell two = new Cell( Location.ZeroZero, CellData.Eight );

        assertNotEquals( one.hashCode(), two.hashCode() );
    }

    @Test
    public void twoEqualCellsGenerateSameHashcode( ) {
        Cell one = new Cell( Location.ThreeFour, CellData.Eight );
        Cell two = new Cell( Location.ThreeFour, CellData.Eight );

        assertEquals( one.hashCode(), two.hashCode() );
    }
}