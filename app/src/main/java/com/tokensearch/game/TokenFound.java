package com.tokensearch.game;

public class TokenFound implements SearchResult {
    @Override
    public void handle( ResultHandler handler, int boxIndex ) {
        handler.onTokenFound( boxIndex );
    }
}
