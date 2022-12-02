package com.monkeyladder.util;

import com.monkeyladder.R;
import com.monkeyladder.game.Location;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CellLocationMapping {

    private final Map<Integer, Location> mapping;

    private final Map<Location, Integer> reverseMapping;

    public CellLocationMapping( ) {
        mapping = Collections.unmodifiableMap( initLookupMap() );
        reverseMapping = Collections.unmodifiableMap( initReverseLookupMap( mapping ) );
    }

    private Map<Integer, Location> initLookupMap( ) {
        Map<Integer, Location> mapping = new HashMap<>();
        mapping.put( R.id.text_00, Location.ZeroZero );
        mapping.put( R.id.text_01, Location.ZeroOne );
        mapping.put( R.id.text_02, Location.ZeroTwo );
        mapping.put( R.id.text_03, Location.ZeroThree );
        mapping.put( R.id.text_04, Location.ZeroFour );
        mapping.put( R.id.text_10, Location.OneZero );
        mapping.put( R.id.text_11, Location.OneOne );
        mapping.put( R.id.text_12, Location.OneTwo );
        mapping.put( R.id.text_13, Location.OneThree );
        mapping.put( R.id.text_14, Location.OneFour );
        mapping.put( R.id.text_20, Location.TwoZero );
        mapping.put( R.id.text_21, Location.TwoOne );
        mapping.put( R.id.text_22, Location.TwoTwo );
        mapping.put( R.id.text_23, Location.TwoThree );
        mapping.put( R.id.text_24, Location.TwoFour );
        mapping.put( R.id.text_30, Location.ThreeZero );
        mapping.put( R.id.text_31, Location.ThreeOne );
        mapping.put( R.id.text_32, Location.ThreeTwo );
        mapping.put( R.id.text_33, Location.ThreeThree );
        mapping.put( R.id.text_34, Location.ThreeFour );
        return mapping;
    }

    private Map<Location, Integer> initReverseLookupMap( Map<Integer, Location> mapping ) {
        final Map<Location, Integer> reverseMap = new HashMap<>();
        mapping.entrySet()
                .stream()
                .forEach( entry -> reverseMap.put( entry.getValue(), entry.getKey() ) );
        return reverseMap;
    }

    public Optional<Location> locationForResource( int id ) {
        return Optional.ofNullable( mapping.get( id ) );
    }

    public Optional<Integer> resourceIdForLocation( Location location ) {
        return Optional.ofNullable( reverseMapping.get( location ) );
    }

    public Map<Integer, Location> getMapping( ) {
        return this.mapping; // not worried about internal data being modified as we're returning unmodifiable maps
    }
}
