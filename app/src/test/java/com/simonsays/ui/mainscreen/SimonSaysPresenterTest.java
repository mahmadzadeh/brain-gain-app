package com.simonsays.ui.mainscreen;

import android.os.Handler;

import com.simonsays.game.GamePhase;
import com.simonsays.game.Sequence;
import com.simonsays.game.SimonSaysGame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith( MockitoJUnitRunner.class )
public class SimonSaysPresenterTest {

    @Mock private SimonSaysViewContract view;
    @Mock private Handler handler;

    private SimonSaysPresenter presenter;
    private SimonSaysGame game;
    private Sequence sequence;

    @Before
    public void setUp() {
        sequence = new Sequence( 16, new Random( 42 ) );
        game = new SimonSaysGame( sequence, 3 );

        // Execute all handler.postDelayed runnables immediately
        doAnswer( invocation -> {
            Runnable runnable = invocation.getArgument( 0 );
            runnable.run();
            return true;
        } ).when( handler ).postDelayed( any( Runnable.class ), anyLong() );

        presenter = new SimonSaysPresenter( view, game, handler );
    }

    @Test
    public void startGameUpdatesInitialState() {
        presenter.startGame();

        verify( view ).updateScore( 0 );
        verify( view ).updateLives( 3 );
    }

    @Test
    public void startGameShowsSequence() {
        presenter.startGame();

        // Should flash at least one tile (the first sequence position)
        verify( view, atLeastOnce() ).flashTile( anyInt() );
    }

    @Test
    public void startGameDisablesTilesDuringSequence() {
        presenter.startGame();

        verify( view, atLeastOnce() ).setTilesEnabled( false );
    }

    @Test
    public void startGameEnablesTilesAfterSequence() {
        presenter.startGame();

        // Since handler executes immediately, tiles should be re-enabled
        verify( view, atLeastOnce() ).setTilesEnabled( true );
    }

    @Test
    public void correctTapFlashesTile() {
        presenter.startGame();

        int expected = sequence.getPosition( 0 );
        presenter.onTileTapped( expected );

        // Should flash the tapped tile (at least once for tap feedback + sequence display)
        verify( view, atLeastOnce() ).flashTile( expected );
    }

    @Test
    public void correctTapUpdatesScoreOnRoundWon() {
        presenter.startGame();

        int expected = sequence.getPosition( 0 );
        presenter.onTileTapped( expected );

        // Score should be updated to 1 after completing round 1
        verify( view, atLeastOnce() ).updateScore( 1 );
    }

    @Test
    public void wrongTapUpdatesLives() {
        presenter.startGame();

        int expected = sequence.getPosition( 0 );
        int wrong = ( expected + 1 ) % 16;
        presenter.onTileTapped( wrong );

        verify( view, atLeastOnce() ).updateLives( 2 );
    }

    @Test
    public void wrongTapShowsErrorMessage() {
        presenter.startGame();

        int expected = sequence.getPosition( 0 );
        int wrong = ( expected + 1 ) % 16;
        presenter.onTileTapped( wrong );

        verify( view, atLeastOnce() ).setStatusText( "Wrong! Try again..." );
    }

    @Test
    public void gameOverCallsOnGameOver() {
        SimonSaysGame oneLifeGame = new SimonSaysGame(
                new Sequence( 16, new Random( 42 ) ), 1 );
        SimonSaysPresenter oneLifePresenter = new SimonSaysPresenter(
                view, oneLifeGame, handler );

        oneLifePresenter.startGame();

        int expected = oneLifeGame.getSequence().getPosition( 0 );
        int wrong = ( expected + 1 ) % 16;
        oneLifePresenter.onTileTapped( wrong );

        verify( view ).onGameOver( anyInt() );
    }

    @Test
    public void gameOverShowsGameOverMessage() {
        SimonSaysGame oneLifeGame = new SimonSaysGame(
                new Sequence( 16, new Random( 42 ) ), 1 );
        SimonSaysPresenter oneLifePresenter = new SimonSaysPresenter(
                view, oneLifeGame, handler );

        oneLifePresenter.startGame();

        int expected = oneLifeGame.getSequence().getPosition( 0 );
        int wrong = ( expected + 1 ) % 16;
        oneLifePresenter.onTileTapped( wrong );

        verify( view, atLeastOnce() ).setStatusText( "Game Over!" );
    }

    @Test
    public void tapIgnoredWhenNotPlayerInput() {
        // Before starting game, phase is SHOWING_SEQUENCE
        presenter.onTileTapped( 0 );

        // View should not receive any flash for this tap
        verify( view, never() ).flashTile( anyInt() );
    }

    @Test
    public void cleanupRemovesHandlerCallbacks() {
        presenter.cleanup();
        verify( handler ).removeCallbacksAndMessages( null );
    }

    @Test
    public void roundWonStartsNextRound() {
        presenter.startGame();

        // Complete round 1
        int expected = sequence.getPosition( 0 );
        presenter.onTileTapped( expected );

        // After round won, should update round number
        verify( view, atLeastOnce() ).updateRound( 2 );
    }

    @Test
    public void lifeLostReplaysSequence() {
        presenter.startGame();

        int expected = sequence.getPosition( 0 );
        int wrong = ( expected + 1 ) % 16;
        presenter.onTileTapped( wrong );

        // After life lost, should show "Watch..." again for replay
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass( String.class );
        verify( view, atLeastOnce() ).setStatusText( captor.capture() );

        boolean hasWatch = captor.getAllValues().stream()
                .anyMatch( s -> s.equals( "Watch..." ) );
        assertEquals( "Expected 'Watch...' status after life lost", true, hasWatch );
    }
}
