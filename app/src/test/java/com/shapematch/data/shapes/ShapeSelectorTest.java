package com.shapematch.data.shapes;

import com.shapematch.data.ShapeSelector;
import com.shapematch.game.GameLevel;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.shapematch.data.ShapeSelector.select;
import static com.shapematch.data.ShapeSelector.selectOneRandomShape;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ShapeSelectorTest {

    @Test
    public void givenSelectorAndGameLevelThenGetShapesReturnsShapesForGameLevel() {

        List<GameLevel> gameLevels = Arrays.asList(
                new GameLevel(1),
                new GameLevel(2),
                new GameLevel(200));

        for( GameLevel gameLevel : gameLevels) {
            assertEquals(gameLevel.points(), select(gameLevel).size());
        }
    }

    @Test
    public void givenSelectorThenOneRandomShapeCanBeSelected() {
        assertTrue(selectOneRandomShape() instanceof Shape);
    }

    @Test
    public void givenSelectorThenOneRandomShapeThatIsNotEqualToGivenShape() {
        HollowCircle shape = new HollowCircle();

        Shape selected =  ShapeSelector.selectOneRandomShapeOtherThan(shape);

        assertNotEquals(selected , shape);
    }
}
