// DB: 4/01/22 - Created Java file for Self Reflection Screen
// DB: 4/13/22 - Added functionality to the buttons to show the different exercises
// DB: 4/13/22 - Added string literal for module name

package com.example.dontpanic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SelfReflectionFourActivity extends AppCompatActivity {
    public static final String NAME = "Self-Reflection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_reflection_three);

        // yes button takes you to the second self reflection screen
        Button selfReflectionButton5 = findViewById(R.id.selfReflectionButton5);
        selfReflectionButton5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");
                Intent selfReflectionExercise5 = new Intent(getApplicationContext(), SelfReflectionFourActivity.class);
                startActivity(selfReflectionExercise5);
            }
        });

        // no button takes you to the second self reflection screen
        Button selfReflectionButton6 = findViewById(R.id.selfReflectionButton6);
        selfReflectionButton6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");
                Intent selfReflectionExercise6 = new Intent(getApplicationContext(), SelfReflectionFourActivity.class);
                startActivity(selfReflectionExercise6);
            }
        });
    }
}
