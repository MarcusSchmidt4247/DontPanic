package com.example.dontpanic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ModuleSelectionActivity extends AppCompatActivity {

    private Button btnModule0, btnModule1, btnModule2, btnModule3, btnModule4, btnFinish;
    private int sequenceID;
    private EditText sqNameEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);
        //Intent intent = getIntent();                                      This is for editing a sequence
        //this.sequence = intent.getParcelableExtra("sequence");

        //we need an incrementer for this
        //sequence = new Sequence(1, "Test Sequence", new ArrayList<Module>());
        int seqID = Database.GetUserSequenceIDs().size() + 1;
        ArrayList<Integer> sequenceModuleList = new ArrayList<>();
        
        this.btnModule0 = findViewById(R.id.btnModule0);
        this.btnModule1 = findViewById(R.id.btnModule1);
        this.btnModule2 = findViewById(R.id.btnModule2);
        this.btnModule3 = findViewById(R.id.btnModule3);
        this.btnModule4 = findViewById(R.id.btnModule4);
        this.btnFinish = findViewById(R.id.btnFinish);
        this.sqNameEdit = findViewById(R.id.sqNameEdit);

        btnModule0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sequenceModuleList.add(0);
                //Database.AppendModuleToSequence(sequenceID, 0);
            }
        });

        btnModule1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sequenceModuleList.add(1);
                //Database.AppendModuleToSequence(sequenceID, 1);
            }
        });

        btnModule2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sequenceModuleList.add(2);
                //Database.AppendModuleToSequence(sequenceID, 2);
            }
        });

        btnModule3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sequenceModuleList.add(3);
                //Database.AppendModuleToSequence(sequenceID, 3);
            }
        });

        btnModule4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sequenceModuleList.add(4);
                //Database.AppendModuleToSequence(sequenceID, 4);
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sequenceModuleList.isEmpty())
                    onBack(view);

                String sequence_name;

                if (TextUtils.isEmpty(sqNameEdit.getText().toString())) {
                    sequence_name = "Sequence " + seqID;
                } else {
                    sequence_name = sqNameEdit.getText().toString();
                }
                sequenceID = Database.CreateSequence(Database.currentUserID, sequence_name);

                for (int i = sequenceModuleList.size()-1; i >= 0; i--) {
                    Database.AppendModuleToSequence(sequenceID, sequenceModuleList.get(i));
                }
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
