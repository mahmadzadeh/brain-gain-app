package com.monkeyladder.game;

public class GameState {

    private PlayerLives lives;
    private GameLevel level;
    private GameLevel maxLevelCompleted;
    private int score;

    public GameState( PlayerLives lives, GameLevel level, int score ) {
        this.lives = lives;
        this.level = level;
        this.maxLevelCompleted = level.previousLevelDown().orElse( null );
        this.score = score;
    }

    /**
     * Copy constructor
     *
     * @param gameState
     */
    public GameState( GameState gameState ) {
        this.lives = gameState.getLives();
        this.level = gameState.getLevel();
        this.score = gameState.score;
        this.maxLevelCompleted = gameState.maxLevelCompleted;
    }

    public PlayerLives getLives( ) {
        return lives;
    }

    public GameLevel getLevel( ) {
        return level;
    }

    public int getScore( ) {
        // Final score is the highest level successfully completed
        return maxLevelCompleted != null ? maxLevelCompleted.cellCount() : 0;
    }

    public void updateGameStateBasedOnResult( UserInputEvaluationResult result ) {
        if ( UserInputEvaluationResult.Correct == result ) {
            score += level.cellCount();
            
            // Mark current level as completed
            maxLevelCompleted = level;

            // if next level up is empty optional then they have reached the last level
            level = level.nextLevelUp().orElse( GameLevel.LevelSixteen );
        } else {
            lives = PlayerLives.fromLifeCount( lives.lifeCount - 1 );
            score -= level.cellCount();
            level = level.previousLevelDown().orElse( GameLevel.LevelOne );
        }
    }

    @Override
    public String toString( ) {
        return "GameState{" +
                "lives=" + lives +
                ", level=" + level +
                ", score=" + score +
                '}';
    }
}
