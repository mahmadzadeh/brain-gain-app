package com.monkeyladder.util.random;

import com.monkeyladder.game.Cell;
import com.monkeyladder.game.CellData;
import com.monkeyladder.game.GameLevel;
import com.monkeyladder.game.Location;
import com.monkeyladder.util.IntegerRange;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.monkeyladder.util.random.RandomNumberGenerator.next_N_DistinctRandomIntsWithinRangeInRandomOrder;

public class RandomBoardGenerator {

    public static List<Cell> nextTrialForLevel( GameLevel level ) {
        List<Location> locationsForLevel = Location.getRandomLocationForLevel( level );
        if ( locationsForLevel.isEmpty() ) {
            return Collections.emptyList();
        }

        List<Integer> numbers = next_N_DistinctRandomIntsWithinRangeInRandomOrder( level.cellCount(),
                new IntegerRange( 1, locationsForLevel.size() ) );

        if ( locationsForLevel.size() != numbers.size() ) {
            throw new IllegalStateException( "Expected to have the same number of locations and " +
                    "numbers to go with locations" );
        }

        return IntStream.range( 0, locationsForLevel.size() )
                .mapToObj( index ->
                        new Cell( locationsForLevel.get( index ), CellData.from( numbers.get( index ) ) ) )
                .collect( Collectors.toList() );
    }
}
