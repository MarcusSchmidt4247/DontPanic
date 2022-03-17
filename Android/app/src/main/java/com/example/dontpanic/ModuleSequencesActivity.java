// MS: 3/17/22 - initial code with first GUI iteration and basic LayoutInflater

package com.example.dontpanic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ModuleSequencesActivity extends AppCompatActivity
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
                            onLaunch(seqID);
                        }
                    });
                    inflatedLayout.findViewById(R.id.editSequenceButton).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            onEdit(seqID);
                        }
                    });
                    inflatedLayout.findViewById(R.id.deleteSequenceButton).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            onDelete(seqID);
                        }
                    });

                    // Customize the layout's IDs and text to be unique to this sequence
                    TextView sequenceName = inflatedLayout.findViewById(R.id.sequenceName);
                    sequenceName.setText(sequence.GetName());
                    sequencesLayout.addView(inflatedLayout);
                }
                else
                    Log.e("Sequence Error", "ModuleSequencesActivity could not retrieve sequence data to inflate layout");
            }
        }
    }

    public void onLaunch(int seqID)
    {
        Log.i("onLaunch", "Called by " + seqID);
    }

    public void onEdit(int seqID)
    {
        Log.i("onEdit", "Called by " + seqID);
    }

    public void onDelete(int seqID)
    {
        Log.i("onDelete", "Called by " + seqID);
    }
}