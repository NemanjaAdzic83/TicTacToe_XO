package com.example.tictactoe_xo.model;

import android.net.ipsec.ike.exceptions.IkeNetworkLostException;

import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;

/**
 *  Predstavlja tablu 3x3  i sadrzi svu logiku oko provjere pobjede, nerjesenog rezultata
 *   i dostupnih poteza
 *   Polja su indeksiranaa 0-8 ,rasporedjena po redovimna
 *   0 | 1 | 2
 *   3 | 4 | 5
 *   6 | 7 | 8
 *
 */
public class Board {

    public static final int SIZE = 9;

    // sve moguce pobjednicke kombincije sa odgovarajucim tipom linije
    private static final int[][] WIN_COMBINATIONS = {
            {0,1,2},{3,4,5},{6,7,8},  // redovi
            {0,3,6},{1,4,7},{2,5,8},  // kolone
            {0,4,8},                  // glavna dijagonala
            {2,4,6}                  // sporedna dijagonala

    };

    private final Player[] cells;

    public Board() {
       cells = new Player[SIZE];
       reset();
    }


    // Pravi kopiju druge table (koristi se u AI simulaciji)
    public Board(Board other){
        this.cells = other.cells.clone();
    }


    /*
    Prazni sva polja i poziva se na pocetku svake partije
     */
    public void reset() {
        for (int i = 0; i < SIZE; i++){
            cells[i] = Player.EMPTY;
        }
    }

    public Player getCell(int index){
        return cells[index];
    }

    public void setCell(int index, Player player){
        cells[index] = player;
    }

    /*
    Provjera prije poteza , ne smije se igrati na popunjeno polje
     */
    public boolean isCellEmpty(int index){
        return cells[index] == Player.EMPTY;
    }


    public boolean isFull(){
        for (Player p : cells){
            if ( p == Player.EMPTY)
                return false;
        }
        return true;
    }

    /**
     * Vraca listu indeksa svih praznih polja - korisno za AI (da  zna  koje poteze moze igrati)
     * i nasumicne poteze
     */
    public List<Integer> getEmptyCells(){
        List<Integer> empty = new ArrayList<>();
        for (int i = 0; i < SIZE; i++){
            if (cells[i] == Player.EMPTY) empty.add(i);
        }
        return empty;
    }


    /**
     * Provjerava da li je dati igrac pobjedio i ako jeste vraca odgovarajucu
     * pobjednicku liniju. Ako nema pobjede vraca null
     */
    public WinLine findWinnLine(Player player){
        for (int[] combo : WIN_COMBINATIONS){
            if (cells[combo[0]] == player && cells[combo[1]] == player
            && cells[combo[2]] == player){
                return new WinLine(combo,resolveLineType(combo));
            }
        }
        return null;
    }

    public boolean hasWinner(Player player){
        return findWinnLine(player) != null;
    }


    /**
     * Vraca trenutni status partije
     *  WIN_X -> WIN_O -> DRAW -> IN_PROGRESS , ovim redosljedom
     */
    public GameStatus evaluateStatus(){
        if (hasWinner(Player.X)) return GameStatus.WIN_X;
        if (hasWinner(Player.O)) return GameStatus.WIN_O;
        if (isFull()) return GameStatus.DRAW;
        return GameStatus.IN_PROGRESS;
    }



    private WinLine.Type resolveLineType(int[] combo){
        if (combo[0] == 0 && combo[2] == 8) return WinLine.Type.DIAGONAL_MAIN;
        if (combo[0] == 2 && combo[2] == 6) return WinLine.Type.DIAGONAL_SEC;
        if (combo[1] - combo[0] == 1) return WinLine.Type.ROW;
        return WinLine.Type.COLUMN;
    }

}
