package com.example.admin.dss_project.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

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

public class MainAppActivity extends AppCompatActivity implements View.OnClickListener {

    public static Camera mCamera = null;
    public static QrCodeFinderView qrCodeFinderView;
    private int requestCamera = 0;
    private int isCheckClick = -1;
    private final int CLICK_SCAN = 1;
    private final int CLICK_HISTORY = 2;
    private final int CLICK_WIN = 3;
    private final int CLICK_ACCOUNT = 4;
    private TextView txtScan, txtHistory, txtAccount, txtWin;
    private ImageView iconScan, iconHistory, iconWin, iconAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        init();

        txtScan.setTextColor(getResources().getColor(R.color.color_select_tab));
        iconScan.setColorFilter(getResources().getColor(R.color.color_select_tab));
    }

    private void init() {
        addControl();
        addEvent();

         if (!checkCameraHardware(this)) {
            showErrorHardware();
        } else {
            requestPermision();
        }

        ScanFragment previewFragment = new ScanFragment();
        addFragment(previewFragment,R.id.container_main_app);

    }

    private void addControl() {
        txtScan = findViewById(R.id.txt_scan);
        txtHistory = findViewById(R.id.txt_history);
        txtAccount = findViewById(R.id.txt_account);
        txtWin = findViewById(R.id.txt_win);
        iconScan = findViewById(R.id.ic_scan);
        iconHistory = findViewById(R.id.ic_history);
        iconAccount = findViewById(R.id.ic_account);
        iconWin = findViewById(R.id.ic_win);
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

    public void addFragment(BaseFragment fragment, int container) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_left, R.anim.exit_to_left);
        fragmentTransaction.replace(container, fragment, LoginFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(LoginFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    private void addEvent() {
        findViewById(R.id.btn_nofi).setOnClickListener(this);
        findViewById(R.id.btn_scan).setOnClickListener(this);
        findViewById(R.id.btn_history).setOnClickListener(this);
        findViewById(R.id.btn_win).setOnClickListener(this);
        findViewById(R.id.btn_account).setOnClickListener(this);

    }

    private void setColorMenuBottom(){
        txtScan.setTextColor(getResources().getColor(R.color.color_text_home));
        txtAccount.setTextColor(getResources().getColor(R.color.color_text_home));
        txtWin.setTextColor(getResources().getColor(R.color.color_text_home));
        txtHistory.setTextColor(getResources().getColor(R.color.color_text_home));
        iconScan.setColorFilter(getResources().getColor(R.color.color_text_home));
        iconWin.setColorFilter(getResources().getColor(R.color.color_text_home));
        iconHistory.setColorFilter(getResources().getColor(R.color.color_text_home));
        iconAccount.setColorFilter(getResources().getColor(R.color.color_text_home));
        switch (isCheckClick){
            case CLICK_SCAN:
                txtScan.setTextColor(getResources().getColor(R.color.color_select_tab));
                iconScan.setColorFilter(getResources().getColor(R.color.color_select_tab));
                break;

            case CLICK_HISTORY:
                txtHistory.setTextColor(getResources().getColor(R.color.color_select_tab));
                iconHistory.setColorFilter(getResources().getColor(R.color.color_select_tab));
                break;
            case CLICK_WIN:
                txtWin.setTextColor(getResources().getColor(R.color.color_select_tab));
                iconWin.setColorFilter(getResources().getColor(R.color.color_select_tab));
                break;

            case CLICK_ACCOUNT:
                txtAccount.setTextColor(getResources().getColor(R.color.color_select_tab));
                iconAccount.setColorFilter(getResources().getColor(R.color.color_select_tab));
                break;
        }
    }

    @Override
    public void onClick(View view) {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_main_app);
        Fragment fragmentAcc = getSupportFragmentManager().findFragmentById(R.id.container_tab_account);

        switch (view.getId()){
            case R.id.btn_nofi:

                break;

            case R.id.btn_scan:
                isCheckClick = CLICK_SCAN;
                setColorMenuBottom();

                if(fragment instanceof ScanFragment){
                    return;
                }

                getSupportFragmentManager().popBackStack();
                ScanFragment scanFragment = new ScanFragment();
                addFragment(scanFragment,R.id.container_main_app);

                break;

            case R.id.btn_history:
                isCheckClick = CLICK_HISTORY;
                setColorMenuBottom();

                if(fragment instanceof HistoryFragment){
                    return;
                }

                getSupportFragmentManager().popBackStack();
                HistoryFragment historyFragment = new HistoryFragment();
                addFragment(historyFragment,R.id.container_main_app);
                break;
            case R.id.btn_account:
                isCheckClick = CLICK_ACCOUNT;
                setColorMenuBottom();

                if(fragmentAcc instanceof AccountFragment){
                    return;
                }

                getSupportFragmentManager().popBackStack();
                AccountFragment accountFragment = new AccountFragment();
                addFragment(accountFragment,R.id.container_tab_account);
                break;
            case R.id.btn_win:
                isCheckClick = CLICK_WIN;
                setColorMenuBottom();

                if(fragment instanceof WinFragment){
                    return;
                }

                getSupportFragmentManager().popBackStack();
                WinFragment winFragment = new WinFragment();
                addFragment(winFragment,R.id.container_main_app);
                break;

        }
    }
}
