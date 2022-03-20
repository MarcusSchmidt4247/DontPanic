// MS: 3/17/22 - initial code with first GUI iteration and basic LayoutInflater
// MS: 3/18/22 - onLaunch() starts first module in sequence, onDelete() launches confirmation popup
// MS: 3/20/22 - added onBack(), divided functions into categories, and added text scaling

package com.example.dontpanic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.TypedValue;
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

        // Scale the text according to the user's preferences
        float textScale;
        Object preference = Database.GetPreference(Preferences.TEXT_SCALING_FLOAT);
        if (preference == null)
        {
            Log.e("Database Error", "Preference returned null");
            textScale = 1.0f;
        }
        else
            textScale = (Float) preference;
        if (textScale != 1.0f)
        {
            // Cap the size of text scaling a *little* bit lower than 2.0 to keep the UI usable without a ton of work
            if (textScale > 1.8f)
                textScale = 1.8f;

            // Set the header ("My Sequences:")
            TextView headerText = findViewById(R.id.mySequencesHeader);
            headerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, headerText.getTextSize() * textScale);
            // Set the no new sequences text
            TextView noSequencesText = findViewById(R.id.noSequencesText);
            noSequencesText.setTextSize(TypedValue.COMPLEX_UNIT_PX, noSequencesText.getTextSize() * textScale);
            // Set the Create Sequence button
            AppCompatButton createSequenceButton = findViewById(R.id.createSequenceButton);
            createSequenceButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, createSequenceButton.getTextSize() * textScale);
            // Set the back button
            AppCompatButton backButton = findViewById(R.id.backButton);
            backButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, backButton.getTextSize() * textScale);
            if (textScale > 1.6f)
            {
                // Set the padding in px to be 10dp
                int padding = (int) (10 * getResources().getDisplayMetrics().density);
                backButton.setPadding(padding, padding, padding, padding);
            }
        }

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

                    // Finally, scale the text inside this layout
                    if (textScale != 1.0f)
                    {
                        TextView nameText = findViewById(R.id.sequenceName);
                        nameText.setTextSize(TypedValue.COMPLEX_UNIT_PX, nameText.getTextSize() * textScale);
                        AppCompatButton launch = findViewById(R.id.launchSequenceButton);
                        launch.setTextSize(TypedValue.COMPLEX_UNIT_PX, launch.getTextSize() * textScale);
                        AppCompatButton edit = findViewById(R.id.editSequenceButton);
                        edit.setTextSize(TypedValue.COMPLEX_UNIT_PX, edit.getTextSize() * textScale);
                        AppCompatButton delete = findViewById(R.id.deleteSequenceButton);
                        delete.setTextSize(TypedValue.COMPLEX_UNIT_PX, delete.getTextSize() * textScale);
                    }
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