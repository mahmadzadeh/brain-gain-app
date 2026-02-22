package com.tokensearch.game;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class BoxLayoutTest {

    @Test
    public void generatesCorrectNumberOfBoxes() {
        BoxLayout layout = new BoxLayout( new Random( 42 ) );

        List<Box> boxes = layout.generate( 5 );

        assertThat( boxes.size(), is( 5 ) );
    }

    @Test
    public void boxPositionsAreWithinGrid() {
        BoxLayout layout = new BoxLayout( new Random( 42 ) );

        List<Box> boxes = layout.generate( 8 );

        for ( Box box : boxes ) {
            assertTrue( box.row() >= 0 && box.row() < BoxLayout.ROWS );
            assertTrue( box.col() >= 0 && box.col() < BoxLayout.COLS );
        }
    }

    @Test
    public void allPositionsAreUnique() {
        BoxLayout layout = new BoxLayout( new Random( 42 ) );

        List<Box> boxes = layout.generate( 10 );

        Set<String> positions = new HashSet<>();
        for ( Box box : boxes ) {
            positions.add( box.row() + "," + box.col() );
        }
        assertThat( positions.size(), is( boxes.size() ) );
    }

    @Test
    public void generatesSingleBox() {
        BoxLayout layout = new BoxLayout( new Random( 42 ) );

        List<Box> boxes = layout.generate( 1 );

        assertThat( boxes.size(), is( 1 ) );
    }

    @Test
    public void capsAtTotalCells() {
        BoxLayout layout = new BoxLayout( new Random( 42 ) );

        List<Box> boxes = layout.generate( 100 );

        assertThat( boxes.size(), is( BoxLayout.TOTAL_CELLS ) );
    }
}
