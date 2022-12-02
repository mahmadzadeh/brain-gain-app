package com.monkeyladder.ui.mainscreen;

import com.monkeyladder.game.Cell;
import com.monkeyladder.game.GameState;
import com.monkeyladder.game.Location;
import com.monkeyladder.game.MonkeyLadderGame;
import com.monkeyladder.game.UserInputEvaluationResult;
import com.monkeyladder.util.LocationData;

import java.util.List;
import java.util.stream.Collectors;

class MainScreenModel implements MainActivityModelContract {

    private final MonkeyLadderGame monkeyLadderGame;

    MainScreenModel( MonkeyLadderGame monkeyLadderGame ) {
        this.monkeyLadderGame = monkeyLadderGame;
    }

    @Override
    public List<LocationData> getCellsThatAreSet( ) {
        return convert( monkeyLadderGame.getCurrentCellsSetOnBoard() );
    }

    @Override
    public void addSelectedLocation( Location location ) {
        this.monkeyLadderGame.addUserSelectedLocation( location );
    }

    @Override
    public boolean hasCollectedEnoughUserInput( ) {
        return monkeyLadderGame.hasEnoughUserSelectedInput();
    }

    @Override
    public UserInputEvaluationResult evaluateUserInput( ) {
        return monkeyLadderGame.evaluate();
    }

    @Override
    public GameState updateGameState( UserInputEvaluationResult result ) {
        return monkeyLadderGame.updateGameState( result );
    }

    @Override
    public void resetGame( ) {
        monkeyLadderGame.reset();
    }

    @Override
    public boolean isEndGame( UserInputEvaluationResult result ) {
        return monkeyLadderGame.isGameEnd( result );
    }

    @Override
    public GameState getCurrentGameState( ) {
        return monkeyLadderGame.getCurrentState();
    }

    private List<LocationData> convert( List<Cell> setCells ) {
        return setCells
                .stream()
                .map( cell -> new LocationData( cell.getLocation(), cell.getData().getData() ) )
                .collect( Collectors.toList() );
    }
}
