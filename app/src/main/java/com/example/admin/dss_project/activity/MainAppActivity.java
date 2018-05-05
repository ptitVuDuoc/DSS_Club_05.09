package com.example.admin.dss_project.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.fragment.BaseFragment;
import com.example.admin.dss_project.fragment.LoginFragment;
import com.example.admin.dss_project.fragment.ScanFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        init();
    }

    private void init() {
        addControl();
        addEvent();
    }

    private void addControl() {
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
    }

    public void addFragment(BaseFragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.container_main_app, fragment, LoginFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(LoginFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    private void addEvent() {
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_scan) {
                    ScanFragment scanFragment = new ScanFragment();
                    addFragment(scanFragment);
                }
            }
        });
    }
}
