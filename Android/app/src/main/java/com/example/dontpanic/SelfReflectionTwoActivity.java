// DB: 4/01/22 - Created Java file for Self Reflection Screen
// DB: 4/13/22 - Added functionality to the buttons to show the different exercises
// DB: 4/13/22 - Added string literal for module name

package com.example.dontpanic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SelfReflectionTwoActivity extends AppCompatActivity {
    public static final String NAME = "Self-Reflection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_reflection_two);

        // yes button takes you to the third self reflection screen
        Button selfReflectionButton3 = findViewById(R.id.selfReflectionButton3);
        selfReflectionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");
                Intent selfReflectionExercise3 = new Intent(getApplicationContext(), SelfReflectionThreeActivity.class);
                startActivity(selfReflectionExercise3);
            }
        });

        // no button takes you to the third self reflection screen
        Button selfReflectionButton4 = findViewById(R.id.selfReflectionButton4);
        selfReflectionButton4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");
                Intent selfReflectionExercise4 = new Intent(getApplicationContext(), SelfReflectionThreeActivity.class);
                startActivity(selfReflectionExercise4);
            }
        });
    }
}
