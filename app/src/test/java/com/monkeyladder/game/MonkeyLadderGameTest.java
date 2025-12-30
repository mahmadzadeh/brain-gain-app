package com.monkeyladder.game;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MonkeyLadderGameTest {

    private Location expectedLocation_1 = Location.ZeroZero;
    private Location expectedLocation_2 = Location.ZeroOne;
    private Cell cellToBeSelected_1 = new Cell( expectedLocation_1, CellData.One );
    private Cell cellToBeSelected_2 = new Cell( expectedLocation_2, CellData.Two );
    private MonkeyLadderGame monkeyLadderGame;

    private MonkeyLadderGameTestDataProvider testDataProvider;

    @Before
    public void setUp( ) {
        testDataProvider = new MonkeyLadderGameTestDataProvider();
    }

    @Test
    public void constructor( ) {
        monkeyLadderGame = new MonkeyLadderGame( new Board( BoardSize.FourByFive ), GameLevel.LevelTen, PlayerLives.getDefaultStartingValue() );
    }

    @Test
    public void givenLevelOne_whenCollectedOneUserSelectedLocation_thenGameHasEnoughUserInputLoc( ) {
        monkeyLadderGame = new MonkeyLadderGame( new Board( BoardSize.FourByFive ), GameLevel.LevelOne, PlayerLives.getDefaultStartingValue() );

        monkeyLadderGame.addUserSelectedLocation( Location.ZeroZero );

        assertTrue( monkeyLadderGame.hasEnoughUserSelectedInput() );
    }

    @Test
    public void givenLevelTwo_whenUserSelectsCellIncorrectOrder_thenEvaluateReturnsIncorrect( ) {

        List<MonkeyLadderGameTestDataProvider.TestBoardLocationData> testData =
                testDataProvider.levelTwoTestBoardData();

        Board board = constructBoardWithLocations( BoardSize.FourByFive, testData );

        monkeyLadderGame = new MonkeyLadderGame( board, GameLevel.LevelTwo, PlayerLives.getDefaultStartingValue() );

        List<Location> incorrectUserInput = swapTwoRandomLocations(
                testData.stream().map( d -> d.getLocation() ).collect( Collectors.toList() ) );

        simulateUserInput( monkeyLadderGame, incorrectUserInput );

        assertTrue( monkeyLadderGame.hasEnoughUserSelectedInput() );

        assertEquals( UserInputEvaluationResult.Incorrect, monkeyLadderGame.evaluate() );
    }

    @Test
    public void givenLevelTwo_whenUserSelectsCellCorrectOrder_thenEvaluateReturnsCorrect( ) {

        Location expectedLocation_1 = Location.ZeroZero;
        Location expectedLocation_2 = Location.ZeroOne;

        Cell cellToBeSelected_1 = new Cell( expectedLocation_1, CellData.One );
        Cell cellToBeSelected_2 = new Cell( expectedLocation_2, CellData.Two );

        Board board = new Board( BoardSize.FourByFive, Arrays.asList( cellToBeSelected_1, cellToBeSelected_2 ) );

        monkeyLadderGame = new MonkeyLadderGame( board, GameLevel.LevelTwo, PlayerLives.getDefaultStartingValue() );

        monkeyLadderGame.addUserSelectedLocation( expectedLocation_1 ); // order are
        monkeyLadderGame.addUserSelectedLocation( expectedLocation_2 );

        assertTrue( monkeyLadderGame.hasEnoughUserSelectedInput() );

        assertEquals( UserInputEvaluationResult.Correct, monkeyLadderGame.evaluate() );
    }

    @Test
    public void givenLevelFive_whenUserSelectsCellIncorrectOrder_thenEvaluateReturnsIncorrect( ) {

        List<MonkeyLadderGameTestDataProvider.TestBoardLocationData> testData =
                testDataProvider.levelFourTestBoardData();

        Board board = constructBoardWithLocations( BoardSize.FourByFive, testData );

        monkeyLadderGame = new MonkeyLadderGame( board, GameLevel.LevelFive, PlayerLives.getDefaultStartingValue() );

        List<Location> incorrectUserInput = swapTwoRandomLocations(
                testData.stream().map( d -> d.getLocation() ).collect( Collectors.toList() ) );

        simulateUserInput( monkeyLadderGame, incorrectUserInput );

        assertTrue( monkeyLadderGame.hasEnoughUserSelectedInput() );

        assertEquals( UserInputEvaluationResult.Incorrect, monkeyLadderGame.evaluate() );
    }

    @Test
    public void givenLevelFive_whenUserSelectsCellCorrectOrder_thenEvaluateReturnsCorrect( ) {

        List<MonkeyLadderGameTestDataProvider.TestBoardLocationData> data = testDataProvider.levelFourTestBoardData();

        Board board = constructBoardWithLocations( BoardSize.FourByFive, data );

        monkeyLadderGame = new MonkeyLadderGame( board, GameLevel.LevelFive, PlayerLives.getDefaultStartingValue() );

        List<Location> correctUserInput = data.stream().map( d -> d.getLocation() ).collect( Collectors.toList() );

        simulateUserInput( monkeyLadderGame, correctUserInput );

        assertTrue( monkeyLadderGame.hasEnoughUserSelectedInput() );

        assertEquals( UserInputEvaluationResult.Correct, monkeyLadderGame.evaluate() );
    }

    @Test
    public void playOneRoundOnFourByFiveBoardAndProvideCorrectSelection_thenGameStateIsUpdated( ) {
        Location expectedLocation_1 = Location.ZeroZero;
        Location expectedLocation_2 = Location.ZeroOne;

        Cell cellToBeSelected_1 = new Cell( expectedLocation_1, CellData.One );
        Cell cellToBeSelected_2 = new Cell( expectedLocation_2, CellData.Two );

        Board board = new Board( BoardSize.FourByFive, Arrays.asList( cellToBeSelected_1, cellToBeSelected_2 ) );

        monkeyLadderGame = new MonkeyLadderGame( board, GameLevel.LevelTwo, PlayerLives.getDefaultStartingValue() );

        monkeyLadderGame.addUserSelectedLocation( expectedLocation_1 ); // order are
        monkeyLadderGame.addUserSelectedLocation( expectedLocation_2 );

        assertTrue( monkeyLadderGame.hasEnoughUserSelectedInput() );

        UserInputEvaluationResult result = monkeyLadderGame.evaluate();

        assertEquals( UserInputEvaluationResult.Correct, result );

        monkeyLadderGame.updateGameState( result );
    }

    @Test
    public void givenAtLevelThreeAndNoLivesLeft_thenWrongSelectionWillEndGame( ) {

        OneRoundResult gameRunResult = playOneBadRound( GameLevel.LevelTwo ); // 2 - 1 = 1 life left

        assertEquals( GameLevel.LevelOne, gameRunResult.gameState.getLevel() );
        assertFalse( monkeyLadderGame.isGameEnd( gameRunResult.result ) );

        gameRunResult = playOneBadFollowUpRound();  // 1 - 1 = no life left

        assertEquals( GameLevel.LevelOne, gameRunResult.gameState.getLevel() );
        assertEquals( UserInputEvaluationResult.Incorrect, gameRunResult.result );
        assertFalse( monkeyLadderGame.isGameEnd( gameRunResult.result ) );

        gameRunResult = playOneBadFollowUpRound();  //last mistake - game end
        assertTrue( monkeyLadderGame.isGameEnd( gameRunResult.result ) );
    }

    private OneRoundResult playOneBadRound( GameLevel gameLevel ) {

        Board board = new Board( BoardSize.FourByFive, Arrays.asList( cellToBeSelected_1, cellToBeSelected_2 ) );

        monkeyLadderGame = new MonkeyLadderGame( board, gameLevel, PlayerLives.getDefaultStartingValue() );

        monkeyLadderGame.addUserSelectedLocation( expectedLocation_2 ); // wrong order are
        monkeyLadderGame.addUserSelectedLocation( expectedLocation_1 );

        UserInputEvaluationResult result = monkeyLadderGame.evaluate();

        return new OneRoundResult( monkeyLadderGame.updateGameState( result ), result );
    }

    private OneRoundResult playOneBadFollowUpRound( ) {
        // reset() generates a random board; for this test we need deterministic input that is always wrong.
        GameState currentState = monkeyLadderGame.getCurrentState();
        Board board = new Board( BoardSize.FourByFive, Arrays.asList( cellToBeSelected_1 ) );
        monkeyLadderGame = new MonkeyLadderGame( board, currentState.getLevel(), currentState.getLives() );

        monkeyLadderGame.addUserSelectedLocation( expectedLocation_2 ); // wrong order are
        monkeyLadderGame.addUserSelectedLocation( expectedLocation_1 );

        UserInputEvaluationResult result = monkeyLadderGame.evaluate();

        return new OneRoundResult( monkeyLadderGame.updateGameState( result ), result );
    }

    private List<Location> swapTwoRandomLocations( List<Location> collect ) {
        assert ( collect.size() >= 2 );

        Location loc = collect.get( 0 );
        collect.set( 0, collect.get( collect.size() - 1 ) );
        collect.set( collect.size() - 1, loc );

        return collect;
    }

    private void simulateUserInput( final MonkeyLadderGame game, final List<Location> userEnteredLocations ) {
        userEnteredLocations.stream().forEach( location -> game.addUserSelectedLocation( location ) );
    }

    private Board constructBoardWithLocations( BoardSize size, List<MonkeyLadderGameTestDataProvider.TestBoardLocationData> data ) {

        return new Board( size,
                data.stream()
                        .map( testData -> new Cell( testData.getLocation(), testData.getData() ) )
                        .collect( Collectors.toList() ) );
    }

    private class OneRoundResult {
        private GameState gameState;
        private UserInputEvaluationResult result;

        public OneRoundResult( GameState gameState, UserInputEvaluationResult result ) {
            this.gameState = gameState;
            this.result = result;
        }
    }

}