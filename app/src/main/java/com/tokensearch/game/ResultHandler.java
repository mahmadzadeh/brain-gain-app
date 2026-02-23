package com.tokensearch.game;

/**
 * Interface for handling search results.
 * Implemented by the Presenter to receive polymorphic callbacks from the Game.
 */
public interface ResultHandler {
    void onTokenFound( int boxIndex );
    void onEmptyBoxTapped();
    void onErrorTapped( int boxIndex );
}
