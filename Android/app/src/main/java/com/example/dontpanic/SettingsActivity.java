// MS: 3/6/22 - initial code, plus setting sliders and switches to match database values
// MS: 3/10/22 - SeekBar listener pushes new value to the database
// MS: 3/20/22 - added more listeners, a back button, text scaling, and changed text size seek bar to be 1-2 rather than 0.5-2.0
// MS: 4/1/22 - added Default Sequence section with dynamic sequence name, text scaling, and button to select sequence
// MS: 4/7/22 - added switchToGuidedBreathingSettings
// MS: 4/8/22 - added Guided Breathing setting initialization and updates to/from the database, plus text scaling for new layout

package com.example.dontpanic;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity
{
    SeekBar masterVolume, hapticStrength, textScale;
    float textScaleRatio;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_settings);

        // Scale the text according to the user's preferences
        Object preference = Database.GetPreference(Preferences.TEXT_SCALING_FLOAT);
        if (preference == null)
        {
            Log.e("Database Error", "Preference returned null");
            textScaleRatio = 1.0f;
        }
        else
            textScaleRatio = (Float) preference;
        if (textScaleRatio != 1.0f)
            ScaleAppSettingsText();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.masterVolume = findViewById(R.id.VolumeBar);
        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            int index = masterVolume.getProgress();
            masterVolume.setProgress(index + 1);
            return true;
        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            int index = masterVolume.getProgress();
            masterVolume.setProgress(index - 1);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void switchToGeneralSettings(View view) {
        setContentView(R.layout.activity_settings_general);

        this.masterVolume = findViewById(R.id.VolumeBar);
        this.hapticStrength = findViewById(R.id.breathDurationBar);
        SwitchCompat hapticSwitch = findViewById(R.id.HapticSwitch);

        // Update the name of the sequence that's been chosen as the default
        TextView defSeqName = findViewById(R.id.defSeqName);
        int seqID;
        Object preference = Database.GetPreference(Preferences.LAUNCH_SEQUENCE_INT);
        if (preference == null)
        {
            Log.e("Database Error", "Preference returned null");
            seqID = -1;
        }
        else
            seqID = (Integer) preference;
        if (seqID != -1)
        {
            Sequence sequence = Database.GetSequence(seqID);
            if (sequence != null)
                defSeqName.setText(sequence.GetName());
        }

        if (textScaleRatio != 1.0f)
        {
            TextView volumeText = findViewById(R.id.volumeLabel);
            volumeText.setTextSize(TypedValue.COMPLEX_UNIT_PX, volumeText.getTextSize() * textScaleRatio);
            hapticSwitch.setTextSize(TypedValue.COMPLEX_UNIT_PX, hapticSwitch.getTextSize() * textScaleRatio);
            TextView hapticText = findViewById(R.id.breathDurationLabel);
            hapticText.setTextSize(TypedValue.COMPLEX_UNIT_PX, hapticText.getTextSize() * textScaleRatio);
            Button backButton = findViewById(R.id.backButtonSettings);
            backButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, backButton.getTextSize() * textScaleRatio);
            // Grow the Default Sequence header by half speed
            TextView defSeqHeader = findViewById(R.id.sequenceHeader);
            defSeqHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, defSeqHeader.getTextSize() * (textScaleRatio + ((textScaleRatio - 1) / 2)));
            TextView defSeqExplanation = findViewById(R.id.defSeqExplanation);
            defSeqExplanation.setTextSize(TypedValue.COMPLEX_UNIT_PX, defSeqExplanation.getTextSize() * textScaleRatio);
            TextView defSeqLabel = findViewById(R.id.currentLabel);
            defSeqLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, defSeqLabel.getTextSize() * textScaleRatio);
            defSeqName.setTextSize(TypedValue.COMPLEX_UNIT_PX, defSeqName.getTextSize() * textScaleRatio);
            Button selectButton = findViewById(R.id.selectButton);
            selectButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectButton.getTextSize() * textScaleRatio);
        }

        try {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            masterVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

            masterVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (b) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
                        /*
                        Please double check this if this is correct - SC
                        Feel free to delete these two comments when done

                        float val = (float) seekBar.getProgress() / (float) (seekBar.getMax() / 2);
                        Database.SetPreference(Preferences.AUDIO_VOLUME_FLOAT, val);
                        Update, alright, it works
                        default is 1.0, with range between 0.0 - 2.0
                         */
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    float val = (float) seekBar.getProgress() / (float) (seekBar.getMax() / 2);
                    Database.SetPreference(Preferences.AUDIO_VOLUME_FLOAT, val);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            hapticStrength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) { }

                @Override public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override public void onStopTrackingTouch(SeekBar seekBar) {
                    float val = (float) seekBar.getProgress() / (float) (seekBar.getMax() / 2);
                    Database.SetPreference(Preferences.HAPTIC_STRENGTH_FLOAT, val);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        preference = Database.GetPreference(Preferences.AUDIO_VOLUME_FLOAT);
        float val;
        if (preference == null)
        {
            Log.e("Database Error", "Could not retrieve preference");
            val = 1.0f;
        }
        else
            val = (Float) preference;
        this.masterVolume.setProgress((int) ((this.masterVolume.getMax() / 2) * val));

        preference = Database.GetPreference(Preferences.HAPTIC_STRENGTH_FLOAT);
        if (preference == null)
        {
            Log.e("Database Error", "Could not retrieve preference");
            val = 1.0f;
        }
        else
            val = (Float) preference;
        this.hapticStrength.setProgress((int) ((this.hapticStrength.getMax() / 2) * val));

        //********************************************
        // Set the status of the boolean preferences *
        //********************************************

        preference = Database.GetPreference(Preferences.HAPTICS_ENABLED_BOOLEAN);
        boolean on;
        if (preference == null)
        {
            Log.e("Database Error", "Could not retrieve preference");
            on = false;
        }
        else
            on = (boolean) preference;
        hapticSwitch.setChecked(on);
        hapticSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Database.SetPreference(Preferences.HAPTICS_ENABLED_BOOLEAN, isChecked);
            }
        });
    }

    public void switchToAccessibilitySettings(View view) {
        setContentView(R.layout.activity_settings_accessibility);
        this.textScale = findViewById(R.id.TextScaleBar);

        if (textScaleRatio != 1.0f)
            ScaleAccessibilitySettingsText();

        float val;
        Object preference = Database.GetPreference(Preferences.TEXT_SCALING_FLOAT);
        if (preference == null)
        {
            Log.e("Database Error", "Could not retrieve preference");
            val = 1.0f;
        }
        else
            val = (Float) preference;
        textScale.setProgress((int) Math.round((val - 1) * 10));
        try {
            textScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) { }

                @Override public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override public void onStopTrackingTouch(SeekBar seekBar) {
                    float val = 1 + ((float) seekBar.getProgress() / seekBar.getMax());
                    Database.SetPreference(Preferences.TEXT_SCALING_FLOAT, val);
                    textScaleRatio = val;
                    ScaleAccessibilitySettingsText();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchToAppSettings(View view) {
        setContentView(R.layout.activity_settings);
        ScaleAppSettingsText();
    }

    public void switchToGuidedBreathingSettings(View view) {
        setContentView(R.layout.activity_settings_guided_breathing);

        // Initialize the audio switch to its proper value and set a listener to update the database
        Object preference = Database.GetPreference(Preferences.BREATHING_AUDIO_ENABLED_BOOLEAN);
        boolean on;
        if (preference == null)
        {
            Log.e("Database Error", "Could not retrieve preference");
            on = false;
        }
        else
            on = (boolean) preference;
        SwitchCompat audioSwitch = findViewById(R.id.AudioSwitch);
        audioSwitch.setChecked(on);
        audioSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Database.SetPreference(Preferences.BREATHING_AUDIO_ENABLED_BOOLEAN, isChecked);
            }
        });

        // Initialize the haptic switch to its proper value and set a listener to update the database
        preference = Database.GetPreference(Preferences.BREATHING_HAPTICS_ENABLED_BOOLEAN);
        if (preference == null)
        {
            Log.e("Database Error", "Could not retrieve preference");
            on = false;
        }
        else
            on = (boolean) preference;
        SwitchCompat hapticSwitch = findViewById(R.id.HapticSwitch);
        hapticSwitch.setChecked(on);
        hapticSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Database.SetPreference(Preferences.BREATHING_HAPTICS_ENABLED_BOOLEAN, isChecked);
            }
        });

        // Initialize the breath duration seek bar to its proper value and set a listener to update the database
        preference = Database.GetPreference(Preferences.BREATHING_DURATION_FLOAT);
        float val;
        if (preference == null)
        {
            Log.e("Database Error", "Could not retrieve preference");
            val = Database.DEFAULT_BREATH_DURATION;
        }
        else
            val = (Float) preference;
        SeekBar breathDuration = findViewById(R.id.breathDurationBar);
        float midpoint = (float) Math.ceil(breathDuration.getMax() / 2.0f);
        float maxVariation = 2.0f;
        float step = maxVariation / (breathDuration.getMax() - midpoint);
        breathDuration.setProgress(Math.round((val - (Database.DEFAULT_BREATH_DURATION - maxVariation)) / step));
        try {
            breathDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) { }

                @Override public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override public void onStopTrackingTouch(SeekBar seekBar) {
                    // Create a float value that is plus or minus 2 seconds of the default breath duration
                    float val = Database.DEFAULT_BREATH_DURATION + (((float) seekBar.getProgress() - midpoint) * step);
                    Database.SetPreference(Preferences.BREATHING_DURATION_FLOAT, val);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Scale GUI elements if necessary
        if (textScaleRatio != 1.0f)
        {
            audioSwitch.setTextSize(TypedValue.COMPLEX_UNIT_PX, audioSwitch.getTextSize() * textScaleRatio);
            hapticSwitch.setTextSize(TypedValue.COMPLEX_UNIT_PX, hapticSwitch.getTextSize() * textScaleRatio);
            TextView breathLabel = findViewById(R.id.breathDurationLabel);
            breathLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, breathLabel.getTextSize() * textScaleRatio);
            Button backButton = findViewById(R.id.backButtonSettings);
            backButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, backButton.getTextSize() * textScaleRatio);
        }
    }

    ActivityResultLauncher<Intent> selectSequenceLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->
            {
                if (result.getResultCode() == RESULT_OK)
                {
                    Intent resultIntent = result.getData();
                    if (resultIntent != null)
                    {
                        int seqID = resultIntent.getIntExtra("seqID", -1);
                        if (seqID != -1)
                        {
                            Database.SetPreference(Preferences.LAUNCH_SEQUENCE_INT, seqID);
                            Sequence selectedSequence = Database.GetSequence(seqID);
                            if (selectedSequence != null)
                            {
                                TextView defSeqName = findViewById(R.id.defSeqName);
                                defSeqName.setText(selectedSequence.GetName());
                            }
                        }
                    }
                }
            }
    );

    public void onSelectSequence(View view) {
        Intent selectSequenceIntent = new Intent(this, SequenceSelectionActivity.class);
        selectSequenceLauncher.launch(selectSequenceIntent);
    }

    public void onBack(View view)
    {
        finish();
    }

    private void ScaleAppSettingsText()
    {
        Button generalButton = findViewById(R.id.generalSettingsButton);
        generalButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, generalButton.getTextSize() * textScaleRatio);
        Button accessibilityButton = findViewById(R.id.accessibilitySettingsButton);
        accessibilityButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, accessibilityButton.getTextSize() * textScaleRatio);
        Button guidedBreathingButton = findViewById(R.id.guidedBreathingSettingsButton);
        guidedBreathingButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, guidedBreathingButton.getTextSize() * textScaleRatio);
        Button backButton = findViewById(R.id.backButton);
        backButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, backButton.getTextSize() * textScaleRatio);
        if (textScaleRatio > 1.6f)
        {
            // Set the padding in px to be 10dp
            int padding = (int) (10 * getResources().getDisplayMetrics().density);
            backButton.setPadding(padding, padding, padding, padding);
        }
    }

    private void ScaleAccessibilitySettingsText()
    {
        TextView scaleText = findViewById(R.id.textSizeLabel);
        scaleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, 55 * textScaleRatio);
        Button backButton = findViewById(R.id.backButtonSettings);
        backButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, 55 * textScaleRatio);
    }
}