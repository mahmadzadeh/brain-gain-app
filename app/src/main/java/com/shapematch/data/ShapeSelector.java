package com.shapematch.data;

import static com.shapematch.util.random.RandomNumberGenerator.next_N_NumbersWithinRange;

import com.shapematch.data.shapes.FourSquare;
import com.shapematch.data.shapes.HollowCircle;
import com.shapematch.data.shapes.HollowCross;
import com.shapematch.data.shapes.HollowSquare;
import com.shapematch.data.shapes.HollowTriangle;
import com.shapematch.data.shapes.InvertedL;
import com.shapematch.data.shapes.ReverseL;
import com.shapematch.data.shapes.Shape;
import com.shapematch.data.shapes.SolidCircle;
import com.shapematch.data.shapes.SolidCross;
import com.shapematch.data.shapes.SolidSquare;
import com.shapematch.data.shapes.SolidTriangle;
import com.shapematch.game.GameLevel;
import com.shapematch.util.IntegerRange;
import com.shapematch.util.random.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShapeSelector {

    private static List<Shape> shapes = Arrays.asList(
            new FourSquare(),
            new HollowCircle(),
            new HollowCross(),
            new HollowSquare(),
            new HollowTriangle(),
            new InvertedL(),
            new ReverseL(),
            new SolidCircle(),
            new SolidCross(),
            new SolidSquare(),
            new SolidTriangle());

    private static IntegerRange shapesIndexRange = new IntegerRange(0, shapes.size() - 1);

    public static List<Shape> select(GameLevel gameLevel) {
        List<Integer> indecies = next_N_NumbersWithinRange(gameLevel.getShapeCount(), shapesIndexRange);

        List<Shape> selectedShapes = new ArrayList<>(gameLevel.getShapeCount());

        for (Integer index : indecies) {
            selectedShapes.add(shapes.get(index));
        }

        return selectedShapes;
    }

    public static Shape selectOneRandomShape() {
        return shapes.get(RandomNumberGenerator.next(shapesIndexRange));
    }

    public static Shape selectOneRandomShapeOtherThan(Shape shape) {
        Shape it;
        do {
            it = selectOneRandomShape();
        } while (it.equals(shape));

        return it;
    }
}
