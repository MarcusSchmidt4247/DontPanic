package com.example.dontpanic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ModulesActivity extends AppCompatActivity {
    private int id;

    private TextView tvmName, tvmID, tvmClass;
    private Button btnModule0, btnModule1;
    private Module tempModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);
        this.id = 1;

        this.tvmName = findViewById(R.id.txttvmName);
        this.tvmID = findViewById(R.id.txttvmID);
        this.tvmClass = findViewById(R.id.txttvmClass);
        this.btnModule0 = findViewById(R.id.btnModule0);
        this.btnModule1 = findViewById(R.id.btnModule1);

        Module module = Module.InstanceOf(id);


        //Bottom stuff below invokes crashing, as a NullPointer Exception keeps being thrown.  Will monitor this in a bit -SC
        if(module != null) {
            Log.i("Module created", module.type.toString());
            Log.i("Module Name", module.name);
            Log.i("Module ID", String.format("%d", module.id));
        }
        if (module != null) {
            tvmName.setText(String.format("Name: %s", module.name));
            tvmID.setText(String.format("ID: %d", module.id));
            tvmClass.setText(String.format("Class: %s", module.type.toString()));
        }

        // #0 and #2 Modules are finished, see if we can get redirection over to those activities after hitting launch button
        btnModule0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = 0;
                tempModule = Module.InstanceOf(id);
                switchToModule(v);
            }
        });

        btnModule1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = 2;
                tempModule = Module.InstanceOf(id);
                switchToModule(v);
            }
        });
        // register for intent

    }

    public void switchToModule(View view) {
        Intent intent = new Intent(this, tempModule.type);
        startActivity(intent);
    }
}