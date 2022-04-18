package com.example.dontpanic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;

public class ModuleSelectionActivity extends AppCompatActivity {

    private TextView tvmName, tvmID, tvmClass;
    private Button btnModule0, btnModule1;
    private int sequenceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);
        //Intent intent = getIntent();                                      This is for editing a sequence
        //this.sequence = intent.getParcelableExtra("sequence");

        //we need an incrementer for this
        //sequence = new Sequence(1, "Test Sequence", new ArrayList<Module>());
        sequenceID = Database.CreateSequence(Database.currentUserID, "Test Sequence");
        
        this.tvmClass = findViewById(R.id.txttvmClass);
        this.btnModule0 = findViewById(R.id.btnModule0);
        this.btnModule1 = findViewById(R.id.btnModule1);

        btnModule0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database.AppendModuleToSequence(sequenceID, 0);
            }
        });

        btnModule1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database.AppendModuleToSequence(sequenceID, 1);
            }
        });

    }

    public static Intent createIntent(Context context, Sequence sequenceToEdit) {
        Intent intent = new Intent(context, ModuleSelectionActivity.class);
        intent.putExtra("sequence", (Parcelable) sequenceToEdit);
        return intent;
    }

    public void onBack(View view) {
        finish();
    }
}
