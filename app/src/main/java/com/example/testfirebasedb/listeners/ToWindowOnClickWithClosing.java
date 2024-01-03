package com.example.testfirebasedb.listeners;

import android.app.Activity;
import android.view.View;

import com.example.testfirebasedb.activity.MyMenuActivity;


public class ToWindowOnClickWithClosing extends ToWindowOnClick {
    public ToWindowOnClickWithClosing(Activity fromActivity, Class<? extends Activity> toActivityClass) {
        super(fromActivity, toActivityClass);
    }

    @Override
    public void onClick(View v) {
        if (!MyMenuActivity.isOpen) {
            super.onClick(v);
        }
        fromActivity.finish();
    }
}
