package com.monkeyladder.game;

import java.util.Objects;

public class Cell {

    private final Location location;
    private final CellData data;

    public Cell( Location location, CellData data ) {
        this.location = location;
        this.data = data;
    }

    public Cell( Cell cell ) {
        this(cell.location, cell.data);
    }

    public Location getLocation( ) {
        return location;
    }

    public CellData getData( ) {
        return data;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Cell cell = ( Cell ) o;
        return location == cell.location &&
                data == cell.data;
    }

    @Override
    public int hashCode( ) {
        return Objects.hash( location, data );
    }

    @Override
    public String toString( ) {
        return "Cell{" +
                "location=" + location +
                ", data=" + data +
                '}';
    }
}
