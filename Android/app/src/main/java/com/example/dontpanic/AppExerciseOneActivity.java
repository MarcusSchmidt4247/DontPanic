package com.example.dontpanic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AppExerciseOneActivity extends AppCompatActivity {

    private Button oneBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appexercise_one);

        /* IN PROGRESS */

        // back button takes you to the exercise selection screen
        Button oneBack = findViewById(R.id.oneBackButton);
        oneBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
