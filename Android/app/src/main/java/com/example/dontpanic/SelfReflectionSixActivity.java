// DB: 4/01/22 - Created Java file for Self Reflection Screen
// DB: 4/13/22 - Added functionality to the buttons to show the different exercises
// DB: 4/13/22 - Added string literal for module name

package com.example.dontpanic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SelfReflectionSixActivity extends AppCompatActivity {
    public static final String NAME = "Self-Reflection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_reflection_six);

        EditText selfReflectionText11 = (EditText) findViewById(R.id.selfReflectionText11);

        // next button takes you to the sixth self reflection screen
        Button selfReflectionButton12 = findViewById(R.id.selfReflectionButton12);
        selfReflectionButton12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");
                Intent selfReflectionExercise12 = new Intent(getApplicationContext(), MentalExerciseSelectionActivity.class);
                startActivity(selfReflectionExercise12);
            }
        });
    }
}
