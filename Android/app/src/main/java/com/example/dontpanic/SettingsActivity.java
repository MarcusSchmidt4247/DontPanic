// MS: 3/6/22 - initial code, plus setting sliders and switches to match database values
// MS: 3/10/22 - SeekBar listener pushes new value to the database
// MS: 3/20/22 - added more listeners and a back button

package com.example.dontpanic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

public class SettingsActivity extends AppCompatActivity
{
    SeekBar masterVolume, hapticStrength, textScale;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.masterVolume = findViewById(R.id.VolumeBar);
        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            int index = masterVolume.getProgress();
            masterVolume.setProgress(index + 1);
            return true;
        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            int index = masterVolume.getProgress();
            masterVolume.setProgress(index - 1);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void switchToGeneralSettings(View view) {
        setContentView(R.layout.activity_settings_general);

        this.masterVolume = findViewById(R.id.VolumeBar);
        this.hapticStrength = findViewById(R.id.HapticStrengthBar);

        try {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            masterVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

            masterVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (b) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
                        /*
                        Please double check this if this is correct - SC
                        Feel free to delete these two comments when done

                        float val = (float) seekBar.getProgress() / (float) (seekBar.getMax() / 2);
                        Database.SetPreference(Preferences.AUDIO_VOLUME_FLOAT, val);
                        Update, alright, it works
                        default is 1.0, with range between 0.0 - 2.0
                         */
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    float val = (float) seekBar.getProgress() / (float) (seekBar.getMax() / 2);
                    Database.SetPreference(Preferences.AUDIO_VOLUME_FLOAT, val);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            hapticStrength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) { }

                @Override public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override public void onStopTrackingTouch(SeekBar seekBar) {
                    float val = (float) seekBar.getProgress() / (float) (seekBar.getMax() / 2);
                    Database.SetPreference(Preferences.HAPTIC_STRENGTH_FLOAT, val);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        Object preference = Database.GetPreference(Preferences.AUDIO_VOLUME_FLOAT);
        float val;
        if (preference == null)
        {
            Log.e("Database Error", "Could not retrieve preference");
            val = 1.0f;
        }
        else
            val = (Float) preference;
        this.masterVolume.setProgress((int) ((this.masterVolume.getMax() / 2) * val));

        preference = Database.GetPreference(Preferences.HAPTIC_STRENGTH_FLOAT);
        if (preference == null)
        {
            Log.e("Database Error", "Could not retrieve preference");
            val = 1.0f;
        }
        else
            val = (Float) preference;
        this.hapticStrength.setProgress((int) ((this.hapticStrength.getMax() / 2) * val));

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
        hapticSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Database.SetPreference(Preferences.HAPTICS_ENABLED_BOOLEAN, isChecked);
            }
        });
    }

    public void switchToAccessibilitySettings(View view) {
        setContentView(R.layout.activity_settings_accessibility);
        this.textScale = findViewById(R.id.TextScaleBar);

        float val;
        Object preference = Database.GetPreference(Preferences.TEXT_SCALING_FLOAT);
        if (preference == null)
        {
            Log.e("Database Error", "Could not retrieve preference");
            val = 1.0f;
        }
        else
            val = (Float) preference;
        textScale.setProgress((int) ((textScale.getMax() / 2) * val));
        try {
            textScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) { }

                @Override public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override public void onStopTrackingTouch(SeekBar seekBar) {
                    float val = (float) seekBar.getProgress() / (float) (seekBar.getMax() / 2);
                    Database.SetPreference(Preferences.TEXT_SCALING_FLOAT, val);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchToAppSettings(View view) {
        setContentView(R.layout.activity_settings);
    }

    public void onBack(View view)
    {
        finish();
    }
}