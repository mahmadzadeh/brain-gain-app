package com.simonsays.game;

/**
 * Core game logic for Simon Says.
 * Manages the sequence, lives, score, and player input validation.
 */
public class SimonSaysGame {

    public static final int GRID_TOTAL_TILES = 16;
    public static final int INITIAL_LIVES = 3;

    private final Sequence sequence;
    private int lives;
    private int score;
    private int currentInputIndex;
    private GamePhase phase;

    public SimonSaysGame( Sequence sequence, int lives ) {
        this.sequence = sequence;
        this.lives = lives;
        this.score = 0;
        this.currentInputIndex = 0;
        this.phase = GamePhase.SHOWING_SEQUENCE;
    }

    /**
     * Starts a new round: extends the sequence by one and resets input index.
     */
    public void startNewRound() {
        sequence.addRandomPosition();
        currentInputIndex = 0;
        phase = GamePhase.SHOWING_SEQUENCE;
    }

    /**
     * Called when the sequence display is complete and the player can begin input.
     */
    public void onSequenceDisplayed() {
        phase = GamePhase.PLAYER_INPUT;
    }

    /**
     * Processes a player's tile tap.
     * Returns the resulting game phase after the tap.
     */
    public GamePhase handlePlayerTap( int tileIndex ) {
        if ( phase != GamePhase.PLAYER_INPUT ) return phase;

        int expected = sequence.getPosition( currentInputIndex );

        if ( tileIndex == expected ) {
            currentInputIndex++;
            if ( currentInputIndex >= sequence.length() ) {
                score = sequence.length();
                phase = GamePhase.ROUND_WON;
            }
        } else {
            lives--;
            currentInputIndex = 0;
            if ( lives <= 0 ) {
                phase = GamePhase.GAME_OVER;
            } else {
                phase = GamePhase.LIFE_LOST;
            }
        }

        return phase;
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    public GamePhase getPhase() {
        return phase;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public int getCurrentRound() {
        return sequence.length();
    }
}
