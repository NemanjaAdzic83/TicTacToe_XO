package com.example.tictactoe_xo.model;

/**
 * Cumulative statistics for SOLO games (human vs AI).
 * Unlike {@link GameStats}, which counts wins per symbol (X/O), this class counts wins
 * per side (human/AI), so the result is meaningful no matter which symbol the human played.
 */
public class SoloStats {

    private int humanWins;
    private int aiWins;
    private int draws;

    public SoloStats() {
    }

    public SoloStats(int humanWins, int aiWins, int draws) {
        this.humanWins = humanWins;
        this.aiWins = aiWins;
        this.draws = draws;
    }

    public int getHumanWins() {
        return humanWins;
    }

    public int getAiWins() {
        return aiWins;
    }

    public int getDraws() {
        return draws;
    }

    public int getTotalGames() {
        return humanWins + aiWins + draws;
    }

    /**
     * Translates a symbol-based result (WIN_X / WIN_O) into a human/AI result.
     * IN_PROGRESS is ignored because the game is not finished yet.
     */
    public void incrementForResult(GameStatus status, Player humanPlayer) {
        switch (status) {
            case WIN_X:
                incrementWinner(Player.X, humanPlayer);
                break;
            case WIN_O:
                incrementWinner(Player.O, humanPlayer);
                break;
            case DRAW:
                draws++;
                break;
            default:
                break;
        }
    }

    private void incrementWinner(Player winner, Player humanPlayer) {
        if (winner == humanPlayer) {
            humanWins++;
        } else {
            aiWins++;
        }
    }

    /** Human win rate in percent (0-100); returns 0 when no games were played. */
    public float getHumanWinRate() {
        int total = getTotalGames();
        if (total == 0) return 0f;
        return (float) (humanWins * 100) / total;
    }
}
