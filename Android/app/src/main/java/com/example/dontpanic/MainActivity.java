// MS: 2/2/22 - Created default React Native file
// MS: 2/18/22 - Overrided onCreate() to interact with the database
// SC: 2/22/22 - Port-over of old code to new repo
// DB: 2/24/22 - Modified this file to be the splash screen of the app
// MS: 2/27/22 - Modified so that the splash screen will send the user to the appropriate screen after a few seconds

package com.example.dontpanic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageSplashScreen = (ImageView) findViewById(R.id.imageSplashScreen);
        imageSplashScreen.setImageResource(R.drawable.icon);

        Intent destinationIntent;

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
            destinationIntent = new Intent(this, FirstActivity.class);
        } else {
            Database.currentUserID = 1;

            // Because the user has used the app before, send them to the main screen (for now the guided breathing app)
            destinationIntent = new Intent(this, GuidedBreathing.class);
        }

        // Wait three seconds on the splash screen and then launch the intent to go to the next screen
        // Source: https://stackoverflow.com/questions/17237287
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(destinationIntent);
                finish();
            }
        }, 3000);
    }
}
