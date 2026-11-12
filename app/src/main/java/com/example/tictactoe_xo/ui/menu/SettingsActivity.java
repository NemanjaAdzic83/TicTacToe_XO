package com.example.tictactoe_xo.ui.menu;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tictactoe_xo.R;
import com.example.tictactoe_xo.databinding.ActivitySettingsBinding;
import com.example.tictactoe_xo.ui.widgets.ColorSwatchView;
import com.example.tictactoe_xo.viewmodel.SettingsViewModel;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private SettingsViewModel viewModel;

    private final List<ColorSwatchView> swatchesX = new ArrayList<>();
    private final List<ColorSwatchView> swatchesO = new ArrayList<>();

    private int[] paletteColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = ActivitySettingsBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());

       viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

       loadPalette();
       setupSwatches();
       setupColorPallets();
       observeViewModel();

       binding.btnResetStats.setOnClickListener( v -> confirmResetStats());
       binding.btnBackSettings.setOnClickListener(v-> finish());
    }




    private void loadPalette() {
        paletteColors = new int[]{
          getColor(R.color.player_color_cyan),
          getColor(R.color.player_color_magenta),
          getColor(R.color.player_color_yellow),
          getColor(R.color.player_color_green),
          getColor(R.color.player_color_purple),
          getColor(R.color.player_color_orange)
        };
    }

    private void setupSwatches() {
        binding.switchSound.setOnCheckedChangeListener((buttonView,isChecked) ->
                viewModel.setSoundEnabled(isChecked));

        binding.switchVibration.setOnCheckedChangeListener((buttonView,isChecked) ->
                viewModel.setVibrationEnabled(isChecked));
    }

    private void setupColorPallets() {
        swatchesX.clear();
        swatchesX.add(binding.swatchX1);
        swatchesX.add(binding.swatchX2);
        swatchesX.add(binding.swatchX3);
        swatchesX.add(binding.swatchX4);
        swatchesX.add(binding.swatchX5);
        swatchesX.add(binding.swatchX6);

        swatchesO.clear();
        swatchesO.add(binding.swatchO1);
        swatchesO.add(binding.swatchO2);
        swatchesO.add(binding.swatchO3);
        swatchesO.add(binding.swatchO4);
        swatchesO.add(binding.swatchO5);
        swatchesO.add(binding.swatchO6);

        for (int i = 0; i < swatchesX.size(); i++){
            int color = paletteColors[i];
            ColorSwatchView swatchView = swatchesX.get(i);
            swatchView.setColorValue(color);
            swatchView.setOnClickListener( v -> viewModel.setColorX(color));
        }

        for (int i = 0; i < swatchesO.size(); i++){
            int color = paletteColors[i];
            ColorSwatchView swatchView = swatchesO.get(i);
            swatchView.setColorValue(color);
            swatchView.setOnClickListener(v-> viewModel.setColorO(color));
        }

    }

    private void observeViewModel() {
        viewModel.getSoundEnabled().observe(this,enabled ->
                binding.switchSound.setChecked(Boolean.TRUE.equals(enabled)));

        viewModel.getVibrationEnabled().observe(this,enabled ->
                binding.switchVibration.setChecked(Boolean.TRUE.equals(enabled)));

        viewModel.getColorX().observe(this,color -> {
            if (color != null) updateSelection(swatchesX,color);
        });

        viewModel.getColorO().observe(this,color -> {
            if (color != null) updateSelection(swatchesO,color);
        });
    }

    private void updateSelection(List<ColorSwatchView> swatches, Integer selectedColor) {
        for (ColorSwatchView swatchView : swatches){
            swatchView.setSelected2(swatchView.getColorValue() == selectedColor);
        }
    }

    private void confirmResetStats() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.settings_reset_stats)
                .setMessage(R.string.settings_reset_stats_confirm)
                .setPositiveButton(R.string.dialog_yes,(dialog,which) ->{
                    viewModel.resetStats();
                    Toast.makeText(this, R.string.toast_stats_reset, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.dialog_no,null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}