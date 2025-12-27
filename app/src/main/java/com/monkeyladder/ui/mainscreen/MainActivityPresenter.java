package com.monkeyladder.ui.mainscreen;

import com.monkeyladder.game.GameState;
import com.monkeyladder.game.Location;
import com.monkeyladder.game.UserInputEvaluationResult;
import com.monkeyladder.util.CountDownTimerParameter;
import com.monkeyladder.util.GameCountDownTimer;
import com.monkeyladder.util.LevelBasedTimerParameter;

class MainActivityPresenter implements MainActivityPresenterContract {

    private final MainActivityViewContract view;
    private final MainActivityModelContract model;
    private final GameCountDownTimer timer;
    private final LevelBasedTimerParameter timerParam = new LevelBasedTimerParameter();

    public MainActivityPresenter( MainActivityViewContract viewContract, MainActivityModelContract model ) {
        this.view = viewContract;
        this.model = model;
        this.timer = GameCountDownTimer.INSTANCE( this );
    }

    @Override
    public void startOneRound( ) {
        view.setReadyToTakeUserInput( false );

        view.displayBoard( model.getCellsThatAreSet() );

        CountDownTimerParameter parameters = timerParam.getTimerParametersForLevel(
                model.getCurrentGameState().getLevel(), 800, 500 );

        startDisplayTimer( parameters.getTimerDurationInMillis(), parameters.getOneTickInMillis() );
    }

    public void startDisplayTimer( int timerDurationInMillis, int oneTickInMillis ) {
        timer.start( timerDurationInMillis, oneTickInMillis );
    }

    @Override
    public void addSelectedLocation( Location location ) {
        model.addSelectedLocation( location );

        if ( model.hasCollectedEnoughUserInput() ) {
            endOneRound();
        }
    }

    @Override
    public void setDisplayGameBoardProgress( int progress ) {
        view.updateDisplayBoardProgressBar( progress );
    }

    @Override
    public void setDisplayGameBoardCountdownText( String text ) {
        view.updateDisplayBoardCountdownText( text );
    }

    @Override
    public void onDisplayTimerFinish( ) {
        // Transition the displayed (numbered) cells into the "input" state (no numbers).
        view.setReadyToTakeUserInput( false );
        view.updateDisplayBoardCountdownText( "" );
        view.showInputBoard( model.getCellsThatAreSet() );
    }

    @Override
    public void endOneRound( ) {
        view.clearHighlightedCells();

        UserInputEvaluationResult result = model.evaluateUserInput();

        GameState gameState = model.updateGameState( result );

        view.updateGameStateInUI( gameState );

        model.resetGame();

        if ( UserInputEvaluationResult.Correct == result ) {
            view.displayUserSelectionCorrectFeedback();
        } else {
            view.displayUserSelectionIncorrectFeedback();
            view.updateLivesInUI( gameState.getLives() );
        }

        if ( model.isEndGame( result ) ) {
            view.onGameEnd( gameState );
        } else {
            startOneRound();
        }
    }

}
