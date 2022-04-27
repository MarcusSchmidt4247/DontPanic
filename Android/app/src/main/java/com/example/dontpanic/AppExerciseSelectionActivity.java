package com.example.dontpanic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AppExerciseSelectionActivity extends AppCompatActivity {

    private Button appButton, appButton2, appBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appexercise_selection);

        // First Exercise Button - Follow the Circle
        Button appButton = findViewById(R.id.appButton);
        appButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");
                openActivity();
            }
        });

        // Second Exercise Button - Click Circles
        Button appButton2 = findViewById(R.id.appButton2);
        appButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button Clicked");
                openActivity2();
            }
        });

        Button appBack = findViewById(R.id.back_button);
        appBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Button Clicked");
                finish();
            }
        });
    }


    public void openActivity() {
        Intent appExerciseOne = new Intent(getApplicationContext(), AppExerciseOneActivity.class);
        startActivity(appExerciseOne);
    }

    public void openActivity2() {
        Intent appExerciseTwo = new Intent(getApplicationContext(), AppExerciseTwoActivity.class);
        startActivity(appExerciseTwo);
    }


}
