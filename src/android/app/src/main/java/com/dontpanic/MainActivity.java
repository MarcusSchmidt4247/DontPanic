// MS: 2/2/22 - Created default React Native file

package com.dontpanic;

import android.os.Bundle;
import android.widget.Button;
import com.facebook.react.ReactActivity;

public class MainActivity extends ReactActivity {
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

    emergencyButton = findViewById(R.id.emerB);
    generalButton = findViewById(R.id.genB);

    emergencyButton.setOnClickListener(v -> {
      //this takes the main screen to the emergency screen/fragment

    });
    generalButton.setOnClickListener(v -> {
      //this takes the main screen to the general screen/fragment

    });
    /**
     * Possible interpretation of transferring to the next screens:
     * Activity                   vs.                   Fragment
     * - Independent instance                           - dependent on the activity that created it
     * - not too complicated(?)                         - pretty simple to implement, but I haven't done it yet before - Stephen
     *
     *
     * Activity code:
     * public void switchToEmergencyActivity(View view) {
     *      Intent intent = new Intent(this, EmergencyScreenActivity.class);
     *      startActivity(intent);
     * }
     * public void switchToGeneralActivity(View view) {
     *      *      Intent intent = new Intent(this, GeneralScreenActivity.class);
     *      *      startActivity(intent);
     *      * }
     *
     * code above will be called in lines 37-40 and 41-44
     */
  }
}
