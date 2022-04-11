// MS: 4/7/22 - added back button function
// MS: 4/11/22 - added functionality to recommended modules and sequences

package com.example.dontpanic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
            Intent intent = new Intent(this, recommendedModule.getClass());
            startActivity(intent);
        }
    }

    public void onLaunchSequence(View view) {
        //***********************************************
        // To-do: Replace with proper sequence launcher *
        //***********************************************
        if (recommendedSequence != null && !recommendedSequence.GetModules().isEmpty())
        {
            Intent intent = new Intent(this, recommendedSequence.GetModules().get(0).getClass());
            startActivity(intent);
        }
    }

    public void onBack(View view) {
        finish();
    }
}
