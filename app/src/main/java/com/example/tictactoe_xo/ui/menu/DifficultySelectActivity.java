package com.example.tictactoe_xo.ui.menu;



import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tictactoe_xo.databinding.ActivityDifficultySelectBinding;
import com.example.tictactoe_xo.model.Difficulty;
import com.example.tictactoe_xo.model.GameMode;
import com.example.tictactoe_xo.model.Player;
import com.example.tictactoe_xo.ui.game.GameActivity;

/**
 * Ekran za izbor nivoa tezine AI protivnika (lako/srednje/tesko) i izbor simbola kojim ce igrac igrati prije pokretanja solo partije
 */

public class DifficultySelectActivity extends AppCompatActivity {

    public static final  String EXTRA_MODE = "extra_mode";
    private ActivityDifficultySelectBinding binding;
    private Difficulty selectedDifficulty = Difficulty.MEDIUM;
    private Player selectedSymbol = Player.X;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDifficultySelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupDifficultyCards();
        setupSymbol();
        highlightSelectedDifficulty();
        highlightSelectedSymbol();

        binding.btnStartGame.setOnClickListener( v-> startSoloGame());
        binding.btnBack.setOnClickListener( v-> finish());

    }

    private void setupSymbol() {
        binding.btnSymbolX.setOnClickListener( v ->{
            selectedSymbol = Player.X;
            highlightSelectedSymbol();
        });

        binding.btnSymbolO.setOnClickListener( v -> {
            selectedSymbol = Player.O;
            highlightSelectedSymbol();
        });
    }

    private void highlightSelectedSymbol() {
        float dimAlpha = 0.4f;
        binding.btnSymbolX.setAlpha(selectedSymbol == Player.X ? 1f : dimAlpha);
        binding.btnSymbolO.setAlpha(selectedSymbol == Player.O ? 1f : dimAlpha);
    }

    private void setupDifficultyCards() {
        binding.cardEasy.setOnClickListener( v -> {
            selectedDifficulty = Difficulty.EASY;
            highlightSelectedDifficulty();
        });

        binding.cardMedium.setOnClickListener( v -> {
            selectedDifficulty = Difficulty.MEDIUM;
            highlightSelectedDifficulty();
        });

        binding.cardHard.setOnClickListener( v -> {
            selectedDifficulty = Difficulty.HARD;
            highlightSelectedDifficulty();
        });
    }

    private void highlightSelectedDifficulty() {
        float dimAlpha = 0.45f;
        binding.cardEasy.setAlpha(selectedDifficulty == Difficulty.EASY ? 1f : dimAlpha);
        binding.cardMedium.setAlpha(selectedDifficulty == Difficulty.MEDIUM ? 1f : dimAlpha);
        binding.cardHard.setAlpha(selectedDifficulty == Difficulty.HARD ? 1f : dimAlpha);

        binding.cardEasy.setStrokeWidth(selectedDifficulty == Difficulty.EASY ? densityToPx(3) : 0);
        binding.cardMedium.setStrokeWidth(selectedDifficulty  == Difficulty.MEDIUM ? densityToPx(3) : 0);
        binding.cardHard.setStrokeWidth(selectedDifficulty == Difficulty.HARD ? densityToPx(3) : 0);
    }

    private int densityToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }

    private void startSoloGame(){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.EXTRA_GAME_MODE, GameMode.SOLO.name());
        intent.putExtra(GameActivity.EXTRA_DIFFICULTY, selectedDifficulty.name());
        intent.putExtra(GameActivity.EXTRA_DIFFICULTY, selectedDifficulty.name());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;

        }

}