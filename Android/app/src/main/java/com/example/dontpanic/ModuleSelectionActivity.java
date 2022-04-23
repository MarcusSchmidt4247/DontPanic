package com.example.dontpanic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ModuleSelectionActivity extends AppCompatActivity {

    private Button btnModule0, btnModule1, btnModule2, btnModule3, btnModule4, btnFinish;
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
        
        this.btnModule0 = findViewById(R.id.btnModule0);
        this.btnModule1 = findViewById(R.id.btnModule1);
        this.btnModule2 = findViewById(R.id.btnModule2);
        this.btnModule3 = findViewById(R.id.btnModule3);
        this.btnModule4 = findViewById(R.id.btnModule4);
        this.btnFinish = findViewById(R.id.btnFinish);

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

        btnModule2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database.AppendModuleToSequence(sequenceID, 2);
            }
        });

        btnModule3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database.AppendModuleToSequence(sequenceID, 3);
            }
        });

        btnModule4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database.AppendModuleToSequence(sequenceID, 4);
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack(view);
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
