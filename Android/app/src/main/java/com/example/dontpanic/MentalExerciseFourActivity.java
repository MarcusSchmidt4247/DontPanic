// DB: 3/18/22 - Created Java file for Mental Exercise Fourth Screen
// DB: 3/21/22 - Added functionality to the button to go back to the exercise selection

package com.example.dontpanic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MentalExerciseFourActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentalexercise_four);

        // back button takes you to the exercise selection screen
        Button exerciseFourButton = findViewById(R.id.exerciseFourButton);
        exerciseFourButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");
                Intent exerciseFourSelection = new Intent(getApplicationContext(), MentalExerciseSelectionActivity.class);
                startActivity(exerciseFourSelection);
            }
        });

    }
}
