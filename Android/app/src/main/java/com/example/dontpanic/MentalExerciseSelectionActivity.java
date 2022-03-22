// DB: 3/18/22 - Created Java file for Mental Exercise Selection Screen
// DB: 3/21/22 - Added functionality to the buttons to show the different exercises

package com.example.dontpanic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class MentalExerciseSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentalexercise_selection);

        // first button takes you to the first exercise screen
        Button mentalButton = findViewById(R.id.mentalButton);
        mentalButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");
                Intent mentalExerciseOne = new Intent(getApplicationContext(), MentalExerciseOneActivity.class);
                startActivity(mentalExerciseOne);
            }
        });

        // two button takes you to the two exercise screen
        Button mentalButton2 = findViewById(R.id.mentalButton2);
        mentalButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");
                Intent mentalExerciseTwo = new Intent(getApplicationContext(), MentalExerciseTwoActivity.class);
                startActivity(mentalExerciseTwo);
            }
        });

        // third button takes you to the third exercise screen
        Button mentalButton3 = findViewById(R.id.mentalButton3);
        mentalButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");
                Intent mentalExerciseThree = new Intent(getApplicationContext(), MentalExerciseThreeActivity.class);
                startActivity(mentalExerciseThree);
            }
        });

        // four button takes you to the four exercise screen
        Button mentalButton4 = findViewById(R.id.mentalButton4);
        mentalButton4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");
                Intent mentalExerciseFour = new Intent(getApplicationContext(), MentalExerciseFourActivity.class);
                startActivity(mentalExerciseFour);
            }
        });

    }
}
