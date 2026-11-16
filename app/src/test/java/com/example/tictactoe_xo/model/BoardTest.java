package com.example.tictactoe_xo.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;

public class BoardTest {

    /**
     * Builds a board from a 9-char string, row by row: 'X', 'O' or '.' for empty.
     * Example: "XXX" + "OO." + "..." -> X wins the top row.
     */
    private static Board boardOf(String layout) {
        Board board = new Board();
        for (int i = 0; i < Board.SIZE; i++) {
            char c = layout.charAt(i);
            if (c == 'X') board.setCell(i, Player.X);
            else if (c == 'O') board.setCell(i, Player.O);
        }
        return board;
    }

    // ---------- basic cell handling ----------

    @Test
    public void newBoard_allCellsAreEmpty() {
        Board board = new Board();
        for (int i = 0; i < Board.SIZE; i++) {
            assertTrue("cell " + i, board.isCellEmpty(i));
        }
    }

    @Test
    public void newBoard_evaluateStatus_returnsInProgress() {
        assertEquals(GameStatus.IN_PROGRESS, new Board().evaluateStatus());
    }

    @Test
    public void setCell_storesPlayerAndCellIsNoLongerEmpty() {
        Board board = new Board();

        board.setCell(4, Player.O);

        assertEquals(Player.O, board.getCell(4));
        assertFalse(board.isCellEmpty(4));
    }

    @Test
    public void reset_clearsAllCells() {
        Board board = boardOf("XOX" + "OXO" + "OXO");

        board.reset();

        assertEquals(Board.SIZE, board.getEmptyCells().size());
    }

    @Test
    public void getEmptyCells_returnsOnlyEmptyIndexesInOrder() {
        Board board = boardOf("X.O" + ".X." + "O..");

        assertEquals(Arrays.asList(1, 3, 5, 7, 8), board.getEmptyCells());
    }

    @Test
    public void isFull_trueOnlyWhenNoEmptyCellsLeft() {
        Board board = boardOf("XOX" + "XOO" + "OX.");
        assertFalse(board.isFull());

        board.setCell(8, Player.X);
        assertTrue(board.isFull());
    }

    // ---------- win detection ----------

    @Test
    public void findWinLine_detectsAllEightLinesWithCorrectType() {
        int[][] lines = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                {0, 4, 8}, {2, 4, 6}
        };
        WinLine.Type[] expectedTypes = {
                WinLine.Type.ROW, WinLine.Type.ROW, WinLine.Type.ROW,
                WinLine.Type.COLUMN, WinLine.Type.COLUMN, WinLine.Type.COLUMN,
                WinLine.Type.DIAGONAL_MAIN, WinLine.Type.DIAGONAL_SEC
        };

        for (int i = 0; i < lines.length; i++) {
            Board board = new Board();
            for (int cell : lines[i]) board.setCell(cell, Player.X);

            WinLine winLine = board.findWinLine(Player.X);

            String msg = "line " + Arrays.toString(lines[i]);
            assertNotNull(msg, winLine);
            assertArrayEquals(msg, lines[i], winLine.getCellIndexes());
            assertEquals(msg, expectedTypes[i], winLine.getType());
        }
    }

    @Test
    public void findWinLine_noWinner_returnsNull() {
        Board board = boardOf("XX." + "OO." + "...");

        assertNull(board.findWinLine(Player.X));
        assertNull(board.findWinLine(Player.O));
    }

    @Test
    public void findWinLine_onlyForGivenPlayer() {
        Board board = boardOf("XXX" + "OO." + "...");

        assertNotNull(board.findWinLine(Player.X));
        assertNull(board.findWinLine(Player.O));
    }

    @Test
    public void findWinLine_changingReturnedIndexes_doesNotChangeWinRules() {
        WinLine winLine = boardOf("XXX" + "OO." + "...").findWinLine(Player.X);

        winLine.getCellIndexes()[0] = 8;   // a caller modifies the returned array

        // A fresh board must still recognise the top row as a win
        WinLine fresh = boardOf("XXX" + "OO." + "...").findWinLine(Player.X);
        assertNotNull(fresh);
        assertArrayEquals(new int[]{0, 1, 2}, fresh.getCellIndexes());
    }

    // ---------- game status ----------

    @Test
    public void evaluateStatus_rowForX_returnsWinX() {
        assertEquals(GameStatus.WIN_X, boardOf("OO." + "XXX" + "...").evaluateStatus());
    }

    @Test
    public void evaluateStatus_columnForO_returnsWinO() {
        assertEquals(GameStatus.WIN_O, boardOf("XOX" + ".O." + "XO.").evaluateStatus());
    }

    @Test
    public void evaluateStatus_fullBoardWithoutWinner_returnsDraw() {
        assertEquals(GameStatus.DRAW, boardOf("XOX" + "XOO" + "OXX").evaluateStatus());
    }

    @Test
    public void evaluateStatus_winningLastMoveOnFullBoard_returnsWinNotDraw() {
        // X fills the last cell (8) and completes the main diagonal 0-4-8
        Board board = boardOf("XOX" + "OXO" + "OX.");

        board.setCell(8, Player.X);

        assertTrue(board.isFull());
        assertEquals(GameStatus.WIN_X, board.evaluateStatus());
    }

    // ---------- copy constructor ----------

    @Test
    public void copyConstructor_copyIsIndependentFromOriginal() {
        Board original = boardOf("X........");

        Board copy = new Board(original);
        copy.setCell(4, Player.O);

        assertEquals(Player.X, copy.getCell(0));       // copy starts with the same cells
        assertTrue(original.isCellEmpty(4));           // change on copy does not leak back
    }
}
