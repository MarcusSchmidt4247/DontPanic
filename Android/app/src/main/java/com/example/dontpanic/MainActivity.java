// MS: 2/2/22 - Created default React Native file
// MS: 2/18/22 - Overrided onCreate() to interact with the database
// SC: 2/22/22 - Port-over of old code to new repo
// DB: 2/24/22 - Modified this file to be the splash screen of the app
// MS: 2/27/22 - Modified so that the splash screen will send the user to the appropriate screen after a few seconds
// SC & MA: 3/2/22 - full rebuild of MainActivity, introduction of Splash.java, and full redirection between screens

package com.example.dontpanic;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent[] destinationIntent = new Intent[1];

        // Initialize the database
        if (!Database.DatabaseExists(getApplicationContext()) || !Database.UserExists())
        {
            /* If this is the first time launching the app (the database didn't already exist or there are no user accounts),
               then add some sample data */
            Log.i("First Time Launch", "Creating sample data");
            int usrID = Database.CreateUser("John");
            Database.currentUserID = usrID;
            int seqID = Database.CreateSequence(usrID, "Main Sequence");
            Database.AppendModuleToSequence(seqID, 2);
            Database.AppendModuleToSequence(seqID, 3);
            Database.InsertModuleIntoSequence(seqID, 1, 1);

            // Because this is the user's first time launching the app, send them to the first-use screen
            destinationIntent[0] = new Intent(this, FirstActivity.class);
            startActivity(destinationIntent[0]);
        } else {
            Database.currentUserID = 1;
        }
    }

    public void switchToEmergencyMode(View view) {
        Intent intent = new Intent(this, GuidedBreathing.class);
        startActivity(intent);
    }
    public void switchToGeneralUse(View view) {
        Intent intent = new Intent(this, GeneralUseActivity.class);
        startActivity(intent);
    }
}