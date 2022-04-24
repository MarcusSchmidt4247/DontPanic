// DB: 4/01/22 - Created Java file for Self Reflection Screen
// DB: 4/13/22 - Added functionality to the buttons to show the different exercises
// DB: 4/13/22 - Added string literal for module name
// MS: 4/24/22 - Added finish statement

package com.example.dontpanic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SelfReflectionActivity extends AppCompatActivity {
    public static final String NAME = "Self-Reflection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_reflection);

        // yes button takes you to the second self reflection screen
        Button selfReflectionButton1 = findViewById(R.id.selfReflectionButton1);
        selfReflectionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");
                Intent selfReflectionExercise1 = new Intent(getApplicationContext(), SelfReflectionTwoActivity.class);
                startActivity(selfReflectionExercise1);
                finish();
            }
        });

        // no button takes you to the second self reflection screen
        Button selfReflectionButton2 = findViewById(R.id.selfReflectionButton2);
        selfReflectionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");
                Intent selfReflectionExercise2 = new Intent(getApplicationContext(), SelfReflectionTwoActivity.class);
                startActivity(selfReflectionExercise2);
                finish();
            }
        });
    }
}

