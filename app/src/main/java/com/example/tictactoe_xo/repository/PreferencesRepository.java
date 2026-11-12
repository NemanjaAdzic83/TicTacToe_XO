package com.example.tictactoe_xo.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tictactoe_xo.model.GameStats;
import com.example.tictactoe_xo.model.GameStatus;

public class PreferencesRepository {

    /**
     * Repository za cuvanje podesavanja korisnika i kumulatine statistike partije pomocu
     * ShredPreferences. Sva citanja/pisanja prolaze kroz ovu klasu tako da ViewModel
     * ne mora znati o detaljima skladistenja
     */
    private static final String PREF_NAME = "tictactoe_prefs";

    private static final String KEY_SOUND_ENABLED = "sound_enabled";
    private static final String KEY_VIBRATION_ENABLED = "vibration_enabled";
    private static final String KEY_COLOR_X = "player_color_x";
    private static final String KEY_COLOR_O = "player_color_o";
    private static final String KEY_LAST_DIFFICULTY = "last_difficulty";


    private static final String KEY_STATS_WINS_X = "stats_win_x";
    private static final String KEY_STATS_WINS_O = "stats_wins_o";
    private static final String KEY_STATS_DRAWS = "stats_draws";


    private final SharedPreferences prefs;


    public PreferencesRepository(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
    }


    // ----- PODESAVANJE ZVUKA I VIBRACIJE ---------

    public boolean isSoundEnabled(){
        return prefs.getBoolean(KEY_SOUND_ENABLED, true);
    }

    public void setSoundEnabled(boolean enabled){
        prefs.edit().putBoolean(KEY_SOUND_ENABLED,enabled).apply();
    }

    public boolean isVibrationEnabled(){
        return prefs.getBoolean(KEY_VIBRATION_ENABLED,true);
    }

    public void setVibrationEnabled(boolean enabled){
        prefs.edit().putBoolean(KEY_VIBRATION_ENABLED,enabled).apply();
    }



    //-------------- BOJA IGRACA ----------------
    public int getPlayerXColor(int defaultColor){
        return prefs.getInt(KEY_COLOR_X,defaultColor);
    }

    public void setPLayerXColor(int colorRes){
        prefs.edit().putInt(KEY_COLOR_X,colorRes).apply();
    }

    public int getPlayerOColor(int defaultColor){
        return prefs.getInt(KEY_COLOR_O,defaultColor);
    }

    public void setPLayerOColor(int colorRes){
        prefs.edit().putInt(KEY_COLOR_O,colorRes).apply();
    }



    //------ POSLEDNJA IZABRANA TEZINA (RADI PAMCENJA PREFERENCI) -------------
    public String getLastDifficulty(){
        return prefs.getString(KEY_LAST_DIFFICULTY,"MEDIUM");
    }
    public void setLastDifficulty(String difficultyName){
        prefs.edit().putString(KEY_LAST_DIFFICULTY,difficultyName).apply();
    }


    //--------------- KUMULATIVNA STATISTIKA -----------
    public GameStats loadStats(){
        int x = prefs.getInt(KEY_STATS_WINS_X,0);
        int o = prefs.getInt(KEY_STATS_WINS_O,0);
        int d = prefs.getInt(KEY_STATS_DRAWS,0);
        return new GameStats(x,o,d);
    }

    public void saveStatsForResult(GameStatus status){
        GameStats current = loadStats();
        current.incrementForStatus(status);
        prefs.edit()
                .putInt(KEY_STATS_WINS_X, current.getWinX())
                .putInt(KEY_STATS_WINS_O, current.getWinO())
                .putInt(KEY_STATS_DRAWS, current.getDraw())
                .apply();
    }
    
    public void resetStats(){
        prefs.edit()
                .putInt(KEY_STATS_WINS_X,0)
                .putInt(KEY_STATS_WINS_O,0)
                .putInt(KEY_STATS_DRAWS,0)
                .apply();
    }

}
