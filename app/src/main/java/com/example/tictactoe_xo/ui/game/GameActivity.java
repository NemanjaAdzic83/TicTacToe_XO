package com.example.tictactoe_xo.ui.game;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tictactoe_xo.R;
import com.example.tictactoe_xo.databinding.ActivityGameBinding;
import com.example.tictactoe_xo.model.Difficulty;
import com.example.tictactoe_xo.model.GameMode;
import com.example.tictactoe_xo.model.GameStats;
import com.example.tictactoe_xo.model.GameStatus;
import com.example.tictactoe_xo.model.Move;
import com.example.tictactoe_xo.model.Player;
import com.example.tictactoe_xo.model.WinLine;
import com.example.tictactoe_xo.repository.PreferencesRepository;
import com.example.tictactoe_xo.ui.widgets.SymbolView;
import com.example.tictactoe_xo.util.SoundManager;
import com.example.tictactoe_xo.util.VibrationManager;
import com.example.tictactoe_xo.viewmodel.GameViewModel;

/**
 * Glavni ekran partije. Prikazuje tablu 3x3 sastavljenu od devet SymbolViewelemenata unutar Constraintlayouta-a, prati stanje partije
 * preko GAmViewModela , reprodukuje zvuk, vibraciju na poteze i pobjedu a nimira pobjednicku linju preko  WinLineView overlay-a
 *
 * prikazuje korisnicki interfejs,
 * reaguje na klikove korisnika,
 * posmatra (observe) podatke iz GameViewModel-a,
 * azurira ekran kada se podaci promijene.
 */

public class GameActivity extends AppCompatActivity {

    // KONSTANTE - Koriste se za prenos podataka između Activity-ja.
    public static final String EXTRA_GAME_MODE = "extra_game_mod";
    public static final String EXTRA_DIFFICULTY = "extra_difficulty";
    public static final String EXTRA_HUMAN_SYMBOL = "extra_human_symbol";

    private ActivityGameBinding binding; // Omogucava pristup svim View elementima
    private GameViewModel viewModel; // Veza sa ViewModel-om. Sav model igre nalazi se u njemu.
    private PreferencesRepository prefsRepo; //Cita podesavanja iz SharedPreferences
    private SoundManager soundManager; //Pusta zvukove
    private VibrationManager vibrationManager; //Pokreće vibraciju

    private SymbolView[] cellViews; // niz od 9 polja
    private int playerXColor; // boja igraca X
    private int playerOColor; // boja igraca O
    private GameMode gameMode; // Nacin igre, SOLO ili DUO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefsRepo = new PreferencesRepository(this); //Otvara SharedPreferences
        soundManager = new SoundManager(this, prefsRepo.isSoundEnabled()); //Pravi objekat za zvuk
        vibrationManager = new VibrationManager(this,prefsRepo.isVibrationEnabled()); // Pravi objekat za vibraciju

        loadPlayerColors();  // Usitava boje
        readIntentExtras();  // Cita podatke koje je prethodni ekran poslao
        setupBoardViews();   // Povezuje svih 9 polja
        setupButtons();      // Povezuje dugmad

        viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        configureViewModel(savedInstanceState == null); // Ako je prvi put otvorena igra, konfigurise ViewModel.Ako je rotacija ne radi nista
        observeViewModel();  // Pocinje posmatrati LiveData objekte
        

    }



    private void loadPlayerColors() {
        playerXColor = prefsRepo.getPlayerXColor(getColor(R.color.player_color_cyan));
        playerOColor = prefsRepo.getPlayerOColor(getColor(R.color.player_color_magenta));
        binding.winLineView.setLineColor(getColor(R.color.neon_yellow));
    }
    
    private void readIntentExtras(){
        String modeName = getIntent().getStringExtra(EXTRA_GAME_MODE);
        gameMode = modeName != null ? GameMode.valueOf(modeName) : GameMode.DUO;
    }
    
    private void setupBoardViews(){
        cellViews = new SymbolView[]{
                binding.cell0, binding.cell1,binding.cell2,
                binding.cell3,binding.cell4,binding.cell5,
                binding.cell6,binding.cell7,binding.cell8
        };
        
        for (int i = 0; i < cellViews.length; i++){
            final int index = i;
            cellViews[i].setSymbolColor(playerXColor);
            cellViews[i].setOnClickListener(v -> onCellTaped(index));
        }
    }
    

    private void onCellTaped(int index) {
        if (viewModel.isHumanTurnBlocked()) return;
        if (viewModel.getGameStatus().getValue() != GameStatus.IN_PROGRESS) return;
        if (!viewModel.getBoard().isCellEmpty(index)) return;
        
        viewModel.onCellClicked(index);
    }
    
    private void setupButtons(){
        binding.btnUndo.setOnClickListener( v -> {
            viewModel.undoLastMove();
            redrawEntireBoard();
        });

        binding.btnBackMenu.setOnClickListener( v -> finish());

        binding.btnResetMatch.setOnClickListener(v -> viewModel.restartMatch());
    }

    private void redrawEntireBoard() {
        for (int i = 0; i < cellViews.length; i++){
            Player player = viewModel.getBoard().getCell(i);
            cellViews[i].setSymbolColor(player == Player.X ? playerXColor : playerOColor);
            cellViews[i].setSymbolInstant(player);
        }
        binding.winLineView.clearLine();
    }

    private void configureViewModel(boolean firstLaunch){
        if (!firstLaunch) return; //

        Difficulty difficulty = Difficulty.MEDIUM;
        String diffName = getIntent().getStringExtra(EXTRA_DIFFICULTY);
        if (diffName != null){
            try {
                difficulty = Difficulty.valueOf(diffName);
            }catch (IllegalArgumentException e){

            }
        }

        Player humanSymbol = Player.X;
        String symbolName = getIntent().getStringExtra(EXTRA_HUMAN_SYMBOL);
        if (symbolName != null){
            try {
                humanSymbol = Player.valueOf(symbolName);
            }catch (IllegalArgumentException e){

            }
        }

        viewModel.configure(gameMode, difficulty, humanSymbol);
    }

    private void observeViewModel() {

        viewModel.getCurrentTurn().observe(this,this::updateTurnLabel);
        viewModel.getLastMove().observe(this, this::onMoveApplied);
        viewModel.getGameStatus().observe(this, this::onGameStatusChanged);
        viewModel.getWinLine().observe(this, this::onWinLineReceived);
        viewModel.getMatchScore().observe(this, this::updateScoreboard);

        viewModel.getAIThinking().observe(this, thinking -> {
            boolean isThinking = Boolean.TRUE.equals(thinking);
            binding.tvTurnIndicator.setText(isThinking ? getString(R.string.turn_computer_thinking)
                    : resolveTurnText());
        });

        viewModel.getBoardReset().observe(this, reset -> {
            if (Boolean.TRUE.equals(reset)){
                redrawEntireBoard();
            }
        });

        viewModel.getUndoAvailable().observe(this, available -> {
            binding.btnUndo.setEnabled(Boolean.TRUE.equals(available));
        });
    }

    private void updateScoreboard(GameStats stats) {
        if (stats == null) return;
        binding.tvScoreX.setText(String.valueOf(stats.getWinX()));
        binding.tvScoreO.setText(String.valueOf(stats.getWinO()));
        binding.tvScoreDraws.setText(String.valueOf(stats.getDraw()));
    }

    private void onWinLineReceived(WinLine winLine) {
        if (winLine == null){
            binding.winLineView.clearLine();
            return;
        }
        binding.winLineView.post(() -> drawWinLine(winLine));
    }

    private void drawWinLine(WinLine winLine) {
        int[] indexes = winLine.getCellIndexes();
        SymbolView startCell = cellViews[indexes[0]];
        SymbolView endCell = cellViews[indexes[2]];

        float[] startCenter = getCenterRelativeTo(startCell, binding.winLineView);
        float[] endCenter = getCenterRelativeTo(endCell,binding.winLineView);

        binding.winLineView.animateLine(startCenter[0],startCenter[1],endCenter[0],endCenter[1]);

    }

    private float[] getCenterRelativeTo(View child, View ancestor) {
        int[] childLocation = new int [2];
        int[] ancestorLocation = new int[2];
        child.getLocationOnScreen(childLocation);
        ancestor.getLocationOnScreen(ancestorLocation);

        float centerX = (childLocation[0] - ancestorLocation[0]) + child.getWidth() / 2f;
        float centerY = (childLocation[1] - ancestorLocation[1]) + child.getHeight() / 2f;
        return new float[]{centerX,centerY};
    }

    private void onGameStatusChanged(GameStatus status) {
        if (status == null || status == GameStatus.IN_PROGRESS) return;

        if (status == GameStatus.DRAW){
            soundManager.playDraw();
        }else {
            soundManager.playWin();
            vibrationManager.vibrateWIn();
        }

        binding.boardContainer.postDelayed(() -> showResultDialog(status), 480);
    }

    private void showResultDialog(GameStatus status) {
        String title;
        if (gameMode == GameMode.SOLO){
            Player human = resolveHumanPlayer();
            if (status == GameStatus.DRAW){
                title = getString(R.string.result_draw);
            } else if ((status == GameStatus.WIN_X && human == Player.X)
            || (status == GameStatus.WIN_O && human == Player.O)) {
                title = getString(R.string.result_you_win);

            }else {
                title = getString(R.string.result_you_lose);
            }
        }else {
            if (status == GameStatus.WIN_X){
                title = getString(R.string.result_win_x);
            } else if (status == GameStatus.WIN_O) {
                title = getString(R.string.result_win_o);
            }else {
                title = getString(R.string.result_draw);
            }
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton(R.string.btn_play_again,((dialogInterface, i) -> {
                    viewModel.startNewMatch();
                    redrawEntireBoard();
                }))
                .setNegativeButton(R.string.btn_back_to_menu,((dialogInterface, i) -> finish()))
                .show();

    }

    private Player resolveHumanPlayer() {
        String symbolName = getIntent().getStringExtra(EXTRA_HUMAN_SYMBOL);
        if (symbolName == null) return Player.X;

        try {
            return Player.valueOf(symbolName);
        }catch (IllegalArgumentException  e){
            return Player.X;
        }
    }

    private void onMoveApplied(Move move) {
        if (move == null) return;
        SymbolView view = cellViews[move.getCellIndex()];
        view.setSymbolColor(move.getPlayer() == Player.X ? playerXColor : playerOColor);
        view.setSymbolAnimated(move.getPlayer());

        if (move.getPlayer() == Player.X){
            soundManager.playMoveX();
        }else {
            soundManager.playMoveO();
        }
        vibrationManager.vibrateTap();
    }

    private void updateTurnLabel(Player player) {
        if (Boolean.TRUE.equals(viewModel.getAIThinking().getValue())) return;
        binding.tvTurnIndicator.setText(resolveTurnText());
    }

    private String resolveTurnText() {
        Player current = viewModel.getCurrentTurn().getValue();
        if (current == Player.O) return getString(R.string.turn_player_o);
        return getString(R.string.turn_player_x);
    }

    protected void onDestroy(){
        super.onDestroy();
        soundManager.release();
        binding = null;
    }
}