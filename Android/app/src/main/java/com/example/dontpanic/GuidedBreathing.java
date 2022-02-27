// MS: 2/27/22 - initial code

package com.example.dontpanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

        // Animate the breath visual
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
            }

            @Override public void onAnimationEnd(Animation animation) { }
        });
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