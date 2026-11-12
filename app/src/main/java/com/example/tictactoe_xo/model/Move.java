package com.example.tictactoe_xo.model;

/**
 * Predstavlja jedan odigrani potez - koristi se za cuvanje istorije poteza i
 * implementaciju funnkcije "vrati potez (UNDO)"
 */
public class Move {
    private final int cellIndex;
    private final Player player;

    public Move(int cellIndex, Player player) {
        this.cellIndex = cellIndex;
        this.player = player;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public Player getPlayer() {
        return player;
    }
}
