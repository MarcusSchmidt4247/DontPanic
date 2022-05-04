package com.example.dontpanic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ModuleSelectionActivity extends AppCompatActivity {

    private Button btnModule0, btnModule1, btnModule2, btnModule3, btnModule4, btnFinish;
    private int sequenceID;
    private EditText sqNameEdit;
    private TextView txtList0, txtList1, txtList2, txtList3, txtList4;
    private ArrayList<Integer> sequenceModuleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);
        //Intent intent = getIntent();                                      This is for editing a sequence
        //this.sequence = intent.getParcelableExtra("sequence");

        //we need an incrementer for this
        //sequence = new Sequence(1, "Test Sequence", new ArrayList<Module>());
        int seqID = Database.GetUserSequenceIDs().size() + 1;
        boolean[] inList = {false, false, false, false, false};

        this.btnModule0 = findViewById(R.id.btnModule0);
        this.btnModule1 = findViewById(R.id.btnModule1);
        this.btnModule2 = findViewById(R.id.btnModule2);
        this.btnModule3 = findViewById(R.id.btnModule3);
        this.btnModule4 = findViewById(R.id.btnModule4);
        this.btnFinish = findViewById(R.id.btnFinish);
        this.sqNameEdit = findViewById(R.id.sqNameEdit);
        this.txtList0 = findViewById(R.id.txtList0);
        this.txtList1 = findViewById(R.id.txtList1);
        this.txtList2 = findViewById(R.id.txtList2);
        this.txtList3 = findViewById(R.id.txtList3);
        this.txtList4 = findViewById(R.id.txtList4);

        btnModule0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inList[0] == false) {
                    inList[0] = true;
                    sequenceModuleList.add(0);
                    Log.i("Check", String.valueOf(sequenceModuleList));
                    update();
                } else {
                    inList[0] = false;
                    int index = sequenceModuleList.indexOf(0);
                    if (index != -1) sequenceModuleList.remove(index);
                    Log.i("Check", String.valueOf(sequenceModuleList));
                    txtList0.setText("");
                    update();
                }
                //Database.AppendModuleToSequence(sequenceID, 0);
            }
        });

        btnModule1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inList[1] == false) {
                    inList[1] = true;
                    sequenceModuleList.add(1);
                    Log.i("Check", String.valueOf(sequenceModuleList));
                    update();
                } else {
                    inList[1] = false;
                    int index = sequenceModuleList.indexOf(1);
                    if (index != -1) sequenceModuleList.remove(index);
                    Log.i("Check", String.valueOf(sequenceModuleList));
                    txtList1.setText("");
                    update();
                }
                //Database.AppendModuleToSequence(sequenceID, 1);
            }
        });

        btnModule2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inList[2] == false) {
                    inList[2] = true;
                    sequenceModuleList.add(2);
                    Log.i("Check", String.valueOf(sequenceModuleList));
                    update();
                } else {
                    inList[2] = false;
                    int index = sequenceModuleList.indexOf(2);
                    if (index != -1) sequenceModuleList.remove(index);
                    Log.i("Check", String.valueOf(sequenceModuleList));
                    txtList2.setText("");
                    update();
                }
                //Database.AppendModuleToSequence(sequenceID, 2);
            }
        });

        btnModule3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inList[3] == false) {
                    inList[3] = true;
                    sequenceModuleList.add(3);
                    Log.i("Check", String.valueOf(sequenceModuleList));
                    update();
                } else {
                    inList[3] = false;
                    int index = sequenceModuleList.indexOf(3);
                    if (index != -1) sequenceModuleList.remove(index);
                    Log.i("Check", String.valueOf(sequenceModuleList));
                    txtList3.setText("");
                    update();
                }
                //Database.AppendModuleToSequence(sequenceID, 3);
            }
        });

        btnModule4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inList[4] == false) {
                    inList[4] = true;
                    sequenceModuleList.add(4);
                    Log.i("Check", String.valueOf(sequenceModuleList));
                    update();
                } else {
                    inList[4] = false;
                    int index = sequenceModuleList.indexOf(4);
                    if (index != -1) sequenceModuleList.remove(index);
                    Log.i("Check", String.valueOf(sequenceModuleList));
                    txtList4.setText("");
                    update();
                }
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

    public void update() {
        String tmpText = "";
        if (sequenceModuleList.contains(0)) {
            tmpText = new StringBuilder().append((sequenceModuleList.indexOf(0)) + 1).toString();
            txtList0.setText(tmpText);
        }
        if (sequenceModuleList.contains(1)) {
            tmpText = new StringBuilder().append((sequenceModuleList.indexOf(1)) + 1).toString();
            txtList1.setText(tmpText);
        }
        if (sequenceModuleList.contains(2)) {
            tmpText = new StringBuilder().append((sequenceModuleList.indexOf(2)) + 1).toString();
            txtList2.setText(tmpText);
        }
        if (sequenceModuleList.contains(3)) {
            tmpText = new StringBuilder().append((sequenceModuleList.indexOf(3)) + 1).toString();
            txtList3.setText(tmpText);
        }
        if (sequenceModuleList.contains(4)) {
            tmpText = new StringBuilder().append((sequenceModuleList.indexOf(4)) + 1).toString();
            txtList4.setText(tmpText);
        }
    }
}
