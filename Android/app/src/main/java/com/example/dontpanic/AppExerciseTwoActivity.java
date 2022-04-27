package com.example.dontpanic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AppExerciseTwoActivity extends AppCompatActivity {

    private Button twoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appexercise_two);

        com.example.dontpanic.CircleView c =
                (com.example.dontpanic.CircleView) findViewById(R.id.circle);

        // back button takes you to the exercise selection screen
        Button twoBack = findViewById(R.id.twoBackButton);
        twoBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
