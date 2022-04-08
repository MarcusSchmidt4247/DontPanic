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

public class ModuleSelectionActivity extends AppCompatActivity {

    private TextView tvmName, tvmID, tvmClass;
    private Button btnModule0, btnModule1;
    private Sequence sequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);
        //Intent intent = getIntent();                                      This is for editing a sequence
        //this.sequence = intent.getParcelableExtra("sequence");


        sequence = new Sequence(1, "Test Sequence", new ArrayList<Module>());

        this.tvmName = findViewById(R.id.txttvmName);
        this.tvmID = findViewById(R.id.txttvmID);
        this.tvmClass = findViewById(R.id.txttvmClass);
        this.btnModule0 = findViewById(R.id.btnModule0);
        this.btnModule1 = findViewById(R.id.btnModule1);

        tvmName.setText(String.format("Name: %s", sequence.GetName()));
        tvmID.setText(String.format("ID: %d", sequence.GetID()));
        printSequence();

        btnModule0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Module tempModule = Module.InstanceOf(0);
                sequence.addModule(tempModule);
                printSequence();
            }
        });

        btnModule1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Module tempModule = Module.InstanceOf(1);
                sequence.addModule(tempModule);
                printSequence();
            }
        });

    }
    public void printSequence() {
        System.out.printf("\n\nSequence ID and Name: %d %s\n", sequence.GetID(), sequence.GetName());
        for(int i = 0; i < sequence.GetModules().size(); i++) {
            Log.i("Sequence", "Sequence " + i + " " + sequence.GetModules().get(i).toString());
        }
    }

    public void goBackToModuleSelection(View view) {
        Intent intent = new Intent(this, ModuleSelectionActivity.class);
        startActivity(intent);
    }

    public static void startNewActivity() {
        //Iterate over every activity in sequence

        //Finish and go back to Module Selection screen (?)
    }

    public static Intent createIntent(Context context, Sequence sequenceToEdit) {
        Intent intent = new Intent(context, ModuleSelectionActivity.class);
        intent.putExtra("sequence", (Parcelable) sequenceToEdit);
        return intent;
    }

    public void onBack(View view) {
        sequence.GetModules().clear();          //This is temporary.  either whenever the user presses onBack, I feel that we should still save this sequence as a new sequence, just in case. - SC
        //Need to save sequence to DB - SC
        finish();
    }
}
