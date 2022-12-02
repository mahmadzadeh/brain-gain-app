package com.util;

public class RandomBoolean {

    public static Boolean nextRandomTrue( Integer n ) {
        return RandomNumberGenerator.next( new IntegerRange( 1, n ) ) >= ( n / 2 );
    }

    public static Boolean nextRandomTrue( ) {
        return RandomNumberGenerator.next( new IntegerRange( 1, 100 ) ) >= ( 100 / 2 );
    }

}
