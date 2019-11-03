package com.xu.floatview.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;


public class TestActC extends BaseAct {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_c);
    }

    public void back(View view) {
        finish();
    }

}
