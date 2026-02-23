package com.tokensearch.game;

/**
 * Interface for search results in Token Search.
 * Uses the Visitor/Command pattern to handle different result types without switch statements.
 */
public interface SearchResult {
    void handle( ResultHandler handler, int boxIndex );
}
