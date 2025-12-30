package com.shapematch.data.shapes;

public abstract class Shape {

    Integer resourceId;

    public String toString() {
        return this.getClass().getSimpleName();
    }

    public boolean equals(Object other ) {
        if ( this == other ) return true;
        if ( !(other instanceof Shape) ) return false;

        Shape that = (Shape)other;

        return this.resourceId.equals(that.resourceId);
    }

    public Integer getResourceId() {
        return resourceId;
    }

    @Override
    public int hashCode() {
        return resourceId.hashCode();
    }
}
