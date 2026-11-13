package com.example.tictactoe_xo.viewmodel;

import android.app.Application;
import android.os.Looper;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tictactoe_xo.ai.TicTacToeAI;
import com.example.tictactoe_xo.model.Board;
import com.example.tictactoe_xo.model.Difficulty;
import com.example.tictactoe_xo.model.GameMode;
import com.example.tictactoe_xo.model.GameStats;
import com.example.tictactoe_xo.model.GameStatus;
import com.example.tictactoe_xo.model.Move;
import com.example.tictactoe_xo.model.Player;
import com.example.tictactoe_xo.model.WinLine;
import com.example.tictactoe_xo.repository.PreferencesRepository;

import java.util.ArrayDeque;
import java.util.Deque;


/**
 * Centralni ViewModel koji vodi cjelokupnu logiku jedne partije, upravlj tablom,
 * redoljedom poteza, AI protivnikom u solo modu, skorom tekuceg meca i obavjestava UI
 * putem LiveData o promjeni stanja
 *
 * Fragment/Activity sloj se iskljcivo pretplacuje na LiveData i salje korisnicke akcije
 * (onCellclicked, undo, reset) - sva logika je ovde. sime se postuje MVVM odvajanje odgovornosti
 */
public class GameViewModel extends AndroidViewModel {

    private final Board board = new Board();
    private final TicTacToeAI ai = new TicTacToeAI();
    private final PreferencesRepository prefsRepo;
    private final Deque<Move> moveHistory = new ArrayDeque<>();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private GameMode gameMode = GameMode.DUO;
    private Difficulty difficulty = Difficulty.MEDIUM;
    private Player humanPlayer = Player.X;
    private Player currentTurn = Player.X;
    private boolean aiThinking = false;
    // True once configure() has run; a fresh ViewModel (e.g. after process death) starts as false
    private boolean configured = false;

    private final GameStats matchScore = new GameStats();


    private final MutableLiveData<Player> currentTurnLive = new MutableLiveData<>(Player.X);
    private final MutableLiveData<GameStatus> gameStatusLive = new MutableLiveData<>(GameStatus.IN_PROGRESS);
    private final MutableLiveData<WinLine> winLineLive = new MutableLiveData<>();
    private final MutableLiveData<GameStats> matchScoreLive = new MutableLiveData<>(matchScore);
    private final MutableLiveData<Boolean> aiThinkingLive = new MutableLiveData<>(false);
    private final MutableLiveData<Move> lastMoveLive = new MutableLiveData<>();
    private final MutableLiveData<Boolean> boardResetLive = new MutableLiveData<>();
    private final MutableLiveData<Boolean> undoAvailableLive = new MutableLiveData<>(false);

    public GameViewModel(@NonNull Application application) {
        super(application);
        prefsRepo = new PreferencesRepository(application);
    }


    // --- KONFIGURACIJA PARTIJE (POZVATI PRIJE PRVE PARTIJE) ---------
    public void configure(GameMode mode, Difficulty difficulty, Player humanPlayer){
        this.gameMode = mode;
        this.difficulty = difficulty;
        this.humanPlayer = humanPlayer;
        configured = true;
        startNewMatch();
    }

    public boolean isConfigured(){
        return configured;
    }

    public GameMode getGameMode(){
        return gameMode;
    }

    public Difficulty getDifficulty(){
        return difficulty;
    }

    //--- LIVEDATA GETTERI ---
    public LiveData<Player> getCurrentTurn(){
        return currentTurnLive;
    }

    public LiveData<GameStatus> getGameStatus(){
        return gameStatusLive;
    }

    public LiveData<WinLine> getWinLine(){
        return winLineLive;
    }

    public LiveData<GameStats> getMatchScore(){
        return matchScoreLive;
    }

    public LiveData<Boolean> getAIThinking(){
        return aiThinkingLive;
    }

    public LiveData<Move> getLastMove(){
        return lastMoveLive;
    }

    public LiveData<Boolean> getBoardReset(){
        return boardResetLive;
    }

    public LiveData<Boolean> getUndoAvailable(){
        return undoAvailableLive;
    }

    public Board getBoard(){
        return board;
    }

    public boolean isHumanTurnBlocked(){
        // U solo modu se blokira klik dok AI ne napravi svoj potez
        return gameMode == GameMode.SOLO && (aiThinking || currentTurn != humanPlayer);
    }

