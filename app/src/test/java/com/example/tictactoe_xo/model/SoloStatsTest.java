package com.example.tictactoe_xo.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SoloStatsTest {

    private static final float DELTA = 0.001f;

    @Test
    public void winX_whenHumanIsX_countsAsHumanWin() {
        SoloStats stats = new SoloStats();
        stats.incrementForResult(GameStatus.WIN_X, Player.X);
        assertEquals(1, stats.getHumanWins());
        assertEquals(0, stats.getAiWins());
    }

    @Test
    public void winX_whenHumanIsO_countsAsAiWin() {
        SoloStats stats = new SoloStats();
        stats.incrementForResult(GameStatus.WIN_X, Player.O);
        assertEquals(0, stats.getHumanWins());
        assertEquals(1, stats.getAiWins());
    }

    @Test
    public void winO_whenHumanIsO_countsAsHumanWin() {
        SoloStats stats = new SoloStats();
        stats.incrementForResult(GameStatus.WIN_O, Player.O);
        assertEquals(1, stats.getHumanWins());
        assertEquals(0, stats.getAiWins());
    }

    @Test
    public void winO_whenHumanIsX_countsAsAiWin() {
        SoloStats stats = new SoloStats();
        stats.incrementForResult(GameStatus.WIN_O, Player.X);
        assertEquals(0, stats.getHumanWins());
        assertEquals(1, stats.getAiWins());
    }

    @Test
    public void draw_countsAsDraw() {
        SoloStats stats = new SoloStats();
        stats.incrementForResult(GameStatus.DRAW, Player.X);
        assertEquals(1, stats.getDraws());
        assertEquals(1, stats.getTotalGames());
    }

    @Test
    public void inProgress_isIgnored() {
        SoloStats stats = new SoloStats();
        stats.incrementForResult(GameStatus.IN_PROGRESS, Player.X);
        assertEquals(0, stats.getTotalGames());
    }

    @Test
    public void humanWinRate_withNoGames_isZero() {
        assertEquals(0f, new SoloStats().getHumanWinRate(), DELTA);
    }

    @Test
    public void humanWinRate_isPercentOfTotalGames() {
        SoloStats stats = new SoloStats(1, 2, 1);
        assertEquals(25f, stats.getHumanWinRate(), DELTA);
    }
}
