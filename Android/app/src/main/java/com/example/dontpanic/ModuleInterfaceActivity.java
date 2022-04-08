// MS: 4/7/22 - added back button function

package com.example.dontpanic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ModuleInterfaceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_interface);
    }

    public void switchToModulesActivity(View view) {
        Intent intent = new Intent(this, ModulesActivity.class);
        startActivity(intent);
    }
    public void switchToModuleSequencesActivity(View view) {
        Intent intent = new Intent(this, ModuleSequencesActivity.class);
        startActivity(intent);
    }
    public void onBack(View view) {
        finish();
    }
}
