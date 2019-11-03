package com.xu.floatview.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;


public class TestActB extends BaseAct {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
    }

    public void back(View view) {
        finish();
    }

    public void skipC(View view) {
        Intent intent = new Intent();
        intent.setClass(this, TestActC.class);
        startActivity(intent);
    }
}
