package com.tokensearch.game;

public class Empty implements SearchResult {
    @Override
    public void handle( ResultHandler handler, int boxIndex ) {
        handler.onEmptyBoxTapped();
    }
}
