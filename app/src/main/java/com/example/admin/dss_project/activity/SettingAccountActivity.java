package com.example.admin.dss_project.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.fragment.BaseFragment;
import com.example.admin.dss_project.fragment.ChangePassWordFragment;
import com.example.admin.dss_project.fragment.EditAccountFragment;
import com.example.admin.dss_project.fragment.LoginFragment;
import com.example.admin.dss_project.ultility.KeyConst;
import com.example.admin.dss_project.ultility.Statistic;

public class SettingAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account);
        init();
    }

    private void init() {
        addControl();
        addEvent();

        Intent intent = getIntent();
        int key = intent.getIntExtra(KeyConst.KEY_PUT_EXTRA_SETTING_ACCOUNT,0);

        switch (key){
            case Statistic.KEY_CHANGE_PASS:
                ChangePassWordFragment changePassWordFragment = new ChangePassWordFragment();
                addFragment(changePassWordFragment);
                break;

            case Statistic.KEY_EDIT_ACCOUNT:
                EditAccountFragment editAccountFragment = new EditAccountFragment();
                addFragment(editAccountFragment);
                break;
        }


    }

    private void addEvent() {
    }

    private void addControl() {
    }

    public void addFragment(BaseFragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.container_setting_acc, fragment, LoginFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(LoginFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
