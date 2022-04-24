// DB: 3/18/22 - Created Java file for Mental Exercise Third Screen
// DB: 3/21/22 - Added functionality to the button to go back to the exercise selection
// MS: 4/24/22 - changed intent to finish statement for proper app flow

package com.example.dontpanic;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MentalExerciseThreeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentalexercise_three);

        // back button takes you to the exercise selection screen
        Button exerciseThreeButton = findViewById(R.id.exerciseThreeButton);
        exerciseThreeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");
                finish();
            }
        });

    }
}
