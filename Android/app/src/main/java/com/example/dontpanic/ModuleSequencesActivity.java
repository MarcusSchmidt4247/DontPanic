// MS: 3/17/22 - initial code with first GUI iteration and basic LayoutInflater
// MS: 3/18/22 - onLaunch() starts first module in sequence, onDelete() launches confirmation popup
// MS: 3/20/22 - added onBack() and divided functions into categories

package com.example.dontpanic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ModuleSequencesActivity extends AppCompatActivity implements ConfirmationDialog.ConfirmationDialogListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_sequences);

        ArrayList<Integer> sequences = Database.GetUserSequenceIDs();
        if (sequences != null && !sequences.isEmpty())
        {
            // Remove the "no sequences" text because at least one sequence was found
            LinearLayout sequencesLayout = (LinearLayout) findViewById(R.id.mySequencesLayout);
            sequencesLayout.removeView(findViewById(R.id.noSequencesText));

            // Then, for each sequence found, inflate the sequence layout with the proper UI
            // Source: https://stackoverflow.com/questions/3477422/what-does-layoutinflater-in-android-do
            View inflatedLayout;
            LayoutInflater inflater = getLayoutInflater();
            for (Integer seqID : sequences)
            {
                inflatedLayout = inflater.inflate(R.layout.sequence_layout, sequencesLayout, false);

                // Retrieve the sequence information that this layout will correspond to
                Sequence sequence = Database.GetSequence(seqID);
                if (sequence != null)
                {
                    // Set each button's onClickListener to pass along the correct sequence ID to its corresponding event function
                    inflatedLayout.findViewById(R.id.launchSequenceButton).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            onLaunch(sequence);
                        }
                    });
                    inflatedLayout.findViewById(R.id.editSequenceButton).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            onEdit(sequence);
                        }
                    });
                    inflatedLayout.findViewById(R.id.deleteSequenceButton).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            onDelete(sequence);
                        }
                    });

                    // Customize the layout's IDs and text to be unique to this sequence
                    inflatedLayout.setId(100 + seqID);
                    TextView sequenceName = inflatedLayout.findViewById(R.id.sequenceName);
                    sequenceName.setText(sequence.GetName());
                    sequencesLayout.addView(inflatedLayout);
                }
                else
                    Log.e("Sequence Error", "ModuleSequencesActivity could not retrieve sequence data to inflate layout");
            }
        }
    }

    //*************************
    // Button press functions *
    //*************************

    public void onLaunch(Sequence sequence)
    {
        //**************************************************************************************************************************
        // Needs to be replaced / augmented with a back-end control scheme for the sequence that goes from one module to the next. *
        // This is just a very simply intent to launch whatever the first module in the sequence is, and then it's done.           *
        //
        // Edit by Stephen, original: sequence.GetModules().get(0).GetClass(Module.GetID())
        //**************************************************************************************************************************
        if (!sequence.GetModules().isEmpty())
        {
            ModuleReference firstModule = sequence.GetModules().get(0);
            Intent moduleIntent = new Intent(this, firstModule.getType());
            startActivity(moduleIntent);
            finish();
        }
    }

    public void onEdit(Sequence sequence)
    {
        Log.i("onEdit", "Called by " + sequence.GetID());
    }

    public void onDelete(Sequence sequence)
    {
        ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance("Delete " + sequence.GetName() + "?", sequence.GetID());
        confirmationDialog.show(getSupportFragmentManager(), "ConfirmationDialogFragment");
    }

    public void onBack(View view)
    {
        finish();
    }

    //********************************
    // Confirmation dialog functions *
    //********************************

    @Override
    public void onDialogPositiveClick(DialogFragment dialog)
    {
        int seqID = -1;
        if (dialog.getArguments() != null)
            seqID = dialog.getArguments().getInt(ConfirmationDialog.EXTRA_INT_KEY);

        if (Database.DeleteSequence(Database.currentUserID, seqID))
            ((LinearLayout) findViewById(R.id.mySequencesLayout)).removeView(findViewById(100 + seqID));
        else
            Log.e("onDialogPositiveClick", "Failed to delete sequence");
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog)
    {
        Log.i("onDialogNegativeClick", "User cancelled delete");
    }
}