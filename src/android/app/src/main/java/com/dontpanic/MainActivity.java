// MS: 2/2/22 - Created default React Native file
// MS: 2/18/22 - Overrided onCreate() to interact with the database

package com.dontpanic;

import android.os.Bundle;
import android.util.Log;

import com.facebook.react.ReactActivity;

import java.util.ArrayList;

public class MainActivity extends ReactActivity {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "DontPanic";
  }

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
      super.onCreate(savedInstanceState);

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
  }
}
