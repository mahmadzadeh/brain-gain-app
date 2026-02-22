package com.monkeyladder.game;

import static com.monkeyladder.game.UserInputEvaluationResult.Correct;
import static com.monkeyladder.game.UserInputEvaluationResult.Incorrect;
import static com.monkeyladder.util.random.RandomBoardGenerator.nextTrialForLevel;

import java.util.ArrayList;
import java.util.List;

public class MonkeyLadderGame {

    private static final BoardSize DEFAULT_BOARD_SIZE = BoardSize.FourByFive;
    private final GameState gameState;
    private Board board;
    private List<Location> userSelectedLocations;

    public MonkeyLadderGame( Board board, GameLevel level, PlayerLives lives ) {
        this.board = board;
        this.userSelectedLocations = new ArrayList<>();
        this.gameState = new GameState( lives, level, 0 );
    }

    public MonkeyLadderGame( GameLevel level ) {
        this( new Board( DEFAULT_BOARD_SIZE, nextTrialForLevel( level ) ), level, PlayerLives.getDefaultStartingValue() );
    }

    public List<Cell> getCurrentCellsSetOnBoard( ) {

        return board.getCellsThatAreNotEmpty();
    }

    public void addUserSelectedLocation( Location location ) {
        userSelectedLocations.add( location );
    }

    public void resetUserSelectedLocations( ) {
        userSelectedLocations = new ArrayList<>();
    }

    public boolean hasEnoughUserSelectedInput( ) {
        return userSelectedLocations.size() == gameState.getLevel().cellCount();
    }

    public UserInputEvaluationResult evaluate( ) {

        List<Cell> nonEmptyCells = board.getCellsThatAreNotEmpty();

        for ( int index = 0; index < nonEmptyCells.size(); index++ ) {
            Cell cell = nonEmptyCells.get( index );
            Location location = userSelectedLocations.get( index );
            if ( cell.getLocation() != location ) {
                return Incorrect;
            }
        }

        return Correct;
    }

    public GameState updateGameState( UserInputEvaluationResult result ) {
        gameState.updateGameStateBasedOnResult( result );

        return gameState;
    }

    public void reset( ) {
        this.board = new Board( DEFAULT_BOARD_SIZE, nextTrialForLevel( gameState.getLevel() ) );

        resetUserSelectedLocations();
    }

    public boolean isGameEnd( UserInputEvaluationResult result ) {
        return Incorrect == result
                && gameState.getLives().lifeCount <= 0;
    }

    public GameState getCurrentState( ) {
        return new GameState( this.gameState );
    }
}
