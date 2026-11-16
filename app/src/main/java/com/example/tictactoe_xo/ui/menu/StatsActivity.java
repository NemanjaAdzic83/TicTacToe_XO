package com.example.tictactoe_xo.ui.menu;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tictactoe_xo.R;
import com.example.tictactoe_xo.databinding.ActivityStatsBinding;
import com.example.tictactoe_xo.model.GameStats;
import com.example.tictactoe_xo.model.SoloStats;
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

        viewModel.getSoloStats().observe(this, this::renderSoloStats);
        viewModel.getDuoStats().observe(this, this::renderDuoStats);

        binding.btnBackStats.setOnClickListener(v-> finish());

    }

    private void renderSoloStats(SoloStats soloStats) {
        if (soloStats == null) return;

        binding.tvHumanWins.setText(String.valueOf(soloStats.getHumanWins()));
        binding.tvAiWins.setText(String.valueOf(soloStats.getAiWins()));
        binding.tvSoloDraws.setText(String.valueOf(soloStats.getDraws()));

        if (soloStats.getTotalGames() == 0) {
            binding.tvWinRate.setText(R.string.stats_no_value);
        } else {
            binding.tvWinRate.setText(String.format(Locale.getDefault(), "%.0f%%", soloStats.getHumanWinRate()));
        }
        updateEmptyState();
    }

    private void renderDuoStats(GameStats duoStats) {
        if (duoStats == null) return;

        binding.tvXWins.setText(String.valueOf(duoStats.getWinX()));
        binding.tvOWins.setText(String.valueOf(duoStats.getWinO()));
        binding.tvDraws.setText(String.valueOf(duoStats.getDraw()));
        updateEmptyState();
    }

    // The empty-state message is shown only when no game was played in either mode
    private void updateEmptyState() {
        SoloStats solo = viewModel.getSoloStats().getValue();
        GameStats duo = viewModel.getDuoStats().getValue();
        int total = (solo != null ? solo.getTotalGames() : 0) + (duo != null ? duo.getTotalGames() : 0);
        binding.tvEmptyStateStats.setVisibility(total == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}