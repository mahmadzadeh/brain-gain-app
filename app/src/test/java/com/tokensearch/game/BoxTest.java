package com.tokensearch.game;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BoxTest {

    @Test
    public void boxStoresGridPosition() {
        Box box = new Box( 2, 3 );

        assertThat( box.row(), is( 2 ) );
        assertThat( box.col(), is( 3 ) );
    }

    @Test
    public void newBoxHasNoTokenFound() {
        Box box = new Box( 0, 0 );

        assertThat( box.isTokenFound(), is( false ) );
    }

    @Test
    public void markTokenFoundSetsFlag() {
        Box box = new Box( 0, 0 );

        box.markTokenFound();

        assertThat( box.isTokenFound(), is( true ) );
    }
}
