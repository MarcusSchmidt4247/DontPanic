// MS: 2/23/22 - moved to its own file and added more preferences
// MS: /2/28/22 - more preferences

package com.example.dontpanic;

public class Preferences
{
    private Preferences() { }

    /* The naming convention is to include the data type at the end of the name so that the caller knows
       how to cast the result from GetPreference() */

    // App-wide preferences
    public static final String HAPTICS_ENABLED_BOOLEAN = "haptics";
    public static final String HAPTIC_STRENGTH_FLOAT = "haptic_strength";
    public static final String AUDIO_VOLUME_FLOAT = "audio_volume";
    public static final String LAUNCH_SEQUENCE_INT = "launch_sequence";
    public static final String TEXT_SCALING_FLOAT = "text_scaling"; // valid range is between 1.0 and 2.0?

    // Preferences for the guided breathing module
    public static final String BREATHING_DURATION_FLOAT = "breath_duration";
    public static final String BREATHING_AUDIO_ENABLED_BOOLEAN = "breath_audio";
    public static final String BREATHING_HAPTICS_ENABLED_BOOLEAN = "breath_haptics";
    public static final String BREATHING_CYCLES_INT = "breath_repetition"; // how long the module lasts before ending, or null if infinite
}