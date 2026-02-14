package com.sudoku.ui.mainscreen;

import com.sudoku.game.GameState;
import com.sudoku.game.SudokuBoard;
import com.sudoku.game.SudokuValidator;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;
import java.util.HashSet;
import java.util.Set;

public class SudokuPresenterTest {

    private SudokuMainViewContract view;
    private SudokuBoard board;
    private SudokuValidator validator;
    private GameState gameState;
    private SudokuPresenter presenter;

    @Before
    public void setUp() {
        view = mock( SudokuMainViewContract.class );
        board = mock( SudokuBoard.class );
        validator = mock( SudokuValidator.class );
        gameState = mock( GameState.class );
        presenter = new SudokuPresenter( view, board, validator, gameState );
    }

    @Test
    public void displayBoardUpdatesAllCells() {
        when( board.getValue( anyInt(), anyInt() ) ).thenReturn( SudokuBoard.EMPTY );
        when( board.isFixed( anyInt(), anyInt() ) ).thenReturn( false );

        presenter.displayBoard();

        // 9x9 = 81 cells
        verify( view, times( 81 ) ).updateCell( anyInt(), anyInt(), anyString(), anyBoolean() );
    }

    @Test
    public void displayBoardShowsFixedCellValues() {
        when( board.getValue( 0, 0 ) ).thenReturn( 5 );
        when( board.isFixed( 0, 0 ) ).thenReturn( true );
        when( board.getValue( anyInt(), anyInt() ) ).thenReturn( SudokuBoard.EMPTY );
        when( board.isFixed( anyInt(), anyInt() ) ).thenReturn( false );
        // Override specific cell
        when( board.getValue( 0, 0 ) ).thenReturn( 5 );
        when( board.isFixed( 0, 0 ) ).thenReturn( true );

        presenter.displayBoard();

        verify( view ).updateCell( 0, 0, "5", true );
    }

    @Test
    public void displayBoardShowsEmptyCellsAsBlank() {
        when( board.getValue( anyInt(), anyInt() ) ).thenReturn( SudokuBoard.EMPTY );
        when( board.isFixed( anyInt(), anyInt() ) ).thenReturn( false );

        presenter.displayBoard();

        verify( view ).updateCell( 0, 0, "", false );
    }

    @Test
    public void onCellSelectedDoesNothingForFixedCell() {
        when( board.isFixed( 0, 0 ) ).thenReturn( true );

        presenter.onCellSelected( 0, 0 );

        verify( view, never() ).highlightSelectedCell( anyInt(), anyInt() );
    }

    @Test
    public void onCellSelectedHighlightsNonFixedCell() {
        when( board.isFixed( 3, 4 ) ).thenReturn( false );

        presenter.onCellSelected( 3, 4 );

        verify( view ).highlightSelectedCell( 3, 4 );
    }

    @Test
    public void onNumberSelectedDoesNothingWithoutSelection() {
        presenter.onNumberSelected( 5 );

        verify( board, never() ).setValue( anyInt(), anyInt(), anyInt() );
        verify( view, never() ).updateCell( anyInt(), anyInt(), anyString(), anyBoolean() );
    }

    @Test
    public void onNumberSelectedSetsValueAndUpdatesView() {
        when( board.isFixed( 2, 3 ) ).thenReturn( false );
        when( validator.findConflicts( board ) ).thenReturn( Collections.emptySet() );
        when( validator.isSolved( board ) ).thenReturn( false );

        presenter.onCellSelected( 2, 3 );
        presenter.onNumberSelected( 7 );

        verify( board ).setValue( 2, 3, 7 );
        verify( view ).updateCell( 2, 3, "7", false );
        verify( gameState ).incrementMoveCount();
    }

    @Test
    public void onNumberSelectedUpdatesMovesDisplay() {
        when( board.isFixed( 0, 0 ) ).thenReturn( false );
        when( gameState.getMoveCount() ).thenReturn( 5 );
        when( validator.findConflicts( board ) ).thenReturn( Collections.emptySet() );
        when( validator.isSolved( board ) ).thenReturn( false );

        presenter.onCellSelected( 0, 0 );
        presenter.onNumberSelected( 1 );

        verify( view ).updateMoves( 5 );
    }

    @Test
    public void onNumberSelectedShowsConflicts() {
        when( board.isFixed( 0, 0 ) ).thenReturn( false );
        Set<String> conflicts = new HashSet<>();
        conflicts.add( "0,0" );
        conflicts.add( "0,5" );
        when( validator.findConflicts( board ) ).thenReturn( conflicts );
        when( validator.isSolved( board ) ).thenReturn( false );

        presenter.onCellSelected( 0, 0 );
        presenter.onNumberSelected( 3 );

        verify( view ).clearConflicts();
        verify( view ).showConflicts( conflicts );
    }

    @Test
    public void onNumberSelectedClearsConflictsWhenNone() {
        when( board.isFixed( 0, 0 ) ).thenReturn( false );
        when( validator.findConflicts( board ) ).thenReturn( Collections.emptySet() );
        when( validator.isSolved( board ) ).thenReturn( false );

        presenter.onCellSelected( 0, 0 );
        presenter.onNumberSelected( 1 );

        verify( view ).clearConflicts();
        verify( view, never() ).showConflicts( any() );
    }

