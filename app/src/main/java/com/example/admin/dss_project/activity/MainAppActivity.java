package com.example.admin.dss_project.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.custom.view.scan.CameraPreview;
import com.example.admin.dss_project.custom.view.scan.Constant;
import com.example.admin.dss_project.custom.view.scan.QrCodeFinderView;
import com.example.admin.dss_project.fragment.AccountFragment;
import com.example.admin.dss_project.fragment.BaseFragment;
import com.example.admin.dss_project.fragment.HistoryFragment;
import com.example.admin.dss_project.fragment.LoginFragment;
import com.example.admin.dss_project.fragment.ScanFragment;
import com.example.admin.dss_project.fragment.WinFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainAppActivity extends AppCompatActivity {

    public static Camera mCamera = null;
    public static QrCodeFinderView qrCodeFinderView;
    private int requestCamera = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        init();
    }

    private void init() {
        addControl();
        addEvent();


        ScanFragment previewFragment = new ScanFragment();
        addFragment(previewFragment);


        if (!checkCameraHardware(this)) {
            showErrorHardware();
        } else {
            requestPermision();
        }

    }

    private void addControl() {
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void showErrorHardware() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("abc")
                .setMessage("def")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermision();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void requestPermision() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        requestCamera);
            } else {
                setUpMain();
            }
        } else {
            setUpMain();
        }
    }

    private void setUpMain() {
        initLoadData();
    }

    private void initLoadData() {
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        CameraPreview.previewHeight = sharedPref.getInt(getResources().getString(R.string.preview_height), Constant.INT_DEFAULT_CAMERA_PREVIEW_HEIGHT);
        CameraPreview.previewWidth = sharedPref.getInt(getResources().getString(R.string.preview_width), Constant.INT_DEFAULT_CAMERA_PREVIEW_WIDTH);
    }

    public void addFragment(BaseFragment fragment) {
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
                switch (tabId) {
                    case R.id.tab_scan:
                        ScanFragment scanFragment = new ScanFragment();
                        addFragment(scanFragment);
                        break;

                    case R.id.tab_history:

                        HistoryFragment historyFragment = new HistoryFragment();
                        addFragment(historyFragment);

                        break;

                    case R.id.tab_win:

                        WinFragment winFragment = new WinFragment();
                        addFragment(winFragment);

                        break;

                    case R.id.tab_account:

                        AccountFragment accountFragment = new AccountFragment();
                        addFragment(accountFragment);

                        break;
                }
            }
        });
    }
}
