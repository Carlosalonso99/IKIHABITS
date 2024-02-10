package com.example.controla_tus_habitos.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.example.controla_tus_habitos.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.dark_mode_switch);
        item.setActionView(R.layout.switch_item);
        SwitchCompat themeSwitch = item.getActionView().findViewById(R.id.theme_switch);

        // Configura el estado inicial del switch basado en el modo actual
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        themeSwitch.setChecked(nightModeFlags == Configuration.UI_MODE_NIGHT_YES);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            saveThemePreference(isChecked);

        });

        return true;
    }

    /**
     * Cueardo la preferencia que luego cargare en mi activity de login
     * @param isDarkMode
     */
    private void saveThemePreference(boolean isDarkMode) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("dark_mode", isDarkMode);
        editor.apply();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.itemHome) {
            // Maneja el clic en el ítem de configuración aquí
            Intent intent = new Intent(BaseActivity.this, HomeActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.itemGraphic) {
            Intent intent = new Intent(BaseActivity.this, GraficaActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }
}