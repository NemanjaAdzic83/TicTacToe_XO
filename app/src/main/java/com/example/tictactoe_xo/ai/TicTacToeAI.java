package com.example.tictactoe_xo.ai;


import androidx.appcompat.app.WindowDecorActionBar;

import com.example.tictactoe_xo.model.Board;
import com.example.tictactoe_xo.model.Difficulty;
import com.example.tictactoe_xo.model.GameStatus;
import com.example.tictactoe_xo.model.Player;

import java.nio.charset.MalformedInputException;
import java.util.List;
import java.util.Random;

/**
 * Ai protivnik za solo mod. Implementira tri nivo tezine
 *
 * EASY
 * MEDIUM
 * HARD
 *
 */

public class TicTacToeAI {
    private final Random random = new Random();


    /**
     * Racuna sledeci potez AI igraca
     * @param board trenutni stanje table
     * @param aiPlayer simbol koji igra Ai protivnik ( X ili O)
     * @param difficulty izabrani nivo tezine
     * @return indeks polja na koje AI igra potez
     */
    public int computeMove(Board board, Player aiPlayer, Difficulty difficulty){
        List<Integer> emptyCells = board.getEmptyCells();
        if (emptyCells.isEmpty()){
            throw new IllegalStateException("Nema dostupnih poteza na tabli");
        }
        if (emptyCells.size() == Board.SIZE){
            //prvi potez na praznoj tabli ide nasumicno tj. ne mora biti fiksan
            return emptyCells.get(random.nextInt(emptyCells.size()));
        }

        switch (difficulty){
            case EASY:
                return randomMove(emptyCells);
            case MEDIUM:
                return random.nextBoolean() ? bestMove(board, aiPlayer) : randomMove(emptyCells);
            default:
                return bestMove(board, aiPlayer);
        }

    }



    private int randomMove(List<Integer> emptyCells) {
        return emptyCells.get(random.nextInt(emptyCells.size()));
    }


/**
 * Pronalazi optimalan potez koristeci minimax sa minValue-maxValue secenjem
 */
    private int bestMove(Board board, Player aiPlayer) {
        Player opponent = aiPlayer.oposite();
        int bestScore = Integer.MIN_VALUE;
        int move = board.getEmptyCells().get(0);

        for (int index : board.getEmptyCells()){
            Board copy = new Board(board);
            copy.setCell(index,aiPlayer);
            int score = minimax(copy,0,false, aiPlayer, opponent, Integer.MIN_VALUE,Integer.MAX_VALUE);
            if (score > bestScore){
                bestScore = score;
                move = index;
            }
        }
        return move;
    }

    private int minimax(Board board, int depth, boolean isMaximizig, Player aiPlayer,
                        Player opponent, int minValue, int maxValue) {
        GameStatus status = board.evaluateStatus();

        if (status != GameStatus.IN_PROGRESS){
            return scoreForStatus(status,aiPlayer,depth);
        }

        if (isMaximizig){
            int best = Integer.MIN_VALUE;
            for (int index : board.getEmptyCells()){
                Board copy = new Board(board);
                copy.setCell(index, aiPlayer);
                int score = minimax(copy, depth + 1,false, aiPlayer, opponent, minValue, maxValue);
                best = Math.max(best,score);
                minValue = Math.max(minValue,best);
                if (minValue >= maxValue) break;
            }
            return best;
        }else {
            int best = Integer.MAX_VALUE;
            for (int index : board.getEmptyCells()){
                Board copy = new Board(board);
                copy.setCell(index,opponent);
                int score = minimax(copy, depth + 1, true,aiPlayer,opponent,minValue,maxValue);
                best = Math.min(best,score);
                maxValue = Math.min(maxValue,best);
                if (minValue >= maxValue) break;
            }
            return best;
        }

    }


    private int scoreForStatus(GameStatus status, Player aiPlayer, int depth) {
        boolean aiIsX = aiPlayer == Player.X;
        if (status == GameStatus.WIN_X){
            return aiIsX ? (10 - depth) : (depth - 10);
        }
        if (status == GameStatus.WIN_O){
            return !aiIsX ? (10 - depth) : (depth - 10);
        }
        return 0;
    }

}
