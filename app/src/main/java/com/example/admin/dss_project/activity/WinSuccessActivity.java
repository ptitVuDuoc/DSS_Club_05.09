package com.example.admin.dss_project.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.ultility.KeyConst;

public class WinSuccessActivity extends AppCompatActivity implements View.OnClickListener{

    private String giftName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_success);

        giftName = getIntent().getStringExtra(KeyConst.INTENT_PUT_GIFT_NAME);

        addControl();
        addEvent();
    }

    private void addEvent() {
        findViewById(R.id.btn_confirm).setOnClickListener(this);
    }

    private void addControl() {
        TextView textView = findViewById(R.id.txt_gift_name);
        if(giftName != null){
            textView.setText(giftName);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_confirm:

                finish();

                break;
        }
    }
}
