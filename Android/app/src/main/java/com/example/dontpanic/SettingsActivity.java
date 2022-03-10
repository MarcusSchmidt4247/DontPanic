// MS: 3/6/22 - initial code

package com.example.dontpanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
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
        this.masterVolume = findViewById(R.id.VolumeBar);
        this.hapticStrength = findViewById(R.id.HapticStrengthBar);
        this.textScale = findViewById(R.id.TextScaleBar);
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
        try {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            masterVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

            masterVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (b) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            hapticStrength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void switchToAccessibilitySettings(View view) {
        setContentView(R.layout.activity_settings_accessibility);
    }
    public void switchToAppSettings(View view) {
        setContentView(R.layout.activity_settings);
    }
}