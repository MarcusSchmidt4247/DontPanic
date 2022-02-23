// MS: 2/23/22 - moved to its own file and added more preferences

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

    // Preferences for the guided breathing module
    public static final String BREATHING_DURATION_FLOAT = "breath_duration";
    public static final String BREATHING_AUDIO_ENABLED_BOOLEAN = "breath_audio";
}