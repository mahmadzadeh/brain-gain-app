package com.tokensearch.game;

/**
 * Encapsulates the state of a game session including score, lives, and level progression.
 */
public class GameSession {
    private final Score score;
    private int lives;
    private int level;
    private int tokensCollectedThisLevel;

    public GameSession( int initialLives ) {
        this.score = new Score();
        this.lives = initialLives;
        this.level = 1;
        this.tokensCollectedThisLevel = 0;
    }

    public int lives() {
        return lives;
    }

    public int level() {
        return level;
    }

    public int scoreValue() {
        return score.value();
    }

    public int tokensCollected() {
        return tokensCollectedThisLevel;
    }

    public void incrementTokens( int totalInLevel ) {
        tokensCollectedThisLevel++;
    }

    public void loseLife() {
        lives--;
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    public boolean isLevelComplete( int totalInLevel ) {
        return tokensCollectedThisLevel >= totalInLevel;
    }

    public void advanceLevel( int totalInLevel ) {
        score.onLevelCompleted( totalInLevel );
        level++;
        tokensCollectedThisLevel = 0;
    }

    public void resetLevelProgress() {
        tokensCollectedThisLevel = 0;
    }
}
