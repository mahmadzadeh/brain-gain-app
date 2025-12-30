package com.shapematch.data.shapes;

public class ShapeSlotPair {

    private final Shape shape;
    private final Integer slot;

    public ShapeSlotPair(Shape shape, Integer slot ) {
        this.shape = shape;
        this.slot = slot;
    }

    public Shape getShape() {
        return shape;
    }

    public Integer getSlot() {
        return slot;
    }

    @Override
    public boolean equals(Object other) {
        if ( this == other ) return true;
        if ( !(other instanceof ShapeSlotPair) ) return false;

        ShapeSlotPair that = (ShapeSlotPair)other;

        boolean shapesEquals = shape.equals(that.shape);
        boolean slotsAreEquals = slot.equals(that.slot);

        return shapesEquals && slotsAreEquals;
    }

    @Override
    public int hashCode() {
        int result = getShape().hashCode();
        result = 31 * result + getSlot().hashCode();
        return result;
    }
}
