package com.dualnback.game.factory;

import com.dualnback.game.Cell;
import com.dualnback.game.DualBackGrid;
import com.monkeyladder.R;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.range;

public class GridFactory {

    public static DualBackGrid instance( ) {
        int onImg = R.mipmap.square_blue;
        int offImg = R.mipmap.square;

        List<List<Cell>> grid =
                range( 0, 3 )
                        .mapToObj( row ->
                                range( 0, 3 )
                                        .mapToObj( col -> createSingleCell( onImg, offImg ) )
                                        .collect( Collectors.toList() ) )
                        .collect( Collectors.toList() );

        return new DualBackGrid( grid );
    }

    private static Cell createSingleCell( int onImg, int offImg ) {
        return new Cell( onImg, offImg );
    }
}
