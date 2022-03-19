package com.example.dontpanic;

public class ModuleReference {
    private String name;
    private int id;
    private Class<?> type;

    public ModuleReference() {
        super();
        this.name = "";
        this.id = -1;
        this.type = null;
    }

    public ModuleReference(String name, int id, Class<?> type) {
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public Class<?> getType() {
        return type;
    }
}
