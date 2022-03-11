// MS: 3/6/22 - initial code, plus setting sliders and switches to match database values

package com.example.dontpanic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //************************************************************
        // Set the progress of the various sliding scale preferences *
        //************************************************************

        Object preference = Database.GetPreference(Preferences.AUDIO_VOLUME_FLOAT);
        float val;
        if (preference == null)
        {
            Log.e("Database Error", "Could not retrieve preference");
            val = 1.0f;
        }
        else
            val = (Float) preference;
        SeekBar volumeBar = findViewById(R.id.VolumeBar);
        Log.e("Volume", val + " -> " + (int) ((volumeBar.getMax() / 2) * val));
        volumeBar.setProgress((int) ((volumeBar.getMax() / 2) * val));

        preference = Database.GetPreference(Preferences.HAPTIC_STRENGTH_FLOAT);
        if (preference == null)
        {
            Log.e("Database Error", "Could not retrieve preference");
            val = 1.0f;
        }
        else
            val = (Float) preference;
        SeekBar hapticStrength = findViewById(R.id.HapticStrengthBar);
        hapticStrength.setProgress((int) ((hapticStrength.getMax() / 2) * val));

        preference = Database.GetPreference(Preferences.TEXT_SCALING_FLOAT);
        if (preference == null)
        {
            Log.e("Database Error", "Could not retrieve preference");
            val = 1.0f;
        }
        else
            val = (Float) preference;
        SeekBar textScaleBar = findViewById(R.id.TextScaleBar);
        textScaleBar.setProgress((int) ((textScaleBar.getMax() / 2) * val));

        //********************************************
        // Set the status of the boolean preferences *
        //********************************************

        preference = Database.GetPreference(Preferences.HAPTICS_ENABLED_BOOLEAN);
        boolean on;
        if (preference == null)
        {
            Log.e("Database Error", "Could not retrieve preference");
            on = false;
        }
        else
            on = (boolean) preference;
        SwitchCompat hapticSwitch = findViewById(R.id.HapticSwitch);
        hapticSwitch.setChecked(on);
    }
}