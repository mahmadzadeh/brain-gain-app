package com.monkeyladder.util.random;

import com.monkeyladder.game.Cell;
import com.monkeyladder.game.GameLevel;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class RandomBoardGeneratorTest {


    @Test
    public void givenNullLevel_thenCodeHandlesNullCase( ) {
        GameLevel level = GameLevel.Null;

        List<Cell> cells = RandomBoardGenerator.nextTrialForLevel( level );

        assertEquals( 0, cells.size() );
    }

    @Test
    public void givenLevelOne_thenNextTrialForLevelReturnsSingleBoardCell( ) {
        GameLevel level = GameLevel.LevelOne;

        List<Cell> cells = RandomBoardGenerator.nextTrialForLevel( level );

        assertEquals( 1, cells.size() );
    }

    @Test
    public void givenLevelTen_thenNextTrialForLevelReturnsTenBoardCell( ) {
        GameLevel level = GameLevel.LevelOne;

        List<Cell> cells = RandomBoardGenerator.nextTrialForLevel( level );

        assertEquals( 1, cells.size() );
    }
}