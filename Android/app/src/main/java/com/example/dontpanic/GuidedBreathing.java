// MS: 2/27/22 - initial code
// MS: 2/28/22 - added very basic haptic effect

package com.example.dontpanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class GuidedBreathing extends AppCompatActivity
{
    // All user preferences that relate to this module
    private boolean haptics;
    private float hapticStrength;
    private boolean audio;
    private float breathDuration;

    private boolean breatheIn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guided_breathing);

        // Retrieve and set the user's preference for how long each breath will last
        Object preference = Database.GetPreference(Preferences.BREATHING_DURATION_FLOAT);
        if (preference == null)
        {
            // Error handling
            Log.e("Database Error", "Preference returned null");
            breathDuration = -1;
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

        ImageView imageView = (ImageView) findViewById(R.id.breathVisual);
        TextView textView = (TextView) findViewById(R.id.breathInstruction);

        // Prepare the breathing animation
        Animation breathAnim = AnimationUtils.loadAnimation(this, R.anim.breath_anim);
        // Convert the breath duration from seconds to milliseconds
        breathAnim.setDuration((long) breathDuration * 1000);
        // Set up the handler for events during the animation
        breathAnim.setAnimationListener(new Animation.AnimationListener()
        {
            // When it first starts, synchronize the boolean state with the text being displayed
            @Override
            public void onAnimationStart(Animation animation)
            {
                breatheIn = true;
                textView.setText(R.string.breatheInstructionIn);
            }

            // Whenever the animation repeats, toggle the state and text
            @Override
            public void onAnimationRepeat(Animation animation)
            {
                // Toggle the state of breathing in or out
                breatheIn = !breatheIn;

                // Initiate the animated transition to the new text
                int stringID = (breatheIn) ? R.string.breatheInstructionIn : R.string.breatheInstructionOut;
                fadeSwapTextView(textView, getString(stringID));

                /*
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                {
                    int effect = (breatheIn) ? VibrationEffect.EFFECT_TICK : VibrationEffect.EFFECT_HEAVY_CLICK;
                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createPredefined(effect));
                }
                else
                    Log.e("API level too low", "Can't test clicks");
                 */
            }

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
            // Calculate the maximum haptic amplitude based on the user's desired strength
            final int AMPLITUDE_MAX = (int) (200 * hapticStrength);
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

        // Finally, start the breathing animation
        imageView.startAnimation(breathAnim);
    }

    public void onBack(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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

            // When the animation ends, change the text and start the animation to fade back in
            @Override
            public void onAnimationEnd(Animation animation)
            {
                textView.setText(text);
                textView.startAnimation(fadeIn);
            }
        });

        textView.startAnimation(fadeOut);
    }
}