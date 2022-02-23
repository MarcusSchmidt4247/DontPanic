// MS: 2/2/22 - Created default React Native file
// MS: 2/18/22 - Overrided onCreate() to interact with the database
// SC: 2/22/22 - Port-over of old code to new repo

package com.dontpanic;

import android.os.Bundle;
import android.util.Log;

import android.widget.Button;

import java.util.ArrayList;

public class MainActivity {
    Button emergencyButton;
    Button generalButton;

    Bundle bundle;

    /**
     * Returns the name of the main component registered from JavaScript. This is used to schedule
     * rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "Don'tPanic";
    }

    /**
     * Start up screen of default instance; this is what the user sees when the First Time launch
     * has already been done, and will be seen from then on (unless the User clears cache data)
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout);

        // Initialize the database
        if (!Database.DatabaseExists(getApplicationContext()))
        {
        /* If this is the first time launching the app (the database didn't already exist),
           then add some sample data */
            Log.i("First Time Launch", "Creating sample data");
            int usrID = Database.CreateUser("John");
            int seqID = Database.CreateSequence(usrID, "Main Sequence");
            Database.AppendModuleToSequence(seqID, 2);
            Database.AppendModuleToSequence(seqID, 3);
            Database.InsertModuleIntoSequence(seqID, 1, 1);
        } else {
            //Check if Database exists; if not, launch first page screen

        }

        // Print the list of module names in the sequence with ID 1
        ArrayList<Integer> sequence = Database.GetModulesInSequence(1);
        if (sequence != null)
        {
            for (int i = 0; i < sequence.size(); i++)
            {
                Log.i("Module " + (i + 1), Database.GetModuleName(sequence.get(i)));
            }
        }
        else
            Log.e("Sequence Error", "Sequence is null");

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
