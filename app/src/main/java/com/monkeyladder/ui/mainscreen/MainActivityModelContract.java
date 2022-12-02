package com.monkeyladder.ui.mainscreen;

import com.monkeyladder.game.GameState;
import com.monkeyladder.game.Location;
import com.monkeyladder.game.UserInputEvaluationResult;
import com.monkeyladder.util.LocationData;

import java.util.List;

interface MainActivityModelContract {

    List<LocationData> getCellsThatAreSet( );

    void addSelectedLocation( Location location );

    boolean hasCollectedEnoughUserInput( );

    UserInputEvaluationResult evaluateUserInput( );

    GameState updateGameState( UserInputEvaluationResult result );

    void resetGame( );

    boolean isEndGame( UserInputEvaluationResult result );

    GameState getCurrentGameState( );
}
