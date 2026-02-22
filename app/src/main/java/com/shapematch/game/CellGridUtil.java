package com.shapematch.game;

import com.shapematch.data.ShapeSelector;
import com.shapematch.data.shapes.Shape;
import com.shapematch.data.shapes.ShapeSlotPair;
import com.shapematch.util.IntegerRange;
import com.shapematch.util.random.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CellGridUtil {

    public static CellGridPair getShapesForLevel(GameLevel currentLvl) {

        List<Shape> shapes = ShapeSelector.select(currentLvl);
        Set<Integer> slots = getSlotIndicesToPutShapesIn(shapes);

        List<ShapeSlotPair> shapeSlotPairs = zipAllShapesAndSlots(shapes, slots);

        if(shapeSlotPairs.size() != currentLvl.getShapeCount()){
            throw new RuntimeException("Expected to have " + currentLvl.getShapeCount() + " shapes and slots " +
                    "but have " + shapeSlotPairs.size());
        }

        CellGrid leftGrid = new CellGrid(shapeSlotPairs);
        CellGrid rightGrid = leftGrid.maybeAlterShapes();

        return new CellGridPair(leftGrid, rightGrid);
    }

    public static Set<Integer> getSlotIndicesToPutShapesIn(List<Shape> shapes) {
        return RandomNumberGenerator.next_N_DistinctNumbersWithinRange(shapes.size(),
                new IntegerRange(0, CellGrid.GRID_SIZE));
    }

    /**
     * implement Scala's zip functionality
     * * @return
     */
    public static List<ShapeSlotPair> zipAllShapesAndSlots(List<Shape> shapes , Set<Integer> slots) {
        if(shapes.size() != slots.size()) {
            throw new RuntimeException("Need the same number of shapes as the slots they go into. ");
        }

        List<ShapeSlotPair> pairs  = new ArrayList<>();

        int index=0;
        for(Integer slot : slots) {
            ShapeSlotPair pair  = new ShapeSlotPair(shapes.get(index++), slot);
            pairs.add(pair);
        }

        return pairs;
    }
}
