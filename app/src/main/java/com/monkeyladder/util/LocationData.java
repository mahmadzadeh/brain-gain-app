package com.monkeyladder.util;

import com.monkeyladder.game.Location;

public class LocationData {
    private final Location location;

    private final int data;

    public LocationData( Location location, int data ) {
        this.location = location;
        this.data = data;
    }

    public Location getLocation( ) {
        return location;
    }

    public int getData( ) {
        return data;
    }

    @Override
    public String toString( ) {
        return "LocationData{" +
                "location=" + location +
                ", data=" + data +
                '}';
    }
}
