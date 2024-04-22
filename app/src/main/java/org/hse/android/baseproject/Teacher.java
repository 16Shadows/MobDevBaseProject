package org.hse.android.baseproject;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "teacher", indices = {@Index(value = {"name"}, unique = true)})
public class Teacher implements Serializable {
    @Ignore
    private static final long serialVersionUID = 2233;

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String name = "Undefined";

    @Override
    @NonNull
    public String toString() {
        return name;
    }
}