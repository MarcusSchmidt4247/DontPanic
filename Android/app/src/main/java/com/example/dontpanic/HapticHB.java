package com.example.dontpanic;

import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HapticHB extends AppCompatActivity {
    public static final String NAME = "Haptic Heartbeat";
    private float hapticStrength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haptic_hb);

        Button btnVibrate = findViewById(R.id.btnVibrate);

        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        final long[] pattern = {750, 250}; // sleep for 2000 milliseconds and vibrate for 1000 milliseconds
        final int[] strength = {0, 100};    // when grabbing from the DB via Settings, convert: value/100 = x /255.  Replace 100 with (int) 50 * 255 / 100


        VibrationEffect waveForm = VibrationEffect.createWaveform(pattern, strength, 1);

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
                    Toast.makeText(HapticHB.this, "Canceled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
