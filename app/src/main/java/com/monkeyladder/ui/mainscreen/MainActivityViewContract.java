package com.monkeyladder.ui.mainscreen;

import com.monkeyladder.game.GameState;
import com.monkeyladder.game.PlayerLives;
import com.monkeyladder.util.LocationData;

import java.util.List;

interface MainActivityViewContract {

    void displayBoard( List<LocationData> board );

    void clearHighlightedCells( );

    void clearScreen( );

    void showInputBoard( List<LocationData> board );

    void updateDisplayBoardProgressBar( int progress );

    void updateDisplayBoardCountdownText( String text );

    void displayUserSelectionCorrectFeedback( );

    void displayUserSelectionIncorrectFeedback( );

    void updateUserInputFeedBackImage( );

    void updateGameStateInUI( GameState gameState );

    void setReadyToTakeUserInput( boolean isReady );

    void onGameEnd( GameState gameState );

    void updateLivesInUI( PlayerLives lives );
}
