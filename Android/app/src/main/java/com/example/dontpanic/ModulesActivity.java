package com.example.dontpanic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ModulesActivity extends AppCompatActivity {
    private int id;
    private Button btnModule0, btnModule1, btnModule2, btnModule3, btnModule4, btnFinish;
    private Module tempModule;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);

        this.btnModule0 = findViewById(R.id.btnModule0);
        this.btnModule1 = findViewById(R.id.btnModule1);
        this.btnModule2 = findViewById(R.id.btnModule2);
        this.btnModule3 = findViewById(R.id.btnModule3);
        this.btnModule4 = findViewById(R.id.btnModule4);
        this.btnFinish = findViewById(R.id.btnFinish);
        this.editText = findViewById(R.id.sqNameEdit);
        editText.setVisibility(View.GONE);

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
                id = 1;
                tempModule = Module.InstanceOf(id);
                switchToModule(v);
            }
        });

        btnModule2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = 2;
                tempModule = Module.InstanceOf(id);
                switchToModule(v);
            }
        });

        btnModule3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = 3;
                tempModule = Module.InstanceOf(id);
                switchToModule(v);
            }
        });

        btnModule4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = 4;
                tempModule = Module.InstanceOf(id);
                switchToModule(v);
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void switchToModule(View view) {
        Intent intent = new Intent(this, tempModule.type);
        startActivity(intent);
    }
}