    @Test
    public void onNumberSelectedTriggersSolvedWhenComplete() {
        when( board.isFixed( 0, 0 ) ).thenReturn( false );
        when( validator.findConflicts( board ) ).thenReturn( Collections.emptySet() );
        when( validator.isSolved( board ) ).thenReturn( true );
        when( gameState.getElapsedSeconds() ).thenReturn( 120L );
        when( gameState.getMoveCount() ).thenReturn( 42 );

        presenter.onCellSelected( 0, 0 );
        presenter.onNumberSelected( 9 );

        verify( gameState ).markCompleted();
        verify( view ).onPuzzleSolved( 120L, 42 );
    }

    @Test
    public void onClearSelectedSetsEmptyValue() {
        when( board.isFixed( 1, 1 ) ).thenReturn( false );
        when( validator.findConflicts( board ) ).thenReturn( Collections.emptySet() );
        when( validator.isSolved( board ) ).thenReturn( false );

        presenter.onCellSelected( 1, 1 );
        presenter.onClearSelected();

        verify( board ).setValue( 1, 1, SudokuBoard.EMPTY );
        verify( view ).updateCell( 1, 1, "", false );
    }

    @Test
    public void onNumberSelectedOnFixedCellDoesNothing() {
        when( board.isFixed( 0, 0 ) ).thenReturn( false );
        when( board.isFixed( 1, 1 ) ).thenReturn( true );
        when( validator.findConflicts( board ) ).thenReturn( Collections.emptySet() );
        when( validator.isSolved( board ) ).thenReturn( false );

        // Select a non-fixed cell first, then try to select a fixed one
        presenter.onCellSelected( 0, 0 );
        presenter.onCellSelected( 1, 1 );
        // The selection should still be on (0,0) since (1,1) is fixed
        presenter.onNumberSelected( 5 );

        verify( board ).setValue( 0, 0, 5 );
    }

    // --- Undo tests ---

    @Test
    public void onUndoDoesNothingWhenNoMoves() {
        presenter.onUndo();

        verify( board, never() ).setValue( anyInt(), anyInt(), anyInt() );
        verify( view, never() ).updateCell( anyInt(), anyInt(), anyString(), anyBoolean() );
    }

    @Test
    public void onUndoRestoresPreviousValue() {
        when( board.isFixed( 2, 3 ) ).thenReturn( false );
        when( board.getValue( 2, 3 ) ).thenReturn( SudokuBoard.EMPTY );
        when( validator.findConflicts( board ) ).thenReturn( Collections.emptySet() );
        when( validator.isSolved( board ) ).thenReturn( false );

        presenter.onCellSelected( 2, 3 );
        presenter.onNumberSelected( 7 );

        // Now undo
        presenter.onUndo();

        // Should restore previous value (EMPTY)
        verify( board ).setValue( 2, 3, SudokuBoard.EMPTY );
        verify( view ).updateCell( 2, 3, "", false );
    }

    @Test
    public void onUndoRestoresNonEmptyPreviousValue() {
        when( board.isFixed( 0, 0 ) ).thenReturn( false );
        when( board.getValue( 0, 0 ) ).thenReturn( 3 );
        when( validator.findConflicts( board ) ).thenReturn( Collections.emptySet() );
        when( validator.isSolved( board ) ).thenReturn( false );

        presenter.onCellSelected( 0, 0 );
        presenter.onNumberSelected( 5 );

        presenter.onUndo();

        verify( board ).setValue( 0, 0, 3 );
        verify( view ).updateCell( 0, 0, "3", false );
    }

    @Test
    public void multipleUndosReverseInOrder() {
        when( board.isFixed( 0, 0 ) ).thenReturn( false );
        when( board.isFixed( 1, 1 ) ).thenReturn( false );
        when( board.getValue( 0, 0 ) ).thenReturn( SudokuBoard.EMPTY );
        when( board.getValue( 1, 1 ) ).thenReturn( SudokuBoard.EMPTY );
        when( validator.findConflicts( board ) ).thenReturn( Collections.emptySet() );
        when( validator.isSolved( board ) ).thenReturn( false );

        presenter.onCellSelected( 0, 0 );
        presenter.onNumberSelected( 1 );
        presenter.onCellSelected( 1, 1 );
        presenter.onNumberSelected( 2 );

        // First undo reverses the second move
        presenter.onUndo();
        verify( board ).setValue( 1, 1, SudokuBoard.EMPTY );

        // Second undo reverses the first move
        presenter.onUndo();
        verify( board ).setValue( 0, 0, SudokuBoard.EMPTY );
    }

    @Test
    public void onUndoIncrementsMovesCount() {
        when( board.isFixed( 0, 0 ) ).thenReturn( false );
        when( board.getValue( 0, 0 ) ).thenReturn( SudokuBoard.EMPTY );
        when( validator.findConflicts( board ) ).thenReturn( Collections.emptySet() );
        when( validator.isSolved( board ) ).thenReturn( false );

        presenter.onCellSelected( 0, 0 );
        presenter.onNumberSelected( 5 );
        presenter.onUndo();

        // incrementMoveCount called twice: once for the move, once for undo
        verify( gameState, times( 2 ) ).incrementMoveCount();
    }

    @Test
    public void onUndoRefreshesConflicts() {
        when( board.isFixed( 0, 0 ) ).thenReturn( false );
        when( board.getValue( 0, 0 ) ).thenReturn( SudokuBoard.EMPTY );
        when( validator.findConflicts( board ) ).thenReturn( Collections.emptySet() );
        when( validator.isSolved( board ) ).thenReturn( false );

        presenter.onCellSelected( 0, 0 );
        presenter.onNumberSelected( 5 );
        presenter.onUndo();

        // clearConflicts called twice: once for the move, once for undo
        verify( view, times( 2 ) ).clearConflicts();
    }
}
