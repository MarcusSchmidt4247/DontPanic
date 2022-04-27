package com.example.dontpanic;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class HapticHB extends AppCompatActivity {
    public static final String NAME = "Haptic Heartbeat";
    private float hapticStrength;
    public Vibrator vib;
    public Button btnVibrate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //start a sound file stating to bring the device over the recipient's heart, or hold the phone and close their eyes
        //have a display of the BPM, possibly have a slider between 60-100 BPM in the settings
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haptic_hb);

        btnVibrate = findViewById(R.id.btnVibrate);
        vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        try {
            MediaPlayer audioPlayer = MediaPlayer.create(this, R.raw.haptic_hb_startup);
            audioPlayer.start();
            TimeUnit.SECONDS.sleep(8);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Object preference = Database.GetPreference(Preferences.HAPTIC_STRENGTH_FLOAT);
        if (preference != null) {
            hapticStrength = (float) preference;
        }

        final long[] pattern = {550, 125, 125};     // 75 BPM  Maybe go 60?  Lower BPM definitely is more soothing, but average resting BPM of 75 is nice
        final int[] strength = {0, 100, 50 };       // Upbeat : 100, downbeat: 50

        VibrationEffect waveForm = VibrationEffect.createWaveform(pattern, strength, 1);
        vib.vibrate(waveForm);
        btnVibrate.setText("Stop");

        btnVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnVibrate.getText().toString().equals("Start")) {
                    vib.vibrate(waveForm);
                    Toast.makeText(HapticHB.this, "Started", Toast.LENGTH_SHORT).show();
                    btnVibrate.setText("Stop");
                } else {
                    vib.cancel();
                    btnVibrate.setText("Start");
                    Toast.makeText(HapticHB.this, "Stopped", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {         //stops the HB as well
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            vib.cancel();
            btnVibrate.setText("Start");
            Toast.makeText(HapticHB.this, "Stopped", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void onBack(View view) {
        Database.CompletedModule(Module.GetID(NAME));
        vib.cancel();
        finish();
    }
}
