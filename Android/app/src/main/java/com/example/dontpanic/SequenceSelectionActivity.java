// MS: 4/1/22 - basic activity functionality

package com.example.dontpanic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SequenceSelectionActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence_selection);

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
            // Set the cancel button
            AppCompatButton cancelButton = findViewById(R.id.cancelButton);
            cancelButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, cancelButton.getTextSize() * textScale);
            if (textScale > 1.6f)
            {
                // Set the padding in px to be 10dp
                int padding = (int) (10 * getResources().getDisplayMetrics().density);
                cancelButton.setPadding(padding, padding, padding, padding);
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
                inflatedLayout = inflater.inflate(R.layout.sequence_selection_layout, sequencesLayout, false);

                // Retrieve the sequence information that this layout will correspond to
                Sequence sequence = Database.GetSequence(seqID);
                if (sequence != null)
                {
                    // Set each button's onClickListener to pass along the correct sequence ID to its corresponding event function
                    inflatedLayout.findViewById(R.id.selectSequenceButton).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            onSelect(sequence);
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
                        AppCompatButton select = seqLayout.findViewById(R.id.selectSequenceButton);
                        select.setTextSize(TypedValue.COMPLEX_UNIT_PX, select.getTextSize() * textScale);
                    }
                }
                else
                    Log.e("Sequence Error", "SequenceSelectionActivity could not retrieve sequence data to inflate layout");
            }
        }
    }

    private void onSelect(Sequence sequence)
    {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("seqID", sequence.GetID());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void onCancel(View view)
    {
        setResult(RESULT_CANCELED);
        finish();
    }
}