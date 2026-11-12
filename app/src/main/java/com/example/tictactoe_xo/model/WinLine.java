package com.example.tictactoe_xo.model;

/**
 * Predstavlja pobjednicku liniju na tabli - tro polja u nizu (red,kolona, dijagonala)
 * Koristi se za crtanje animacije ,linije preko polja koja su odnijela pobjedu
 */
public class WinLine {

    public enum Type{
        ROW,
        COLUMN,
        DIAGONAL_MAIN,
        DIAGONAL_SEC
    }

    private final int[] cellIndexes;
    private final Type type;

    public WinLine(int[] cellIndexes, Type type){
        this.cellIndexes = cellIndexes;
        this.type = type;
    }

    public int[] getCellIndexes(){
        return cellIndexes;
    }

    public Type getType() {
        return type;
    }
}


