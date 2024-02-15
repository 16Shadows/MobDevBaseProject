package org.hse.android.baseproject;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Teacher implements Serializable {
    private static final long serialVersionUID = 2233;

    private final int id;

    public Teacher(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "Teacher " + id;
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }

}
