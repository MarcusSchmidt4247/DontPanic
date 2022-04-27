// MS: 2/22/22 - initial code based on https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
// MS: 2/23/22 - added InstanceOf() functions
// MS: 3/18/22 - added 'type' and GetClass() function
// MS: 4/11/22 - moved string literal for module names for easier and safer use

package com.example.dontpanic;

public enum Module
{
    GUIDED_BREATHING (GuidedBreathing.NAME, 0, GuidedBreathing.class),
    SELF_REFLECTION (SelfReflectionActivity.NAME, 1, SelfReflectionActivity.class),
    MENTAL_EXERCISES (MentalExerciseSelectionActivity.NAME, 2, MentalExerciseSelectionActivity.class),
    APP_ACTIVITIES (AppExerciseSelectionActivity.NAME, 3, AppExerciseSelectionActivity.class), // needs update once activities module exists
    HAPTIC_HEARTBEAT (HapticHB.NAME, 4, HapticHB.class); // needs update once haptics module exists

    public final String name;
    public final int id;
    public final Class<?> type;

    Module(String name, int id, Class<?> type)
    {
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public Class<?> GetClass()
    {
        return type;
    }

    //*******************
    // Static functions *
    //*******************

    public static Class<?> GetClass(int _id)
    {
        for (Module module : Module.values())
        {
            if (module.id == _id)
                return module.type;
        }
        return null;
    }

    public static int GetID(String _name)
    {
        for (Module module : Module.values())
        {
            if (module.name.equals(_name))
                return module.id;
        }

        return -1;
    }

    public static String GetName(int _id)
    {
        for (Module module : Module.values())
        {
            if (module.id == _id)
                return module.name;
        }

        return "";
    }

    public static Module InstanceOf(String _name)
    {
        for (Module module : Module.values())
        {
            if (module.name.equals(_name))
                return module;
        }

        return null;
    }

    public static Module InstanceOf(int _id)
    {
        for (Module module : Module.values())
        {
            if (module.id == _id)
                return module;
        }

        return null;
    }
}