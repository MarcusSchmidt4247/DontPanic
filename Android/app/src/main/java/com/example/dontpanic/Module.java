// MS: 2/22/22 - initial code based on https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
// MS: 2/23/22 - added InstanceOf() functions
// MS: 3/18/22 - added 'type' and GetClass() function

package com.example.dontpanic;

public enum Module
{
    GUIDED_BREATHING ("Guided Breathing", 0, GuidedBreathing.class),
    SELF_REFLECTION ("Reflection", 1, GuidedBreathing.class), // needs update once reflection module exists
    MENTAL_EXERCISES ("Exercises", 2, GuidedBreathing.class), // needs update once exercises module exists
    APP_ACTIVITIES ("Activities", 3, GuidedBreathing.class), // needs update once activities module exists
    HAPTIC_HEARTBEAT ("Haptic Heartbeat", 4, GuidedBreathing.class); // needs update once haptics module exists

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