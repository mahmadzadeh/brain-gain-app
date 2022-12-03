package com.monkeyladder.game;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameStateTest {

    @Test
    public void givenInitialGameStateAndCorrectUserInput_thenUpdateWillUpdateItBasedOnUserInput( ) {
        GameState state = new GameState( PlayerLives.getDefaultStartingValue(), GameLevel.LevelThree, 0 );

        state.updateGameStateBasedOnResult( UserInputEvaluationResult.Correct );

        assertEquals( PlayerLives.getDefaultStartingValue(), state.getLives() );
        assertEquals( GameLevel.LevelFour, state.getLevel() );
        assertEquals( 0 + GameLevel.LevelThree.cellCount(), state.getScore() );

    }

    @Test
    public void givenInitialGameStateAndIncorrectUserInput_thenUpdateWillUpdateItBasedOnUserInput( ) {
        GameState state = new GameState( PlayerLives.Three, GameLevel.LevelThree, 3 );

        state.updateGameStateBasedOnResult( UserInputEvaluationResult.Incorrect );

        assertEquals( PlayerLives.Two, state.getLives() );
        assertEquals( GameLevel.LevelTwo, state.getLevel() );
        assertEquals( 0 , state.getScore() );
    }

    @Test
    public void givenPlayerReachedLastLevel_thenUpdateWillNotLevelUp( ) {

        GameLevel lastLevel = GameLevel.lastLevel();

        GameState state = new GameState( PlayerLives.Three, lastLevel, 3 );

        state.updateGameStateBasedOnResult( UserInputEvaluationResult.Correct );

        assertEquals( PlayerLives.Three, state.getLives() );

        assertEquals( lastLevel, state.getLevel() );

        assertEquals( 3+lastLevel.cellCount() , state.getScore() );
    }

}