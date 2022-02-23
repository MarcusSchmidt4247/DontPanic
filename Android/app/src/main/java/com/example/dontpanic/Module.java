// MS: 2/22/22 - initial code based on https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html

package com.example.dontpanic;

public enum Module
{
    GUIDED_BREATHING ("Guided Breathing", 0),
    SELF_REFLECTION ("Reflection", 1),
    MENTAL_EXERCISES ("Exercises", 2),
    APP_ACTIVITIES ("Activities", 3),
    HAPTIC_HEARTBEAT ("Haptic Heartbeat", 4);

    public final String name;
    public final int id;

    Module(String name, int id)
    {
        this.name = name;
        this.id = id;
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
}
