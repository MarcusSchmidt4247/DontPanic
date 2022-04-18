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

public class SelfReflectionFourActivity extends AppCompatActivity {
    public static final String NAME = "Self-Reflection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_reflection_four);

        EditText selfReflectionText7 = (EditText) findViewById(R.id.selfReflectionText7);

        // next button takes you to the fifth self reflection screen
        Button selfReflectionButton8 = findViewById(R.id.selfReflectionButton8);
        selfReflectionButton8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");
                Intent selfReflectionExercise8 = new Intent(getApplicationContext(), SelfReflectionFiveActivity.class);
                startActivity(selfReflectionExercise8);
            }
        });
    }
}
