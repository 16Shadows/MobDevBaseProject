package org.hse.android.baseproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class ActivityBase extends AppCompatActivity {
    protected abstract void initializeLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeLayout();
    }
}
