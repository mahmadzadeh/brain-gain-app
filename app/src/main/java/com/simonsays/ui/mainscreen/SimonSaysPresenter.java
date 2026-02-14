package com.simonsays.ui.mainscreen;

import android.os.Handler;

import com.simonsays.game.GamePhase;
import com.simonsays.game.SimonSaysGame;

import java.util.List;

/**
 * Orchestrates the Simon Says game flow:
 * show sequence → accept player input → validate → next round or lose life.
 */
public class SimonSaysPresenter {

    private static final long FLASH_DURATION_MS = 400;
    private static final long FLASH_GAP_MS = 200;
    private static final long ROUND_DELAY_MS = 800;

    private final SimonSaysViewContract view;
    private final SimonSaysGame game;
    private final Handler handler;

    public SimonSaysPresenter( SimonSaysViewContract view, SimonSaysGame game, Handler handler ) {
        this.view = view;
        this.game = game;
        this.handler = handler;
    }

    public void startGame() {
        view.updateScore( game.getScore() );
        view.updateLives( game.getLives() );
        view.updateRound( 0 );
        startNewRound();
    }

    private void startNewRound() {
        game.startNewRound();
        view.updateRound( game.getCurrentRound() );
        view.setTilesEnabled( false );
        view.setStatusText( "Watch..." );

        showSequence();
    }

    private void showSequence() {
        List<Integer> positions = game.getSequence().getPositions();

        for ( int i = 0; i < positions.size(); i++ ) {
            final int tileIndex = positions.get( i );
            long flashStart = (long) i * ( FLASH_DURATION_MS + FLASH_GAP_MS );

            handler.postDelayed( () -> view.flashTile( tileIndex ), flashStart );
            handler.postDelayed( () -> view.resetTile( tileIndex ), flashStart + FLASH_DURATION_MS );
        }

        long sequenceEnd = (long) positions.size() * ( FLASH_DURATION_MS + FLASH_GAP_MS );
        handler.postDelayed( () -> {
            game.onSequenceDisplayed();
            view.setTilesEnabled( true );
            view.setStatusText( "Your turn!" );
        }, sequenceEnd );
    }

    public void onTileTapped( int tileIndex ) {
        if ( game.getPhase() != GamePhase.PLAYER_INPUT ) return;

        // Brief flash on tap
        view.flashTile( tileIndex );
        handler.postDelayed( () -> view.resetTile( tileIndex ), 150 );

        GamePhase result = game.handlePlayerTap( tileIndex );

        switch ( result ) {
            case ROUND_WON:
                view.updateScore( game.getScore() );
                view.setStatusText( "Correct!" );
                view.setTilesEnabled( false );
                handler.postDelayed( this::startNewRound, ROUND_DELAY_MS );
                break;

            case LIFE_LOST:
                view.updateLives( game.getLives() );
                view.setStatusText( "Wrong! Try again..." );
                view.setTilesEnabled( false );
                handler.postDelayed( () -> {
                    view.setStatusText( "Watch..." );
                    replayCurrentSequence();
                }, ROUND_DELAY_MS );
                break;

            case GAME_OVER:
                view.updateLives( 0 );
                view.setStatusText( "Game Over!" );
                view.setTilesEnabled( false );
                handler.postDelayed( () -> view.onGameOver( game.getScore() ), ROUND_DELAY_MS );
                break;

            default:
                break;
        }
    }

    private void replayCurrentSequence() {
        game.onSequenceDisplayed();
        showSequence();
    }

    public void cleanup() {
        handler.removeCallbacksAndMessages( null );
    }
}
