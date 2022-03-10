// MS: 3/6/22 - initial code

package com.example.dontpanic;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void switchToGeneralSettings(View view) {
        setContentView(R.layout.activity_settings_general);
    }
    public void switchToAccessibilitySettings(View view) {
        setContentView(R.layout.activity_settings_accessibility);
    }
    public void switchToAppSettings(View view) {
        setContentView(R.layout.activity_settings);
    }
}