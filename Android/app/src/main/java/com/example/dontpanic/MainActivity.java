// MS: 2/2/22 - Created default React Native file
// MS: 2/18/22 - Overrided onCreate() to interact with the database
// SC: 2/22/22 - Port-over of old code to new repo
// DB: 2/24/22 - Modified this file to be the splash screen of the app
// MS: 2/27/22 - Modified so that the splash screen will send the user to the appropriate screen after a few seconds
// SC & MA: 3/2/22 - full rebuild of MainActivity, introduction of Splash.java, and full redirection between screens
// MS: 3/20/22 - added text scaling
// MS: 4/1/22 - Emergency button now searches for default sequence to launch

package com.example.dontpanic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

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
            //********************************************************
            // To-do: check the database for the last logged in user *
            //********************************************************
            Database.currentUserID = 1;

            // Scale the text according to the user's preferences
            float textScale;
            Object preference = Database.GetPreference(Preferences.TEXT_SCALING_FLOAT);
            if (preference == null)
            {
                Log.e("Database Error", "Preference returned null");
                textScale = 1.0f;
            }
            else
                textScale = (Float) preference;
            AppCompatButton emergencyButton = findViewById(R.id.emerB);
            emergencyButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, emergencyButton.getTextSize() * textScale);
            AppCompatButton generalButton = findViewById(R.id.genB);
            generalButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, generalButton.getTextSize() * textScale);
        }
    }

    public void switchToEmergencyMode(View view) {
        boolean failedToLaunchPreference = true;
        ArrayList<Integer> sequences = Database.GetUserSequenceIDs();
        if (sequences != null && sequences.size() == 1) {
            // If the user has only one sequence, launch it regardless of the database setting
            Sequence sequence = Database.GetSequence(sequences.get(0));
            if (sequence != null) {
                //*********************************************
                // To-do: Replace with proper sequence launch *
                //*********************************************
                ArrayList<Module> modules = sequence.GetModules();
                if (modules != null) {
                    failedToLaunchPreference = false;
                    Intent intent = new Intent(this, Module.GetClass(modules.get(0).id));
                    startActivity(intent);
                }
            }
        }
        else if (sequences != null && sequences.size() > 1) {
            // If the user has more than one sequence, launch the one indicated by the database setting, or randomly if none is chosen
            int seqID;
            Object preference = Database.GetPreference(Preferences.LAUNCH_SEQUENCE_INT);
            if (preference == null) {
                Log.e("Database Error", "Preference returned null");
                seqID = -1;
            }
            else
                seqID = (Integer) preference;

            Sequence sequence = null;
            if (seqID != -1 && sequences.contains(seqID)) {
                // If a preference was found and it's in the list of this user's sequences, select it to be launched
                sequence = Database.GetSequence(seqID);
            }
            else {
                // If no preference was found or if this sequence doesn't belong to the user, pick a random sequence instead
                Random random = new Random();
                int index = random.nextInt(sequences.size());
                sequence = Database.GetSequence(sequences.get(index));
            }

            if (sequence != null) {
                //*********************************************
                // To-do: Replace with proper sequence launch *
                //*********************************************
                ArrayList<Module> modules = sequence.GetModules();
                if (modules != null && !modules.isEmpty())
                {
                    failedToLaunchPreference = false;
                    Intent intent = new Intent(this, Module.GetClass(modules.get(0).id));
                    startActivity(intent);
                }
            }
        }

        if (failedToLaunchPreference)
        {
            /* If the user's preference or a random sequence can't be launched for any reason,
               launch the highest rated module instead (for now, just pick one randomly) */
            Random random = new Random();
            int modID = random.nextInt(5);

            Intent intent = new Intent(this, Module.GetClass(modID));
            startActivity(intent);
        }
    }

    public void switchToGeneralUse(View view) {
        Intent intent = new Intent(this, GeneralUseActivity.class);
        startActivity(intent);
    }
}