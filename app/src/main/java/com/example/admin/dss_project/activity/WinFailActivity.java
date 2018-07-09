package com.example.admin.dss_project.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.admin.dss_project.R;

public class WinFailActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_fail);

        addControl();
        addEvent();
    }

    private void addEvent() {
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    private void addControl() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }

}
