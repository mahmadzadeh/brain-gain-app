package com.monkeyladder.game;

import static com.monkeyladder.util.random.RandomNumberGenerator.next_N_DistinctRandomIntsWithinRangeInRandomOrder;

import com.monkeyladder.util.IntegerRange;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum Location {
    ZeroZero( 0, 0 ),
    ZeroOne( 0, 1 ),
    ZeroTwo( 0, 2 ),
    ZeroThree( 0, 3 ),
    ZeroFour( 0, 4 ),

    OneZero( 1, 0 ),
    OneOne( 1, 1 ),
    OneTwo( 1, 2 ),
    OneThree( 1, 3 ),
    OneFour( 1, 4 ),

    TwoZero( 2, 0 ),
    TwoOne( 2, 1 ),
    TwoTwo( 2, 2 ),
    TwoThree( 2, 3 ),
    TwoFour( 2, 4 ),

    ThreeZero( 3, 0 ),
    ThreeOne( 3, 1 ),
    ThreeTwo( 3, 2 ),
    ThreeThree( 3, 3 ),
    ThreeFour( 3, 4 ),

    NonExistentLocation( -1, -1 );


    private final int row;

    private final int col;
    Location( int row, int col ) {

        this.row = row;
        this.col = col;
    }

    /**
     * Given the game level selection level.cellCount() valid random locations
     * Valid means location that is not NullLocation
     *
     * @param level
     * @return
     */
    public static List<Location> getRandomLocationForLevel( GameLevel level ) {
        List<Location> locationList = Arrays.stream( Location.values() ).collect( Collectors.toList() );

        Collections.shuffle( locationList );

        locationList.remove( NonExistentLocation );

        return next_N_DistinctRandomIntsWithinRangeInRandomOrder( level.cellCount(),
                IntegerRange.instanceWithinCollectionSize( locationList ) )
                .stream()
                .map( index -> locationList.get( index ) )
                .collect( Collectors.toList() );
    }

    public static Location of( int row, int col ) {
        for ( Location location : Location.values() ) {
            if ( location.row == row && location.col == col ) {
                return location;
            }
        }
        throw new IllegalStateException( "Unable to create location of row=" + row + " and col=" + col );
    }

    public int getRow( ) {
        return row;
    }

    public int getCol( ) {
        return col;
    }

    @Override
    public String toString( ) {
        return "Location{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