    /**
     * Pokrece novu partiju , cisti tablu , ali ne resetuje skor meca
     */
    public void startNewMatch() {
        // Cancel a pending AI move so it can't land on the new board
        mainHandler.removeCallbacksAndMessages(null);
        board.reset();
        moveHistory.clear();
        currentTurn = Player.X;
        aiThinking = false;

        currentTurnLive.setValue(currentTurn);
        gameStatusLive.setValue(GameStatus.IN_PROGRESS);
        winLineLive.setValue(null);
        aiThinkingLive.setValue(false);
        boardResetLive.setValue(true);
        undoAvailableLive.setValue(false);

        // Ako je solo mod i AI pocinje prvi
        if (gameMode ==  GameMode.SOLO && humanPlayer != Player.X){
            scheduleAiMove();
        }

    }

    /**
     * Pozvati kad korisnik tapne na polje
     */
    public void onCellClicked(int index){
        if (gameStatusLive.getValue() != GameStatus.IN_PROGRESS) return;
        if (!board.isCellEmpty(index)) return;
        if (isHumanTurnBlocked()) return;

        applyMove(index, currentTurn);

        if (checkAndHandleGameEnd()) return;

        switchTurn();

        if (gameMode == GameMode.SOLO && currentTurn != humanPlayer){
            scheduleAiMove();
        }
    }

    /**
     * Vraca poslednji potez (UNDO) Dostupno samo dok partija traje
     */
    public void undoLastMove(){
        if (!canUndo()) return;
        if (gameStatusLive.getValue() != GameStatus.IN_PROGRESS) return;
        if (aiThinking) return;

        if (gameMode == GameMode.SOLO) {
            // Pop moves until the human's last move has been removed,
            // so the human is always on turn after undo (works whether AI played first or not)
            Move last;
            do {
                last = moveHistory.pop();
                board.setCell(last.getCellIndex(), Player.EMPTY);
            } while (last.getPlayer() != humanPlayer);
            currentTurn = humanPlayer;
        } else {
            // DUO: undo exactly one move
            Move last = moveHistory.pop();
            board.setCell(last.getCellIndex(), Player.EMPTY);
            currentTurn = last.getPlayer();
        }

        currentTurnLive.setValue(currentTurn);
        gameStatusLive.setValue(GameStatus.IN_PROGRESS);
        winLineLive.setValue(null);
        undoAvailableLive.setValue(canUndo());

        // ponovo se iscrtava cijela tabla iz modela
        boardResetLive.setValue(true);

    }


    private void switchTurn() {
        currentTurn = currentTurn.oposite();
        currentTurnLive.setValue(currentTurn);
    }

    // Provjerva da li je petlja zavrsena ,ako jeste azurira skor i status. Vrca true ako je kraj
    private boolean checkAndHandleGameEnd() {
        GameStatus status = board.evaluateStatus();
        if (status == GameStatus.IN_PROGRESS) return false;

        gameStatusLive.setValue(status);
        matchScore.incrementForStatus(status);
        matchScoreLive.setValue(matchScore);
        prefsRepo.saveStatsForResult(status);

        if (status == GameStatus.WIN_X){
            winLineLive.setValue(board.findWinnLine(Player.X));
        } else if (status == GameStatus.WIN_O) {
            winLineLive.setValue(board.findWinnLine(Player.O));
        }
        return true;

    }

    private void applyMove(int index, Player currentTurn) {
        board.setCell(index,currentTurn);
        moveHistory.push(new Move(index,currentTurn));
        lastMoveLive.setValue(new Move(index,currentTurn));
        undoAvailableLive.setValue(canUndo());
    }

    // In SOLO mode undo only makes sense if the human has made at least one move
    private boolean canUndo() {
        if (gameMode != GameMode.SOLO) return !moveHistory.isEmpty();
        for (Move m : moveHistory) {
            if (m.getPlayer() == humanPlayer) return true;
        }
        return false;
    }

    private void scheduleAiMove() {
        aiThinking = true;
        aiThinkingLive.setValue(true);

        // Mali "delay" kako bi Ai potez izgledao kao razmisljanje
        long delay = 450 + (long) (Math.random() * 350);
        Player aiPlayer = humanPlayer.oposite();

        mainHandler.postDelayed(() -> {
            if (gameStatusLive.getValue() != GameStatus.IN_PROGRESS){
                aiThinking = false;
                aiThinkingLive.setValue(false);
                return;
            }
            int move = ai.computeMove(board,aiPlayer,difficulty);
            applyMove(move,aiPlayer);

            aiThinking = false;
            aiThinkingLive.setValue(false);

            if (!checkAndHandleGameEnd()){
                switchTurn();
            }

        },delay);
    }

    public void resetMatchScore(){
        matchScore.reset();
        matchScoreLive.setValue(matchScore);
    }

    /** Clears the board and resets the current match score (long-term stats are kept). */
    public void restartMatch() {
        resetMatchScore();
        startNewMatch();
    }

    @Override
    protected void onCleared(){
        super.onCleared();
        mainHandler.removeCallbacksAndMessages(null);
    }
}
