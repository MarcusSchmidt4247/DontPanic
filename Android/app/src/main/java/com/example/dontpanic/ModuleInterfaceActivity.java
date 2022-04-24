// MS: 4/7/22 - added back button function
// MS: 4/11/22 - added functionality to recommended modules and sequences
// MS: 4/18/22 - added text scaling
// MS: 4/24/22 - moved module and sequence recommendations to onStart() so that they update properly

package com.example.dontpanic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class ModuleInterfaceActivity extends AppCompatActivity {
    private Module recommendedModule;
    private Sequence recommendedSequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_interface);

        // Scale text
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
            float realScale = Math.min(textScale, 1.7f);
            TextView moduleLabel = findViewById(R.id.lastModuleStatic);
            moduleLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, moduleLabel.getTextSize() * realScale);
            TextView recommendedModuleLabel = findViewById(R.id.lastModuleDynamic);
            recommendedModuleLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, recommendedModuleLabel.getTextSize() * realScale);
            Button launchModule = findViewById(R.id.moduleButton2);
            launchModule.setTextSize(TypedValue.COMPLEX_UNIT_PX, launchModule.getTextSize() * realScale);
            Button allModules = findViewById(R.id.moduleButton3);
            allModules.setTextSize(TypedValue.COMPLEX_UNIT_PX, allModules.getTextSize() * realScale);
            TextView sequenceLabel = findViewById(R.id.lastSequenceStatic);
            sequenceLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, sequenceLabel.getTextSize() * realScale);
            TextView recommendedSequenceLabel = findViewById(R.id.lastSequenceDynamic);
            recommendedSequenceLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, recommendedSequenceLabel.getTextSize() * realScale);
            Button launchSequence = findViewById(R.id.moduleButton5);
            launchSequence.setTextSize(TypedValue.COMPLEX_UNIT_PX, launchSequence.getTextSize() * realScale);
            Button allSequences = findViewById(R.id.moduleButton8);
            allSequences.setTextSize(TypedValue.COMPLEX_UNIT_PX, allSequences.getTextSize() * realScale);
            Button back = findViewById(R.id.backToGenButton);
            back.setTextSize(TypedValue.COMPLEX_UNIT_PX, back.getTextSize() * realScale);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        // Recommend the last completed module
        recommendedModule = Database.LastCompletedModule();
        // If the user has not completed any, recommend a random module
        Random random = new Random();
        if (recommendedModule == null)
            recommendedModule = Module.InstanceOf(random.nextInt(5));
        // Update the text on screen
        TextView recommendedModuleLabel = findViewById(R.id.lastModuleDynamic);
        if (recommendedModule != null)
            recommendedModuleLabel.setText(recommendedModule.name);
        else
            recommendedModuleLabel.setText(R.string.noRecommendation);

        // Recommend a random sequence (if the user has any)
        recommendedSequence = null;
        ArrayList<Integer> sequences = Database.GetUserSequenceIDs();
        TextView recommendedSequenceLabel = findViewById(R.id.lastSequenceDynamic);
        if (sequences == null || sequences.isEmpty())
            recommendedSequenceLabel.setText(R.string.noRecommendation);
        else
        {
            int seqID = sequences.get(random.nextInt(sequences.size()));
            recommendedSequence = Database.GetSequence(seqID);
            if (recommendedSequence != null)
                recommendedSequenceLabel.setText(recommendedSequence.GetName());
        }
    }

    public void switchToModulesActivity(View view) {
        Intent intent = new Intent(this, ModulesActivity.class);
        startActivity(intent);
    }
    public void switchToModuleSequencesActivity(View view) {
        Intent intent = new Intent(this, ModuleSequencesActivity.class);
        startActivity(intent);
    }

    public void onLaunchModule(View view) {
        if (recommendedModule != null)
        {
            Intent intent = new Intent(this, recommendedModule.GetClass());
            startActivity(intent);
        }
    }

    public void onLaunchSequence(View view) {
        //***********************************************
        // To-do: Replace with proper sequence launcher *
        //***********************************************
        if (recommendedSequence != null && !recommendedSequence.GetModules().isEmpty())
        {
            Intent intent = new Intent(this, recommendedSequence.GetModules().get(0).GetClass());
            startActivity(intent);
        }
    }

    public void onBack(View view) {
        finish();
    }
}
