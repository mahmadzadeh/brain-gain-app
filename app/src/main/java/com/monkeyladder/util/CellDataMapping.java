package com.monkeyladder.util;

import com.monkeyladder.R;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CellDataMapping {

    private final Map<Integer, Integer> mapping;


    public CellDataMapping( ) {
        mapping = Collections.unmodifiableMap( initLookupMap() );
    }

    private Map<Integer, Integer> initLookupMap( ) {
        Map<Integer, Integer> mapping = new HashMap<>();
        mapping.put( 1, R.drawable.monkey_ladder_one );
        mapping.put( 2, R.drawable.monkey_ladder_two );
        mapping.put( 3, R.drawable.monkey_ladder_three );
        mapping.put( 4, R.drawable.monkey_ladder_four );
        mapping.put( 5, R.drawable.monkey_ladder_five );
        mapping.put( 6, R.drawable.monkey_ladder_six );
        mapping.put( 7, R.drawable.monkey_ladder_seven );
        mapping.put( 8, R.drawable.monkey_ladder_eight );
        mapping.put( 9, R.drawable.monkey_ladder_nine );
        mapping.put( 10, R.drawable.monkey_ladder_ten );
        mapping.put( 11, R.drawable.monkey_ladder_eleven );
        mapping.put( 12, R.drawable.monkey_ladder_twelve );
        mapping.put( 13, R.drawable.monkey_ladder_thirteen );
        mapping.put( 14, R.drawable.monkey_ladder_fourteen );
        mapping.put( 15, R.drawable.monkey_ladder_fifteen );
        mapping.put( 16, R.drawable.monkey_ladder_two );
        mapping.put( 17, R.drawable.monkey_ladder_two );
        mapping.put( 18, R.drawable.monkey_ladder_two );
        mapping.put( 19, R.drawable.monkey_ladder_two );
        mapping.put( 20, R.drawable.monkey_ladder_two );
        return mapping;
    }

    public Optional<Integer> drawableResourceIdFor( int id ) {
        return Optional.ofNullable( mapping.get( id ) );
    }

    public Map<Integer, Integer> getMapping( ) {
        return this.mapping; // not worried about internal data being modified as we're returning unmodifiable maps
    }
}
