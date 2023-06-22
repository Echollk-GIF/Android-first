package com.ixuea.courses.android_first;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ixuea.courses.android_first.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
