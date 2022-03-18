// MS: 2/27/22 - initial code
// MS: 2/28/22 - added very basic haptic effect
// MS: 3/4/22 - added audio instructions, text scaling, and basic lifecycle persistence
// MS: 3/18/22 - fixed error where the maximum haptic amplitude exceeded 255

package com.example.dontpanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class GuidedBreathing extends AppCompatActivity
{
    // All user preferences that relate to this module
    private boolean haptics;
    private float hapticStrength;
    private boolean audio;
    private float volume;

    private MediaPlayer audioInstruction;
    private boolean breatheIn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guided_breathing);

        // Retrieve and set the user's preference for how long each breath will last
        Object preference = Database.GetPreference(Preferences.BREATHING_DURATION_FLOAT);
        float breathDuration;
        if (preference == null)
        {
            // Error handling
            Log.e("Database Error", "Preference returned null");
            breathDuration = 6.0f;
        }
        else
            breathDuration = (Float) preference;

        // Retrieve and set the user's preference for haptics being on or off
        preference = Database.GetPreference(Preferences.HAPTICS_ENABLED_BOOLEAN);
        if (preference == null)
        {
            // Error handling
            Log.e("Database Error", "Preference returned null");
            haptics = false;
        }
        else
            haptics = (Boolean) preference;

        // Retrieve and set the user's preference for haptic strength in case it will be used
        preference = Database.GetPreference(Preferences.HAPTIC_STRENGTH_FLOAT);
        if (preference == null)
        {
            // Error handling
            Log.e("Database Error", "Preference returned null");
            hapticStrength = -1;
        }
        else
            hapticStrength = (Float) preference;

        // Retrieve and set the user's preference for whether audio should play
        preference = Database.GetPreference(Preferences.BREATHING_AUDIO_ENABLED_BOOLEAN);
        if (preference == null)
        {
            // Error handling
            Log.e("Database Error", "Preference returned null");
            audio = false;
        }
        else
            audio = (Boolean) preference;

        preference = Database.GetPreference(Preferences.AUDIO_VOLUME_FLOAT);
        if (preference == null)
        {
            Log.e("Database Error", "Preference returned null");
            volume = 1.0f;
        }
        else
            volume = (Float) preference;

        float textScale;
        preference = Database.GetPreference(Preferences.TEXT_SCALING_FLOAT);
        if (preference == null)
        {
            Log.e("Database Error", "Preference returned null");
            textScale = 1.0f;
        }
        else
            textScale = (Float) preference;

        // Scale the text on the screen according to the user's preference
        TextView textView = (TextView) findViewById(R.id.breathInstruction);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textView.getTextSize() * textScale);

        // Also for the button
        Button button = (Button) findViewById(R.id.back_button);
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, button.getTextSize() * textScale);

        // Prepare the breathing animation
        Animation breathAnim = AnimationUtils.loadAnimation(this, R.anim.breath_anim);
        // Convert the breath duration from seconds to milliseconds
        breathAnim.setDuration((long) breathDuration * 1000);
        // Initialize it to the correct text
        textView.setText(R.string.breatheInstructionIn);
        // Set up the handler for events during the animation
        breathAnim.setAnimationListener(new Animation.AnimationListener()
        {
            // Whenever the animation repeats, toggle the state and text
            @Override
            public void onAnimationRepeat(Animation animation)
            {
                // Toggle the state of breathing in or out
                breatheIn = !breatheIn;

                // Initiate the animated transition to the new text
                int stringID = (breatheIn) ? R.string.breatheInstructionIn : R.string.breatheInstructionOut;
                fadeSwapTextView(textView, getString(stringID));
            }

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override public void onAnimationEnd(Animation animation) { }
        });

        /* Prepare the basic haptic loop that will sync with the breathing *if* enabled
           and if the device passes a couple of basic hardware checks. This should be replaced
           with a more robust and centralized handling/setup of haptics later on so that this
           module only does a basic check of whether or not it's enabled for use here. */
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (haptics && vibrator.hasVibrator() && vibrator.hasAmplitudeControl())
        {
            // The recommended step length is 500 ms, so calculate everything based on that
            final int DURATION = 500;
            // Find how many 500ms steps are needed for the user's desired breath duration
            final int STEPS = (int) ((breathDuration * 1000) / DURATION);
            // Calculate the maximum haptic amplitude based on the user's desired strength (not exceeding 255)
            final int AMPLITUDE_MAX = (int) ((255.0 / 2.0) * hapticStrength);
            // Then calculate the delta in haptic strength for each step
            final int AMPLITUDE_STEP = AMPLITUDE_MAX / STEPS;
            Log.i("Haptic Steps", "Should go from 0-200 in " + breathDuration + " seconds");

            // Assign each amplitude and its timing to the arrays that will create the effect
            int[] amplitudes = new int[STEPS];
            long[] timings = new long[STEPS];
            for (int i = 0; i < STEPS; i++)
            {
                amplitudes[i] = (i + 1) * AMPLITUDE_STEP;
                timings[i] = DURATION;
                Log.i(String.valueOf(i), amplitudes[i] + " for " + timings[i] + " ms");
            }

            // Finally, launch the haptic effect loop
            //*********************************************************************************************************************
            // Problem: the 6.5 second loop will continue running until the current cycle is done when the user closes the module *
            //*********************************************************************************************************************
            vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, 0));
        }
        else
        {
            //**************************************************************************************************************
            // This kind of 'error' handling should be moved to wherever haptics as a whole is handled in the app later on *
            //**************************************************************************************************************
            if (!haptics)
                Log.i("Haptics Disabled", "User has disabled haptics.");
            else if (!vibrator.hasVibrator())
                Log.e("Haptics Unsupported", "Device does not have haptics.");
            else if (!vibrator.hasAmplitudeControl())
                Log.e("Haptics Unsupported", "Device does not have amplitude control.");
        }

        breatheIn = true;

        // Finally, start the breathing animation
        findViewById(R.id.breathVisual).startAnimation(breathAnim);
        // And start the audio (it handles the user preference)
        playAudio();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (audioInstruction != null)
        {
            audioInstruction.release();
            audioInstruction = null;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean("breatheIn", breatheIn);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        breatheIn = savedInstanceState.getBoolean("breatheIn");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        playAudio();
    }

    public void onBack(View view)
    {
        finish();
    }

    // Change the text of a TextView in the middle of fading out and then back in
    private void fadeSwapTextView(TextView textView, CharSequence text)
    {
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            // When the animation ends, change the text, start the animation to fade back in, and play the next audio cue
            @Override
            public void onAnimationEnd(Animation animation)
            {
                textView.setText(text);
                textView.startAnimation(fadeIn);
                playAudio();
            }
        });

        textView.startAnimation(fadeOut);
    }

    private void playAudio()
    {
        // Only do something if the audio is enabled by the user
        if (audio)
        {
            // Make sure any previous audio object is released to avoid memory leaks
            if (audioInstruction != null)
            {
                audioInstruction.release();
                audioInstruction = null;
            }

            // Load the correct audio file (either to breathe in or out)
            int resourceID = (breatheIn) ? R.raw.breath_in : R.raw.breathe_out;
            Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + resourceID);
            try
            {
                audioInstruction = new MediaPlayer();
                audioInstruction.setDataSource(getApplicationContext(), path);
            }
            catch (IOException e)
            {
                Log.e("GuidedBreathing setMediaPlayer() exception", e.getMessage());
                return;
            }

            // Then set the audio file to play as soon as it's prepared in another thread
            audioInstruction.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mp)
                {
                    audioInstruction.setVolume(0.5f * volume, 0.5f * volume);
                    audioInstruction.start();
                }
            });
            audioInstruction.prepareAsync();
        }
    }
}