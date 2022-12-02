package com.monkeyladder.util;

import java.util.Objects;

public class CountDownTimerParameter {

    private final int timerDurationInMillis;
    private final int oneTickInMillis;

    public CountDownTimerParameter( int timerDurationInMillis, int oneTickInMillis ) {
        this.timerDurationInMillis = timerDurationInMillis;
        this.oneTickInMillis = oneTickInMillis;
    }

    public static CountDownTimerParameter of( int timerDurationInMillis, int oneTickInMillis ) {
        return new CountDownTimerParameter( timerDurationInMillis, oneTickInMillis );
    }

    public int getTimerDurationInMillis( ) {
        return timerDurationInMillis;
    }

    public int getOneTickInMillis( ) {
        return oneTickInMillis;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        CountDownTimerParameter parameter = ( CountDownTimerParameter ) o;
        return getTimerDurationInMillis() == parameter.getTimerDurationInMillis() && getOneTickInMillis() == parameter.getOneTickInMillis();
    }

    @Override
    public int hashCode( ) {
        return Objects.hash( getTimerDurationInMillis(), getOneTickInMillis() );
    }
}
