package com.example.tictactoe_xo.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.tictactoe_xo.R;
import com.example.tictactoe_xo.model.GameStats;
import com.example.tictactoe_xo.repository.PreferencesRepository;


/**
 * ViewModel za ekran podesavanja - upralja zvukom , vibracijom , izborom boja,
 * igraca X i O , te prikazom i resetovanjem kumulativne statistike
 * Sve promjene se odmah prikazuju kroz PreferencesRepository
 */
public class SettingsViewModel extends AndroidViewModel {

    private final PreferencesRepository prefsRepo;

    private final MutableLiveData<Boolean> soundEnabled = new MutableLiveData<>();
    private final MutableLiveData<Boolean> vibrationEnabled = new MutableLiveData<>();
    private final MutableLiveData<Integer> colorX = new MutableLiveData<>();
    private final MutableLiveData<Integer> colorO = new MutableLiveData<>();
    private final MutableLiveData<GameStats> stats = new MutableLiveData<>();

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        prefsRepo = new PreferencesRepository(application);

        soundEnabled.setValue(prefsRepo.isSoundEnabled());
        vibrationEnabled.setValue(prefsRepo.isVibrationEnabled());

        int defaultX = ContextCompat.getColor(application, R.color.neon_cyan);
        int defaultO = ContextCompat.getColor(application, R.color.neon_magenta);

        colorX.setValue(prefsRepo.getPlayerXColor(defaultX));
        colorO.setValue(prefsRepo.getPlayerOColor(defaultO));

        stats.setValue(prefsRepo.loadStats());
    }

    public MutableLiveData<Boolean> getSoundEnabled() {
        return soundEnabled;
    }

    public MutableLiveData<Boolean> getVibrationEnabled() {
        return vibrationEnabled;
    }

    public MutableLiveData<Integer> getColorX() {
        return colorX;
    }

    public MutableLiveData<Integer> getColorO() {
        return colorO;
    }

    public MutableLiveData<GameStats> getStats() {
        return stats;
    }

    public void setVibrationEnabled(boolean enabled){
        prefsRepo.setVibrationEnabled(enabled);
        vibrationEnabled.setValue(enabled);
    }

    public void setSoundEnabled(boolean enabled){
        prefsRepo.setSoundEnabled(enabled);
        soundEnabled.setValue(enabled);
    }

    public void setColorX(int color){
        prefsRepo.setPLayerXColor(color);
        colorX.setValue(color);
    }
    public void setColorO(int color){
        prefsRepo.setPLayerOColor(color);
        colorO.setValue(color);
    }

    public void resetStats(){
        prefsRepo.resetStats();
        stats.setValue(prefsRepo.loadStats());
    }

    public void refreshStats(){
        stats.setValue(prefsRepo.loadStats());
    }

}
