package com.example.tictactoe_xo.ui.menu;



import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.tictactoe_xo.R;
import com.example.tictactoe_xo.databinding.ActivityStatsBinding;
import com.example.tictactoe_xo.model.GameStats;
import com.example.tictactoe_xo.util.InsetsHelper;
import com.example.tictactoe_xo.viewmodel.SettingsViewModel;

import java.util.Locale;

public class StatsActivity extends AppCompatActivity {

    private ActivityStatsBinding binding;
    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        InsetsHelper.applySystemBarPadding(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        viewModel.refreshStats();

        viewModel.getStats().observe(this, this::renderStats);

        binding.btnBackStats.setOnClickListener(v-> finish());

    }

    private void renderStats(GameStats gameStats) {
        if (gameStats == null) return;

        int total = gameStats.getTotalGames();

        binding.tvTotalGames.setText(String.valueOf(total));
        binding.tvXWins.setText(String.valueOf(gameStats.getWinX()));
        binding.tvOWins.setText(String.valueOf(gameStats.getWinO()));
        binding.tvDraws.setText(String.valueOf(gameStats.getDraw()));

        if (total == 0){
           binding.tvWinRate.setText("-");
           binding.tvEmptyStateStats.setVisibility(View.VISIBLE);
        }else {
            binding.tvWinRate.setText(String.format(Locale.getDefault(), "%.0f%%", gameStats.getWinRateX()));
            binding.tvEmptyStateStats.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}