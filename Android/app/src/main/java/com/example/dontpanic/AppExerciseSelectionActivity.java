package com.example.dontpanic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AppExerciseSelectionActivity extends AppCompatActivity {
    public static final String NAME = "App Activities";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appexercise_selection);

        // First Exercise Button - Follow the Circle
        Button appButton = findViewById(R.id.appButton);
        appButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");
                Intent appExerciseOne = new Intent(getApplicationContext(), AppExerciseOneActivity.class);
                startActivity(appExerciseOne);
            }
        });

        // Second Exercise Button - Click Circles
        Button appButton2 = findViewById(R.id.appButton2);
        appButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");
                Intent appExerciseTwo = new Intent(getApplicationContext(), AppExerciseTwoActivity.class);
                startActivity(appExerciseTwo);
            }
        });

    }

    public void onBack(View view)
    {
        Database.CompletedModule(Module.GetID(NAME));
        finish();
    }
}
