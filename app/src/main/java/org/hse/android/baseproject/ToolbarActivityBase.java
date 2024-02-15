package org.hse.android.baseproject;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

public abstract class ToolbarActivityBase extends ActivityBase {
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}
