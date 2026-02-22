package com.tokensearch.game;

import com.tokensearch.ui.mainscreen.TokenSearchPresenter;

/**
 * Interface for search results in Token Search.
 * Uses the Visitor/Command pattern to handle different result types without switch statements.
 */
public interface SearchResult {
    void handle( TokenSearchPresenter presenter, int boxIndex );
}
