package com.shapematch.game;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameLevelTest {

    @Test
    public void canCreateInstanceOfGameLevelOne() {
        new GameLevel(1);
    }

    @Test
    public void canCreateInstanceOfNextGameLevel() {
        GameLevel level2 = new GameLevel(1).nextLevel();

        assertEquals(2, level2.points());
    }

    @Test
    public void whenReachedMaxGameLevelThenNextLevelReturnsSameInstance() {
        GameLevel currentLvl = new GameLevel(GameLevel.MAX_LEVEL_SHAPE_COUNT);

        assertTrue(currentLvl == currentLvl.nextLevel());
    }
}
