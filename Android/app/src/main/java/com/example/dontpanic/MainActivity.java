// MS: 2/2/22 - Created default React Native file
// MS: 2/18/22 - Overrided onCreate() to interact with the database
// SC: 2/22/22 - Port-over of old code to new repo

package com.example.dontpanic;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button emergencyButton;
    Button generalButton;

    /*
     * Returns the name of the main component registered from JavaScript. This is used to schedule
     * rendering of the component.
     */
    /*
    @Override
    protected String getMainComponentName() {
        return "Don'tPanic";
    }
    This shouldn't be needed anymore, I believe - SC
    */

    /*
     * Start up screen of default instance; this is what the user sees when the First Time launch
     * has already been done, and will be seen from then on (unless the User clears cache data)
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the database
        if (!Database.DatabaseExists(getApplicationContext()))
        {
         /* If this is the first time launching the app (the database didn't already exist),
            then add some sample data */
            Log.i("First Time Launch", "Creating sample data");
            int usrID = Database.CreateUser("John");
            Database.currentUserID = usrID;
            int seqID = Database.CreateSequence(usrID, "Main Sequence");
            Database.AppendModuleToSequence(seqID, 2);
            Database.AppendModuleToSequence(seqID, 3);
            Database.InsertModuleIntoSequence(seqID, 1, 1);
            setContentView(R.layout.first_screen);
        } else {
            Database.currentUserID = 1;
            //If Database already exists, launch first page screen
            setContentView(R.layout.activity_main);
        }

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
        }
        else
            Log.e("Sequence Error", "Sequence is null");

        //Stephen - may port over to a MainActivityController.java
        emergencyButton = findViewById(R.id.emerB);
        generalButton = findViewById(R.id.genB);
        emergencyButton.setOnClickListener(v -> {
            //this takes the main screen to the emergency screen/fragment

        });
        generalButton.setOnClickListener(v -> {
            //this takes the main screen to the general screen/fragment

        });
    /*
      Possible interpretation of transferring to the next screens:
      Activity                   vs.                   Fragment
      - Independent instance                           - dependent on the activity that created it
      - not too complicated(?)                         - pretty simple to implement, but I haven't done it yet before - Stephen


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
