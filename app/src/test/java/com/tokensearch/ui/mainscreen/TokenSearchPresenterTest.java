package com.tokensearch.ui.mainscreen;

import android.os.Handler;

import com.monkeyladder.R;
import com.tokensearch.game.Empty;
import com.tokensearch.game.TokenFound;
import com.tokensearch.game.TokenSearchGame;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class TokenSearchPresenterTest {

    private TokenSearchViewContract mockView;
    private Handler mockHandler;
    private TokenSearchPresenter presenter;
    private TokenSearchGame game;

    @Before
    public void setUp() {
        mockView = mock( TokenSearchViewContract.class );
        mockHandler = mock( Handler.class );

        // Make handler execute runnables immediately
        doAnswer( invocation -> {
            Runnable runnable = invocation.getArgument( 0 );
            runnable.run();
            return true;
        } ).when( mockHandler ).postDelayed( any( Runnable.class ), anyLong() );

        Random random = new Random( 42 );
        BoxLayout layout = new BoxLayout( random );
        game = new TokenSearchGame( layout, random );

        presenter = new TokenSearchPresenter( mockView, game, mockHandler );
    }

    @Test
    public void startGameShowsBoxes() {
        presenter.startGame();

        verify( mockView ).showBoxes( any(), anyInt(), anyInt() );
    }

    @Test
    public void startGameUpdatesUI() {
        presenter.startGame();

        verify( mockView, atLeastOnce() ).updateScore( 0 );
        verify( mockView, atLeastOnce() ).updateLives( 3 );
        verify( mockView, atLeastOnce() ).updateLevel( 1 );
    }

    @Test
    public void startGameSetsStatusText() {
        presenter.startGame();

        verify( mockView ).setStatusText( R.string.token_search_tokens_left, 3 );
    }

    @Test
    public void tappingTokenRevealsToken() {
        presenter.startGame();

        // With seed 42, find a box that has the token
        int tokenIndex = -1;
        for ( int i = 0; i < game.boxCount(); i++ ) {
            if ( game.search( i ) instanceof TokenFound ) {
                tokenIndex = i;
                break;
            }
        }
        // Reset game to fresh state with same seed
        game = new TokenSearchGame( new BoxLayout( new Random( 42 ) ), new Random( 42 ) );
        presenter = new TokenSearchPresenter( mockView, game, mockHandler );
        presenter.startGame();

        presenter.onBoxTapped( tokenIndex );

        verify( mockView ).revealToken( tokenIndex );
        verify( mockView ).setStatusText( R.string.token_search_tokens_left, 2 );
    }

    @Test
    public void tappingEmptyBoxTriggersNotification() {
        presenter.startGame();

        int emptyIndex = -1;
        for ( int i = 0; i < game.boxCount(); i++ ) {
            if ( game.search( i ) instanceof Empty ) {
                emptyIndex = i;
                break;
            }
        }
        game = new TokenSearchGame( new BoxLayout( new Random( 42 ) ), new Random( 42 ) );
        presenter = new TokenSearchPresenter( mockView, game, mockHandler );
        presenter.startGame();

        presenter.onBoxTapped( emptyIndex );

        verify( mockView ).notifyEmptySearch();
    }

    @Test
    public void cleanupRemovesCallbacks() {
        presenter.cleanup();

        verify( mockHandler ).removeCallbacksAndMessages( null );
    }

    @Test
    public void tappingBeforeStartDoesNothing() {
        // Don't call startGame - input should be disabled
        presenter.onBoxTapped( 0 );

        verify( mockView, never() ).revealToken( anyInt() );
        verify( mockView, never() ).notifyEmptySearch();
        verify( mockView, never() ).showError( anyInt() );
    }
}
