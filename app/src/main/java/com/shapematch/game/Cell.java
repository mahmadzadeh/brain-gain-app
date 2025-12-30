package com.shapematch.game;

import com.shapematch.data.shapes.Shape;

public class Cell {

    private final Shape shape;

    public Cell(Shape shape) {
        this.shape = shape;
    }

    public String toString(){
        return "Cell with Shape: " +
                (shape !=null ? shape.toString() : " - " );
    }

    public Shape getShape() {
        return shape;
    }

    public int getShapeResourceId() {
        return shape.getResourceId();
    }

    public boolean equals(Object other ) {
        if ( this == other ) return true;
        if ( !(other instanceof Cell) ) return false;

        Cell that = (Cell)other;

        return this.shape.equals(that.shape);
    }

    @Override
    public int hashCode() {
        return shape.hashCode();
    }
}
