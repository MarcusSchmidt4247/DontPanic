package com.example.dontpanic;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ModulesActivity extends AppCompatActivity {
    private int id;

    private TextView tvmName, tvmID, tvmClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tvmName = findViewById(R.id.tvmName);
        tvmID = findViewById(R.id.tvmName);
        tvmClass = findViewById(R.id.tvmClass);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);
        this.id = 1;

        Module module = Module.InstanceOf(id);
        Log.i("Module created", module.GetClass(id).toString());
        Log.i("Module Name", module.name);
        Log.i("Module ID", String.format("%d", module.id));

        //Bottom stuff below invokes crashing, as a NullPointer Exception keeps being thrown.  Will monitor this in a bit -SC

        //tvmName.setText(module.name);
        //tvmID.setText(String.format("d", module.id));

        /*
        if (module != null) {
            tvmName.setText(String.format("Name: %s", module.name));
            tvmID.setText(String.format("ID: %d", module.id));
            tvmClass.setText(String.format("Class: %s", module.GetClass(module.id).toString()));
        }
         */
    }
}