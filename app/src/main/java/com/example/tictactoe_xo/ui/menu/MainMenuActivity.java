package com.example.tictactoe_xo.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tictactoe_xo.R;
import com.example.tictactoe_xo.databinding.ActivityMainMenuBinding;
import com.example.tictactoe_xo.model.GameMode;
import com.example.tictactoe_xo.ui.game.GameActivity;
import com.example.tictactoe_xo.util.InsetsHelper;

public class MainMenuActivity extends AppCompatActivity {

    private ActivityMainMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        InsetsHelper.applySystemBarPadding(binding.getRoot());

        animateLogo();
        setupClickListeners();


    }


    private void animateLogo() {
        Animation pulse = AnimationUtils.loadAnimation(this,R.anim.pulse_glow);
        binding.imgLogoX.startAnimation(pulse);

        Animation pulseDelayed = AnimationUtils.loadAnimation(this,R.anim.pulse_glow);
        pulseDelayed.setStartOffset(400);
        binding.imgLogoO.startAnimation(pulseDelayed);
    }

    private void setupClickListeners() {
        binding.btnSoloMode.setOnClickListener( v -> {
            startActivity(new Intent(this,DifficultySelectActivity.class)
                    .putExtra(DifficultySelectActivity.EXTRA_MODE, GameMode.SOLO.name()));
        });

        binding.btnDuoMode.setOnClickListener(v->{
            Intent intent = new Intent(this,GameActivity.class);
            intent.putExtra(GameActivity.EXTRA_GAME_MODE,GameMode.DUO.name());
            startActivity(intent);
        });

        binding.btnSettings.setOnClickListener(v-> {
            startActivity(new Intent(this,SettingsActivity.class));
        });

        binding.btnStats.setOnClickListener(v->{
            startActivity(new Intent(this,StatsActivity.class));
        });

        binding.btnExit.setOnClickListener(v-> finishAffinity());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}