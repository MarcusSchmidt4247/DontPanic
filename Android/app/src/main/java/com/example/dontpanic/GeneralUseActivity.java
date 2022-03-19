package com.example.dontpanic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class GeneralUseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_use);
    }

    public void switchToModuleInterfaceActivity(View view) {
        System.out.println("hello");
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