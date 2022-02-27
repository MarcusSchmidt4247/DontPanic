// DB: 2/24/22 - Modified this file to be the first screen of the app

package com.example.dontpanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;

public class FirstActivity extends AppCompatActivity {

    //Button emergencyButton;
    //Button generalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        /*
         * Returns the name of the main component registered from JavaScript. This is used to schedule
         * rendering of the component.
         */

        /*
         * Start up screen of default instance; this is what the user sees when the First Time launch
         * has already been done, and will be seen from then on (unless the User clears cache data)
         */

        setContentView(R.layout.activity_first);

        /*
        // Print the list of module names in the sequence with ID 1
        // MS: 2/23/22 - rewrote to use new Module enum
        ArrayList<Module> sequence = Database.GetModulesInSequence(1);
        if (sequence != null)
        {
            int count = 1;
            for (Module module : sequence)
            {
                Log.i("Module " + count, module.name);
                count++;
            }
        } else
            Log.e("Sequence Error", "Sequence is null");
         */

        /*
        //Stephen - may port over to a MainActivityController.java
        emergencyButton = findViewById(R.id.emerB);
        generalButton = findViewById(R.id.genB);
        emergencyButton.setOnClickListener(v -> {
            //this takes the main screen to the emergency screen/fragment

        });
        generalButton.setOnClickListener(v -> {
            //this takes the main screen to the general screen/fragment

        });
         */

        /*
            Possible interpretation of transferring to the next screens:
            Activity                   vs. Fragment
            - Independent instance         - dependent on the activity that created it
            - not too complicated(?)       - pretty simple to implement, but I haven't done it yet before - Stephen


      Activity code:
      public void switchToEmergencyActivity(View view) {
           Intent intent = new Intent(this, EmergencyScreenActivity.class);
           startActivity(intent);
      }
      public void switchToGeneralActivity(View view) {
           *      Intent intent = new Intent(this, GeneralScreenActivity.class);
           *      startActivity(intent);
           * }

      code above will be called in lines 37-40 and 41-44
     */

    }
}