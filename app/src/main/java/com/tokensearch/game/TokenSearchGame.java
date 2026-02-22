package com.tokensearch.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Core game logic for Token Search.
 *
 * Player searches boxes to find hidden tokens. Each box hides a token exactly
 * once. Errors (re-searching or tapping already-found) cost a life and reset
 * the current search round.
 */
public class TokenSearchGame {

    public static final int INITIAL_LIVES = 3;
        public static final int STARTING_BOX_COUNT = 3;
    
        private final BoxLayout boxLayout;
    private final Random random;
    private final GameSession session;

    private List<Box> boxes;
    private int tokenBoxIndex;
    private Set<Integer> searchedThisRound;

    public TokenSearchGame( BoxLayout boxLayout, Random random ) {
        this.boxLayout = boxLayout;
        this.random = random;
        this.session = new GameSession( INITIAL_LIVES );
        startNewPuzzle( boxCountForLevel() );
    }

    public SearchResult search( int boxIndex ) {
        Box box = boxes.get( boxIndex );

        if ( box.isTokenFound() ) {
            return new AlreadyFound();
        }

        if ( searchedThisRound.contains( boxIndex ) ) {
            return new AlreadySearched();
        }

        searchedThisRound.add( boxIndex );

        if ( boxIndex == tokenBoxIndex ) {
            box.markTokenFound();
            session.incrementTokens( boxes.size() );
            searchedThisRound.clear();
            hideNewToken();
            return new TokenFound();
        }

        return new Empty();
    }

    public void loseLife() {
        session.loseLife();
    }

    public void resetPuzzle() {
        startNewPuzzle( boxCountForLevel() );
    }

    public boolean isLevelComplete() {
        return session.isLevelComplete( boxes.size() );
    }

    public void advanceLevel() {
        session.advanceLevel( boxes.size() );
        startNewPuzzle( boxCountForLevel() );
    }

    public boolean isGameOver() {
        return session.isGameOver();
    }

    public List<Box> boxes() {
        return new ArrayList<>( boxes );
    }

    public int lives() {
        return session.lives();
    }

    public int score() {
        return session.scoreValue();
    }

    public int level() {
        return session.level();
    }

    public int boxCount() {
        return boxes.size();
    }

    public int gridCols() {
        return boxLayout.cols();
    }

    public int gridRows() {
        return boxLayout.rows();
    }

    public int tokensRemaining() {
        return boxes.size() - session.tokensCollected();
    }

    private int boxCountForLevel() {
        return STARTING_BOX_COUNT + session.level() - 1;
    }

    private void startNewPuzzle( int count ) {
        boxes = boxLayout.generate( count );
        session.resetLevelProgress();
        searchedThisRound = new HashSet<>();
        hideNewToken();
    }

    private void hideNewToken() {
        List<Integer> unfound = IntStream.range( 0, boxes.size() )
            .filter( i -> !boxes.get( i ).isTokenFound() )
            .boxed()
            .collect( Collectors.toList() );

        if ( !unfound.isEmpty() ) {
            tokenBoxIndex = unfound.get( random.nextInt( unfound.size() ) );
        }
    }
}
