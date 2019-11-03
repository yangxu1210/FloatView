package com.xu.floatview.demo;

import android.support.v7.app.AppCompatActivity;

import com.xu.floatingview.view.XuFloatManager;

public class BaseAct extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        XuFloatManager.get().attach(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        XuFloatManager.get().detach(this);
    }
}
