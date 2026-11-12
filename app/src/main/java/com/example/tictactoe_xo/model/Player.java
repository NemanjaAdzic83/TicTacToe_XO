package com.example.tictactoe_xo.model;

/**
 * Predstavlja igracac na  tabli. EMPTY se koristi za prazno polje
 */
public enum Player {

    EMPTY,
    X,
    O;


    /**
     * Vraca suprotnog igraca, koristi se za smjenu poteza
     * Koristi se umjesto if/else blokova
     */
    public Player oposite(){
        if (this == X) return O;
        if (this == O) return X;
        return EMPTY;
    }

    public boolean isEmpty(){
        return this == EMPTY;
    }

}
