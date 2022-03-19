// MS: 3/17/22 - wrote basic class
// SC: 3/19/22 - changed Modules to ModuleReferences object (Multiple -> Singular)
package com.example.dontpanic;

import java.util.ArrayList;

public class Sequence
{
    private final int id;
    private final String name;
    private ArrayList<ModuleReference> modules;

    public Sequence(int id, String name, ArrayList<ModuleReference> modules)
    {
        this.id = id;
        this.name = name;
        this.modules = modules;
    }

    public int GetID() { return id; }

    public String GetName() { return name; }

    public ArrayList<ModuleReference> GetModules() { return modules; }

    public void SetModules(ArrayList<ModuleReference> modules) { this.modules = modules; }
}