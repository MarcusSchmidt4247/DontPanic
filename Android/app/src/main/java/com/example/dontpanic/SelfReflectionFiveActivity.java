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

public class SelfReflectionFiveActivity extends AppCompatActivity {
    public static final String NAME = "Self-Reflection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_reflection_five);

        EditText selfReflectionText9 = (EditText) findViewById(R.id.selfReflectionText9);

        // next button takes you to the sixth self reflection screen
        Button selfReflectionButton10 = findViewById(R.id.selfReflectionButton10);
        selfReflectionButton10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");
                Intent selfReflectionExercise10 = new Intent(getApplicationContext(), SelfReflectionSixActivity.class);
                startActivity(selfReflectionExercise10);
            }
        });
    }
}
