package com.sudoku.ui.mainscreen;

import com.sudoku.game.GameState;
import com.sudoku.game.Move;
import com.sudoku.game.SudokuBoard;
import com.sudoku.game.SudokuSolution;
import com.sudoku.game.SudokuValidator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class SudokuPresenter {

    private final SudokuMainViewContract view;
    private final SudokuBoard board;
    private final SudokuSolution solution;
    private final SudokuValidator validator;
    private final GameState gameState;
    private final Deque<Move> undoStack;

    private int selectedRow = -1;
    private int selectedCol = -1;
    private Timer timer;
    private boolean paused;

    public SudokuPresenter(SudokuMainViewContract view, SudokuBoard board,
                           SudokuSolution solution, SudokuValidator validator,
                           GameState gameState) {
        this.view = view;
        this.board = board;
        this.solution = solution;
        this.validator = validator;
        this.gameState = gameState;
        this.undoStack = new ArrayDeque<>();
    }

    public void displayBoard() {
        for (int r = 0; r < SudokuBoard.SIZE; r++) {
            for (int c = 0; c < SudokuBoard.SIZE; c++) {
                int val = board.getValue(r, c);
                String text = val == SudokuBoard.EMPTY ? "" : String.valueOf(val);
                view.updateCell(r, c, text, board.isFixed(r, c));
            }
        }
    }

    public void onCellSelected(int row, int col) {
        if (board.isFixed(row, col)) return;

        selectedRow = row;
        selectedCol = col;
        view.highlightSelectedCell(row, col);
    }

    public void onNumberSelected(int number) {
        if (selectedRow < 0 || selectedCol < 0) return;
        if (board.isFixed(selectedRow, selectedCol)) return;

        int previousValue = board.getValue(selectedRow, selectedCol);
        undoStack.push(new Move(selectedRow, selectedCol, previousValue));

        board.setValue(selectedRow, selectedCol, number);
        gameState.incrementMoveCount();

        String text = number == SudokuBoard.EMPTY ? "" : String.valueOf(number);
        view.updateCell(selectedRow, selectedCol, text, false);
        view.updateMoves(gameState.getMoveCount());

        refreshConflicts();

        if (validator.isSolved(board)) {
            gameState.markCompleted();
            stopTimer();
            view.onPuzzleSolved(gameState.getElapsedSeconds(), gameState.getMoveCount());
        }
    }

    public void onClearSelected() {
        onNumberSelected(SudokuBoard.EMPTY);
    }

    public void onUndo() {
        if (undoStack.isEmpty()) return;

        Move move = undoStack.pop();
        board.setValue(move.getRow(), move.getCol(), move.getPreviousValue());
        gameState.incrementMoveCount();

        String text = move.getPreviousValue() == SudokuBoard.EMPTY
            ? "" : String.valueOf(move.getPreviousValue());
        view.updateCell(move.getRow(), move.getCol(), text, false);
        view.updateMoves(gameState.getMoveCount());

        refreshConflicts();
    }

    public void onCheckProgress() {
        boolean correct = solution.isPartialSolutionCorrect( board );
        view.flashGridBorder( correct );
    }

    public void startTimer() {
        if (gameState.isCompleted()) return;
        paused = false;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!paused && !gameState.isCompleted()) {
                    gameState.setElapsedSeconds(gameState.getElapsedSeconds() + 1);
                    view.updateTimer(gameState.formatElapsedTime());
                }
            }
        }, 1000, 1000);
    }

    public void pauseTimer() {
        paused = true;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void stopTimer() {
        paused = true;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void refreshConflicts() {
        Set<String> conflicts = validator.findConflicts(board);
        view.clearConflicts();
        if (!conflicts.isEmpty()) {
            view.showConflicts(conflicts);
        }
    }
}
