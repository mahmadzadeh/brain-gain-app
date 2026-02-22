package com.tokensearch.ui.mainscreen;

import android.os.Handler;

import com.monkeyladder.R;
import com.tokensearch.game.SearchResult;
import com.tokensearch.game.TokenSearchGame;

public class TokenSearchPresenter {

    private static final long ERROR_DISPLAY_MS = 800;
    private static final long LEVEL_COMPLETE_DELAY_MS = 1200;
    private static final long TOKEN_REVEAL_MS = 500;

    private final TokenSearchViewContract view;
    private final TokenSearchGame game;
    private final Handler handler;
    private boolean inputEnabled;

    public TokenSearchPresenter( TokenSearchViewContract view,
                                  TokenSearchGame game,
                                  Handler handler ) {
        this.view = view;
        this.game = game;
        this.handler = handler;
        this.inputEnabled = false;
    }

    public void startGame() {
        updateUI();
        showPuzzle();
    }

    public void onBoxTapped( int boxIndex ) {
        if ( !inputEnabled ) {
            return;
        }

        SearchResult result = game.search( boxIndex );
        result.handle( this, boxIndex );
    }

    public void onTokenFound( int boxIndex ) {
        inputEnabled = false;
        view.revealToken( boxIndex );
        handler.postDelayed( () -> handleTokenFound( boxIndex ), TOKEN_REVEAL_MS );
    }

    public void onEmptyBoxTapped() {
        view.notifyEmptySearch();
    }

    public void onErrorTapped( int boxIndex ) {
        inputEnabled = false;
        view.showError( boxIndex );
        game.loseLife();
        updateUI();

        if ( game.isGameOver() ) {
            handler.postDelayed( () -> view.onGameOver( game.score() ), ERROR_DISPLAY_MS );
        } else {
            handler.postDelayed( () -> {
                game.resetPuzzle();
                showPuzzle();
            }, ERROR_DISPLAY_MS );
        }
    }

    public void cleanup() {
        handler.removeCallbacksAndMessages( null );
    }

    private void handleTokenFound( int boxIndex ) {
        view.hideToken( boxIndex );
        updateUI();

        if ( game.isLevelComplete() ) {
            view.setStatusText( R.string.token_search_level_complete );
            handler.postDelayed( () -> {
                game.advanceLevel();
                showPuzzle();
            }, LEVEL_COMPLETE_DELAY_MS );
        } else {
            inputEnabled = true;
            view.setStatusText( R.string.token_search_tokens_left, game.tokensRemaining() );
        }
    }

    private void showPuzzle() {
        view.showBoxes( game.boxes(), game.gridCols(), game.gridRows() );
        updateUI();
        view.setStatusText( R.string.token_search_tokens_left, game.tokensRemaining() );
        inputEnabled = true;
    }

    private void updateUI() {
        view.updateScore( game.score() );
        view.updateLives( game.lives() );
        view.updateLevel( game.level() );
    }
}
