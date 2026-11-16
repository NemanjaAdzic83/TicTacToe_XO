package com.example.tictactoe_xo.ai;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.example.tictactoe_xo.model.Board;
import com.example.tictactoe_xo.model.Difficulty;
import com.example.tictactoe_xo.model.GameStatus;
import com.example.tictactoe_xo.model.Player;

import org.junit.Test;

public class TicTacToeAITest {

    private final TicTacToeAI ai = new TicTacToeAI();

    /** Same helper as in BoardTest: 9 chars row by row, 'X', 'O' or '.' for empty. */
    private static Board boardOf(String layout) {
        Board board = new Board();
        for (int i = 0; i < Board.SIZE; i++) {
            char c = layout.charAt(i);
            if (c == 'X') board.setCell(i, Player.X);
            else if (c == 'O') board.setCell(i, Player.O);
        }
        return board;
    }

    /** Board as a 9-char string, used in failure messages. */
    private static String describe(Board board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Board.SIZE; i++) {
            Player p = board.getCell(i);
            sb.append(p == Player.X ? 'X' : p == Player.O ? 'O' : '.');
        }
        return sb.toString();
    }

    // ---------- rules that hold for every difficulty ----------

    @Test(expected = IllegalStateException.class)
    public void computeMove_fullBoard_throwsIllegalStateException() {
        ai.computeMove(boardOf("XOX" + "XOO" + "OXX"), Player.O, Difficulty.HARD);
    }

    @Test
    public void computeMove_emptyBoard_returnsValidIndex() {
        for (Difficulty difficulty : Difficulty.values()) {
            int move = ai.computeMove(new Board(), Player.X, difficulty);
            assertTrue(difficulty + " returned " + move, move >= 0 && move < Board.SIZE);
        }
    }

    @Test
    public void computeMove_anyDifficulty_alwaysReturnsEmptyCell() {
        Board board = boardOf("X.O" + ".X." + "O..");
        for (Difficulty difficulty : Difficulty.values()) {
            for (int i = 0; i < 200; i++) {
                int move = ai.computeMove(board, Player.X, difficulty);
                assertTrue(difficulty + " played on taken cell " + move, board.isCellEmpty(move));
            }
        }
    }

    @Test
    public void computeMove_doesNotModifyBoard() {
        Board board = boardOf("X.O" + ".X." + "O..");
        String before = describe(board);

        ai.computeMove(board, Player.X, Difficulty.HARD);

        assertEquals(before, describe(board));
    }

    // ---------- HARD: specific situations ----------

    @Test
    public void hard_asX_takesImmediateWinInRow() {
        Board board = boardOf("XX." + "O.." + "..O");

        assertEquals(2, ai.computeMove(board, Player.X, Difficulty.HARD));
    }

    @Test
    public void hard_asO_takesImmediateWinOnDiagonal() {
        Board board = boardOf("OXX" + ".O." + "X..");

        assertEquals(8, ai.computeMove(board, Player.O, Difficulty.HARD));
    }

    @Test
    public void hard_blocksOpponentsImmediateWin() {
        // X threatens the top row, O has no win of its own
        Board board = boardOf("XX." + ".O." + "...");

        assertEquals(2, ai.computeMove(board, Player.O, Difficulty.HARD));
    }

    @Test
    public void hard_prefersOwnWinOverBlocking() {
        // X threatens cell 2, but O can win right now on cell 5
        Board board = boardOf("XX." + "OO." + "X..");

        assertEquals(5, ai.computeMove(board, Player.O, Difficulty.HARD));
    }

    // ---------- HARD: exhaustive "never loses" check ----------

    @Test
    public void hard_asSecondPlayer_neverLoses() {
        assertAiNeverLoses(new Board(), Player.X, Player.O);
    }

    @Test
    public void hard_asFirstPlayer_neverLoses() {
        // The first move on an empty board is random, so try all nine openings
        for (int first = 0; first < Board.SIZE; first++) {
            Board board = new Board();
            board.setCell(first, Player.X);
            assertAiNeverLoses(board, Player.O, Player.X);
        }
    }

    /**
     * Plays every possible game from this position: on the opponent's turn every empty cell
     * is tried, on the AI's turn the AI picks. Fails if any game ends with an opponent win.
     */
    private void assertAiNeverLoses(Board board, Player toMove, Player aiPlayer) {
        GameStatus status = board.evaluateStatus();
        if (status != GameStatus.IN_PROGRESS) {
            GameStatus loss = (aiPlayer == Player.X) ? GameStatus.WIN_O : GameStatus.WIN_X;
            assertNotEquals("AI lost: " + describe(board), loss, status);
            return;
        }

        if (toMove == aiPlayer) {
            Board next = new Board(board);
            next.setCell(ai.computeMove(board, aiPlayer, Difficulty.HARD), aiPlayer);
            assertAiNeverLoses(next, toMove.opposite(), aiPlayer);
        } else {
            for (int cell : board.getEmptyCells()) {
                Board next = new Board(board);
                next.setCell(cell, toMove);
                assertAiNeverLoses(next, toMove.opposite(), aiPlayer);
            }
        }
    }
}
