package com.tokensearch.game;

public class AlreadyFound implements SearchResult {
    @Override
    public void handle( ResultHandler handler, int boxIndex ) {
        handler.onErrorTapped( boxIndex );
    }
}
