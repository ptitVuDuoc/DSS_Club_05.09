package com.dss.dssClub.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dss.dssClub.R;
import com.dss.dssClub.fragment.BaseFragment;
import com.dss.dssClub.fragment.ChangePassWordFragment;
import com.dss.dssClub.fragment.EditAccountFragment;
import com.dss.dssClub.fragment.LoginFragment;
import com.dss.dssClub.model.User;
import com.dss.dssClub.ultility.KeyConst;
import com.dss.dssClub.ultility.Statistic;

public class SettingAccountActivity extends AppCompatActivity {

    private User user;

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
        user = (User) intent.getSerializableExtra(KeyConst.USER);

        switch (key){
            case Statistic.KEY_CHANGE_PASS:
                ChangePassWordFragment changePassWordFragment = new ChangePassWordFragment();
                addFragment(changePassWordFragment, ChangePassWordFragment.class.getSimpleName());
                break;

            case Statistic.KEY_EDIT_ACCOUNT:
                EditAccountFragment editAccountFragment = new EditAccountFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putSerializable(KeyConst.KEY_PUT_EXTRA_SETTING_ACCOUNT,user);
                editAccountFragment.setArguments(bundle);
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.add(R.id.container_setting_acc, editAccountFragment, EditAccountFragment.class.getSimpleName());
                fragmentTransaction.addToBackStack(EditAccountFragment.class.getSimpleName());
                fragmentTransaction.commit();
                break;
        }


    }

    public User getUser(){
        return user;
    }

    private void addEvent() {
    }

    private void addControl() {
    }

    public void addFragment(BaseFragment fragment, String name){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.add(R.id.container_setting_acc, fragment, name);
        fragmentTransaction.addToBackStack(name);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
