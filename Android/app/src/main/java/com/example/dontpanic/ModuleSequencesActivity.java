// MS: 3/17/22 - initial code with first GUI iteration and basic LayoutInflater
// MS: 3/18/22 - onLaunch() starts first module in sequence, onDelete() launches confirmation popup
// MS: 3/20/22 - added onBack(), divided functions into categories, and added text scaling
// MS: 4/1/22 - fixed text scaling error
// MS: 4/24/22 - updates list of sequences every time the screen is returned to without being completely finished first

package com.example.dontpanic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

public class ModuleSequencesActivity extends AppCompatActivity implements ConfirmationDialog.ConfirmationDialogListener
{
    private float textScale = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_sequences);

        // Scale the text according to the user's preferences
        Object preference = Database.GetPreference(Preferences.TEXT_SCALING_FLOAT);
        if (preference == null)
        {
            Log.e("Database Error", "Preference returned null");
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
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        // Add the GUI for each sequence that this user has created
        ArrayList<Integer> sequences = Database.GetUserSequenceIDs();
        if (sequences != null && !sequences.isEmpty())
        {
            // Remove the "no sequences" (if it still exists) text because at least one sequence was found
            LinearLayout sequencesLayout = findViewById(R.id.mySequencesLayout);
            TextView noSequencesText = findViewById(R.id.noSequencesText);
            if (noSequencesText != null)
                sequencesLayout.removeView(noSequencesText);

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
                        ConstraintLayout seqLayout = findViewById(inflatedLayout.getId());
                        TextView nameText = seqLayout.findViewById(R.id.sequenceName);
                        nameText.setTextSize(TypedValue.COMPLEX_UNIT_PX, nameText.getTextSize() * textScale);
                        AppCompatButton launch = seqLayout.findViewById(R.id.launchSequenceButton);
                        launch.setTextSize(TypedValue.COMPLEX_UNIT_PX, launch.getTextSize() * textScale);
                        AppCompatButton edit = seqLayout.findViewById(R.id.editSequenceButton);
                        edit.setTextSize(TypedValue.COMPLEX_UNIT_PX, edit.getTextSize() * textScale);
                        AppCompatButton delete = seqLayout.findViewById(R.id.deleteSequenceButton);
                        delete.setTextSize(TypedValue.COMPLEX_UNIT_PX, delete.getTextSize() * textScale);
                    }
                }
                else
                    Log.e("Sequence Error", "ModuleSequencesActivity could not retrieve sequence data to inflate layout");
            }
        }
    }

    @Override
    protected void onStop()
    {
        // Clear the GUI for all the sequences so they can be updated and recreated when the activity is started again
        LinearLayout sequencesLayout = findViewById(R.id.mySequencesLayout);
        sequencesLayout.removeAllViews();

        super.onStop();
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
            //Write an iterator here, to iterate over the whole module of sequences - SC
            // ModuleReference firstModule = sequence.GetModules().get(0);      deprecated, no need for Module Reference og: this, firstModule.getType() down below

            // Issue with launching a sequence and crashes the app
            ArrayList<Module> seqList = sequence.GetModules();
            Iterator<Module> iter = seqList.iterator();
            while(iter.hasNext()) {
                Module temp = iter.next();
                Intent moduleIntent = new Intent(this, temp.GetClass());
                startActivity(moduleIntent);
                // Do we call finish inside all of our Modules - SC, as we want them to be off of the Activity stack once they are done occurring
                // The finish down below will remove ModuleSequencesActivity off of the Activity stack, so I dunno if we would want that necessarily, though would make sense since we are importing all Sequences from the DB each time
                finish();
            }
        }
    }

    public void onEdit(Sequence sequence)
    {
        Log.i("onEdit", "Called by " + sequence.GetID());

        Intent intent = ModuleSelectionActivity.createIntent(this, sequence);
        startActivity(intent);

        //WIP, and is optional
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

    public void createSequence(View view) {
        Intent intent = new Intent(this, ModuleSelectionActivity.class);
        startActivity(intent);
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