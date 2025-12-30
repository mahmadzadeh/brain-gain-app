package com.shapematch.game;

import com.shapematch.data.shapes.HollowSquare;
import com.shapematch.data.shapes.Shape;
import com.shapematch.data.shapes.ShapeSlotPair;
import com.shapematch.data.shapes.SolidSquare;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class CellGridUtilTest {

    @Test
    public void givenDisplayShapeObjectAndLevelOneThenGetShapesReturnsShapesToBeDisplayedOnTheRightAsWellAsLeftScreen() {
        CellGridPair cellGridPair = CellGridUtil.getShapesForLevel(new GameLevel(1));

        assertNotNull(cellGridPair);
    }

    @Test(expected = RuntimeException.class)
    public void givenUnequalNumberOfShapesAndSlotsThenZipAllShapesAndSlotsThrowsRuntime() {
        List<Shape> shapes = new ArrayList<>();
        shapes.add(new HollowSquare());

        CellGridUtil.zipAllShapesAndSlots(shapes, new HashSet<>());
    }

    @Test
    public void givenEqualNumberOfShapesAndSlotsThenZipAllShapesAndSlotsReturnsListOfShapeSlotPairs() {
        List<Shape> shapes = new ArrayList<>();
        shapes.add(new HollowSquare());

        Set<Integer> slots = new HashSet<>();
        slots.add(1);

        List<ShapeSlotPair> pairs = CellGridUtil.zipAllShapesAndSlots(shapes, slots);

        assertTrue(pairs.get(0).getShape() instanceof HollowSquare);
        assertTrue(1 == pairs.get(0).getSlot());
    }

    @Test
    public void givenMultipleShapesAndSlotsThenZipAllShapesAndSlotsReturnsListOfShapeSlotPairs() {
        List<Shape> shapes = new ArrayList<>();
        shapes.add(new HollowSquare());
        shapes.add(new SolidSquare());

        Set<Integer> slots = new HashSet<>();
        slots.add(1);
        slots.add(14);

        List<ShapeSlotPair> pairs = CellGridUtil.zipAllShapesAndSlots(shapes, slots);

        ShapeSlotPair firstPair = pairs.get(0);
        assertTrue(firstPair.getShape() instanceof HollowSquare);
        assertTrue(1 == firstPair.getSlot());

        ShapeSlotPair secondPair = pairs.get(1);
        assertTrue(secondPair.getShape() instanceof SolidSquare);
        assertTrue(14 == secondPair.getSlot());
    }
}
