package com.example.tictactoe_xo.model;

/**
 * Drzi kumulativnu statistiku partija(pobjede X, pobjede O i nerjeseno)
 * Koristi se i za skor  uteucem mecu i za dugorocnu statistiku
 */
public class GameStats {

    private int winX;
    private int winO;
    private int draw;

    public GameStats() {
    }

    public GameStats(int winX, int winO, int draw) {
        this.winX = winX;
        this.winO = winO;
        this.draw = draw;
    }

    public int getWinX() {
        return winX;
    }

    public int getWinO() {
        return winO;
    }

    public int getDraw() {
        return draw;
    }

    public int getTotalGames(){
        return winX + winO + draw;
    }

    public void incrementForStatus(GameStatus status){
        switch (status){
            case WIN_X:
                winX++;
                break;
            case WIN_O:
                winO++;
                break;
            case DRAW:
                draw++;
                break;
            default:
                break;
        }
    }


    public float getWinRateX(){
        int total = getTotalGames();
        if (total == 0) return 0f;
        return (float) (winX * 100) / total;
    }

    public void reset(){
        winX = 0;
        winO = 0;
        draw = 0;
    }

}
