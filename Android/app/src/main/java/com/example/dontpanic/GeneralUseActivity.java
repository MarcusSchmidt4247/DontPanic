package com.example.dontpanic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GeneralUseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_use);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        // Scale the text according to the user's preferences
        Object preference = Database.GetPreference(Preferences.TEXT_SCALING_FLOAT);
        float textScale;
        if (preference == null)
        {
            Log.e("Database Error", "Preference returned null");
            textScale = 1.0f;
        }
        else
            textScale = (Float) preference;

        TextView appName = findViewById(R.id.MainView);
        appName.setTextSize(TypedValue.COMPLEX_UNIT_PX, 80 * textScale);

        TextView dailyThoughtHeader = findViewById(R.id.MainView2);
        dailyThoughtHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50 * textScale);
        Button dailyThoughtButton = findViewById(R.id.mainButton);
        dailyThoughtButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40 * textScale);
        Button readMoreButton = findViewById(R.id.mainButton2);
        readMoreButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, 25 * textScale);

        Button moduleButton = findViewById(R.id.buttonModules);
        moduleButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, 32 * textScale);
        Button settingsButton = findViewById(R.id.buttonSettings);
        settingsButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, 32 * textScale);
        Button notificationButton = findViewById(R.id.buttonNotifications);
        notificationButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, 32 * textScale);
        Button helpButton = findViewById(R.id.buttonHelp);
        helpButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, 32 * textScale);
    }

    public void switchToModuleInterfaceActivity(View view) {
        Intent intent = new Intent(this, ModuleInterfaceActivity.class);
        startActivity(intent);
    }
    public void switchToSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    public void switchToNotificationsActivity(View view) {
        Intent intent = new Intent(this, NotificationsActivity.class);
        startActivity(intent);
    }
    public void switchToHelpActivity(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }
}