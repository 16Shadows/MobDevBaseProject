package org.hse.android.baseproject;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class StudentsGroup implements Serializable {
    private static final long serialVersionUID = 2233;

    private final int id;
    private final String name;

    public StudentsGroup(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
