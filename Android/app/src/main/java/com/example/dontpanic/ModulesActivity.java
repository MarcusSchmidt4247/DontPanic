package com.example.dontpanic;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ModulesActivity extends AppCompatActivity {
    private static final Module[] modulesList = Module.values();
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);
        this.id = 1;
        ModuleReference tempModule = new ModuleReference(Module.GetName(this.id), this.id, Module.GetClass(this.id));
        //set onClicker to listen to which button gets clicked (?)
        System.out.println("Name: " + tempModule.getName() + "\nID: " + tempModule.getName() + "\nClass: " + tempModule.getType().toString());
    }
}