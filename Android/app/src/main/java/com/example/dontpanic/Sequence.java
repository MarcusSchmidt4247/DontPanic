// MS: 3/17/22 - wrote basic class
// SC: 3/19/22 - changed Modules to ModuleReferences object (Multiple -> Singular)
package com.example.dontpanic;

import java.util.ArrayList;

public class Sequence
{
    private final int id;
    private final String name;
    private ArrayList<Module> modules;

    public Sequence(int id, String name, ArrayList<Module> modules)
    {
        this.id = id;
        this.name = name;
        this.modules = modules;
    }

    public int GetID() { return id; }

    public String GetName() { return name; }

    public ArrayList<Module> GetModules() { return modules; }

    public void SetModules(ArrayList<Module> modules) { this.modules = modules; }
    public void addModule(Module module) {
        modules.add(module);
    }
    public Class<?> getType(Module module) { return module.type;}
}