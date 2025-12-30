package com.shapematch.game;

import com.shapematch.data.shapes.HollowSquare;
import com.shapematch.data.shapes.ShapeSlotPair;
import com.shapematch.data.shapes.SolidCircle;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.shapematch.game.ShapeMatchGame.REQUIRED_CORRECT_CONSECUTIVE_ANSWERS;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class ShapeMatchGameTest {

    GameLevel levelOne = GameLevel.initialLevel;
    GameLevel levelTwo = levelOne.nextLevel();
    GameLevel levelThree = levelTwo.nextLevel();

    List<ShapeSlotPair> matchingShapesSlotPairs = Arrays.asList(
        new ShapeSlotPair(new HollowSquare(), 0),
        new ShapeSlotPair(new HollowSquare(), 1)
    );

    List<ShapeSlotPair> nonMatchingShapes = Arrays.asList(
        new ShapeSlotPair(new HollowSquare(), 0),
        new ShapeSlotPair(new SolidCircle(), 1)
    );

    CellGridPair matchingDisplayShapesPair = new CellGridPair(
            new CellGrid(matchingShapesSlotPairs),
            new CellGrid(matchingShapesSlotPairs));

    CellGridPair nonMatchingDisplayShapesPair = new CellGridPair(
            new CellGrid(matchingShapesSlotPairs),
            new CellGrid(nonMatchingShapes));


    @Test
    public void canCreateInstance() {
        new ShapeMatchGame(levelOne, matchingDisplayShapesPair, 0, new Score(0), false);
    }

    @Test
    public void givenAGameWhenUserSelectsMatchAndLeftAndRightMatchAndFirstConsecutiveCorrectAnswerThenIsUserInputCorrectReturnsGameLogicWithSameLevel() {
        UserInput userInput = UserInput.Match;

        ShapeMatchGame gl = new ShapeMatchGame(levelOne, matchingDisplayShapesPair, 0, new Score(0), false).evaluateUserInput(userInput);

        assertEquals(gl.currentLevel(), levelOne);
        assertEquals(1, gl.correctAnswers());
    }

    @Test
    public void givenAGameWhenUserChoosesWronglyThenPointsDeducted() {
        UserInput userInput = UserInput.Mismatch;
        ShapeMatchGame gl = new ShapeMatchGame(levelOne, matchingDisplayShapesPair, 0, new Score(0), false).evaluateUserInput(userInput);

        assertEquals(gl.currentLevel(), levelOne);

        assertEquals(-1, gl.score().points());
    }

    @Test
    public void givenGameWhenUserMakesNConsecutiveCorrectAnswersThenLevelIncreased() {

        ShapeMatchGame gl = makeNConsecutiveCorrectGuess(REQUIRED_CORRECT_CONSECUTIVE_ANSWERS,
                new ShapeMatchGame(levelOne, matchingDisplayShapesPair, 0, new Score(0), false));

        assertEquals(levelTwo, gl.currentLevel());
    }

    @Test
    public void giveGameWhenUserMakesFourConsecutiveCorrectGuessesThenLevelIsIncreasedToThree() {

        ShapeMatchGame gl = makeNConsecutiveCorrectGuess(REQUIRED_CORRECT_CONSECUTIVE_ANSWERS * 2,
                new ShapeMatchGame(levelOne, matchingDisplayShapesPair, 0, new Score(0), false));

        assertEquals(levelThree, gl.currentLevel());
    }

    @Test
    public void givenGameWhenUserSelectsMismatchAndLeftAndRightMatchThenGameRemainsInCurrentLevel() {
        UserInput userInput = UserInput.Mismatch;

        ShapeMatchGame gl = new ShapeMatchGame(levelOne, matchingDisplayShapesPair, 0, new Score(0), false).evaluateUserInput(userInput);

        assertEquals(gl.currentLevel(), levelOne);
    }

    @Test
    public void givenGameWhenUserSelectsCorrectlyThenNextLevelHasShapeCountCorrespondingToTheLevel() {
        UserInput userInput = UserInput.Match;

        ShapeMatchGame gl = makeNConsecutiveCorrectGuess(REQUIRED_CORRECT_CONSECUTIVE_ANSWERS,
                new ShapeMatchGame(levelOne, matchingDisplayShapesPair, 0, new Score(0), false));

        assertEquals(gl.cellGridPair().leftGrid().getShapeSlotPairCount(), levelTwo.getShapeCount());
        assertEquals(gl.cellGridPair().rightGrid().getShapeSlotPairCount(), levelTwo.getShapeCount());
    }

    @Test
    public void givenGameWhenGameMarkedAsFinishedThenIsGameOverReturnsTrue() {
        assertTrue(
                new ShapeMatchGame(levelOne, matchingDisplayShapesPair, 0, new Score(0), false).markGameAsFinished().isGameOver());
    }

    @Test
    public void givenGameWhenResetIsCalledThenNewInstanceOfGameIsReturnedWithInitialState() {
        ShapeMatchGame newGame = new ShapeMatchGame(new GameLevel(10), matchingDisplayShapesPair, 12, new Score(2222), false).reset();

        assertEquals( 1 , newGame.currentLevel().getShapeCount());
        assertEquals( 1 , newGame.cellGridPair().leftGrid().getShapeSlotPairCount());
        assertEquals(1, newGame.cellGridPair().rightGrid().getShapeSlotPairCount());
    }

    @Test
    public void givenUserInputThatIsCorrectThenIsCorrectGuessReturnsTrue() {
        ShapeMatchGame gameLogic  = new ShapeMatchGame(new GameLevel(10), matchingDisplayShapesPair, 12, new Score(2222), false);

        assertTrue(gameLogic.isCorrectAnswer(UserInput.Match));
    }

    @Test
    public void givenUserInputThatIsNotCorrectThenIsCorrectGuessReturnsFalse() {
        ShapeMatchGame gameLogic  = new ShapeMatchGame(new GameLevel(10), matchingDisplayShapesPair, 12, new Score(2222), false);

        assertFalse(gameLogic.isCorrectAnswer(UserInput.Mismatch));
    }

    private ShapeMatchGame makeNConsecutiveCorrectGuess(Integer n, ShapeMatchGame gameLogic) {
        return recurse(n, gameLogic);
    }

    private ShapeMatchGame recurse(Integer num , ShapeMatchGame gameLogic) {

        if (num == 0) return gameLogic;
        else {
            if (gameLogic.isMatchingPairShapes())
                return recurse(num - 1, gameLogic.evaluateUserInput(UserInput.Match));
            else
                return recurse(num - 1, gameLogic.evaluateUserInput(UserInput.Mismatch));
        }
    }
}
