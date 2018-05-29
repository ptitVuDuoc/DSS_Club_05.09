package com.example.admin.dss_project.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.fragment.AccountFragment;
import com.example.admin.dss_project.fragment.BaseFragment;
import com.example.admin.dss_project.fragment.HistoryFragment;
import com.example.admin.dss_project.fragment.InfoGiftFragment;
import com.example.admin.dss_project.fragment.ListGiftFragment;
import com.example.admin.dss_project.fragment.LoginFragment;
import com.example.admin.dss_project.fragment.ScanFragment;
import com.example.admin.dss_project.fragment.WinFragment;
import com.example.admin.dss_project.model.User;
import com.example.admin.dss_project.ultility.KeyConst;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainAppActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int WARNING = 1;
    public static final int ERROR = 2;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private final int CLICK_SCAN = 1;
    private final int CLICK_HISTORY = 2;
    private final int CLICK_WIN = 3;
    private final int CLICK_ACCOUNT = 4;
    private int requestCamera = 0;
    private int isCheckClick = -1;
    private TextView txtScan, txtHistory, txtAccount, txtWin;
    private ImageView iconScan, iconHistory, iconWin, iconAccount;
    public static TextView txtScores;
    private TextView txtName;
    private SweetAlertDialog pDialog;
    private Button btnConfirmDialog;
    private int countClickListGift = 0;
    private CardView menuBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        launchActivity();

        init();
        txtScan.setTextColor(getResources().getColor(R.color.color_select_tab));
        iconScan.setColorFilter(getResources().getColor(R.color.color_select_tab));
    }

    public void launchActivity() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            //            Intent intent = new Intent(this, clss);
            //            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //                    if(mClss != null) {
                    //                        Intent intent = new Intent(this, mClss);
                    //                        startActivity(intent);
                    //                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    private void init() {
        addControl();
        addEvent();

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra(KeyConst.USER);
        if(user != null){
            txtName.setText(user.getHoVaTen());
            String txtSc = user.getSoDiemHienTai().toString() + " " + "điểm";
            txtScores.setText(txtSc);
        }

        ScanFragment scanFragment = new ScanFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_main_app, scanFragment, LoginFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(LoginFragment.class.getSimpleName());
        fragmentTransaction.commit();

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
        txtName = findViewById(R.id.txt_name);
        txtScores = findViewById(R.id.txt_scores);
        menuBottom = findViewById(R.id.bottomBar);
    }

    public static void updateSocres(String scores) {
        txtScores.setText(scores + " điểm");
    }

    public void addFragment(BaseFragment fragment, int container) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_left, R.anim.exit_to_left);
        fragmentTransaction.add(container, fragment, LoginFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(LoginFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    private void addEvent() {
        findViewById(R.id.btn_nofi).setOnClickListener(this);
        findViewById(R.id.btn_scan).setOnClickListener(this);
        findViewById(R.id.btn_history).setOnClickListener(this);
        findViewById(R.id.btn_win).setOnClickListener(this);
        findViewById(R.id.btn_account).setOnClickListener(this);
        findViewById(R.id.btn_reward).setOnClickListener(this);

    }

    private void setColorMenuBottom() {
        txtScan.setTextColor(getResources().getColor(R.color.color_text_home));
        txtAccount.setTextColor(getResources().getColor(R.color.color_text_home));
        txtWin.setTextColor(getResources().getColor(R.color.color_text_home));
        txtHistory.setTextColor(getResources().getColor(R.color.color_text_home));
        iconScan.setColorFilter(getResources().getColor(R.color.color_text_home));
        iconWin.setColorFilter(getResources().getColor(R.color.color_text_home));
        iconHistory.setColorFilter(getResources().getColor(R.color.color_text_home));
        iconAccount.setColorFilter(getResources().getColor(R.color.color_text_home));
        switch (isCheckClick) {
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

        switch (view.getId()) {
            case R.id.btn_nofi:

                break;

            case R.id.btn_reward:


                if (countClickListGift != 0) return;

                if(fragment instanceof ScanFragment){
                    ((ScanFragment) fragment).stopCamera();
                }

                Intent intent = new Intent(MainAppActivity.this,GiftActivity.class);
                startActivity(intent);
                countClickListGift = 1;

                break;

            case R.id.btn_scan:
                isCheckClick = CLICK_SCAN;
                setColorMenuBottom();

                if (fragment instanceof ScanFragment) {
                    return;
                }

                for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                    getSupportFragmentManager().popBackStack();
                }
                ScanFragment scanFragment = new ScanFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container_main_app, scanFragment, LoginFragment.class.getSimpleName());
                fragmentTransaction.addToBackStack(LoginFragment.class.getSimpleName());
                fragmentTransaction.commit();

                break;

            case R.id.btn_history:

                isCheckClick = CLICK_HISTORY;
                setColorMenuBottom();

                if (fragment instanceof HistoryFragment) {
                    return;
                }

                if(fragment instanceof ScanFragment){
                    ((ScanFragment)fragment).stopCamera();
                }

                for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                    getSupportFragmentManager().popBackStack();
                }
                HistoryFragment historyFragment = new HistoryFragment();
                addFragment(historyFragment, R.id.container_main_app);
                break;
            case R.id.btn_account:

                isCheckClick = CLICK_ACCOUNT;
                setColorMenuBottom();

                if (fragmentAcc instanceof AccountFragment) {
                    return;
                }

                if(fragment instanceof ScanFragment){
                    ((ScanFragment)fragment).stopCamera();
                }

                for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                    getSupportFragmentManager().popBackStack();
                }
                AccountFragment accountFragment = new AccountFragment();
                addFragment(accountFragment, R.id.container_tab_account);
                break;
            case R.id.btn_win:

                isCheckClick = CLICK_WIN;
                setColorMenuBottom();

                if (fragment instanceof WinFragment) {
                    return;
                }

                if(fragment instanceof ScanFragment){
                    ((ScanFragment)fragment).stopCamera();
                }

                for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                    getSupportFragmentManager().popBackStack();
                }
                WinFragment winFragment = new WinFragment();
                addFragment(winFragment, R.id.container_main_app);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        countClickListGift = 0;
    }

    public void showDialog(int type, String title, String content, Context context) {

        switch (type) {

            case WARNING:
                pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                break;

            case ERROR:
                pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                break;
        }
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(title);
        pDialog.setContentText(content);
        pDialog.setCancelable(false);
        pDialog.show();
        btnConfirmDialog = pDialog.findViewById(R.id.confirm_button);
        btnConfirmDialog.setBackgroundColor(Color.parseColor("#DC6B59"));
        btnConfirmDialog.setText(R.string.cancel);
    }

    @Override
    public void onBackPressed() {

        final Fragment fragmentContainerMain = getSupportFragmentManager().findFragmentById(R.id.container_main_app);
        Fragment fragmentContainerTabAcc = getSupportFragmentManager().findFragmentById(R.id.container_tab_account);
        Fragment fragmentContainerTabHistory = getSupportFragmentManager().findFragmentById(R.id.container_history);

        if (fragmentContainerMain instanceof ScanFragment) {
            finish();
            return;
        }

        if (fragmentContainerMain instanceof HistoryFragment || fragmentContainerMain instanceof WinFragment
                || fragmentContainerTabAcc instanceof AccountFragment) {
            getSupportFragmentManager().popBackStack();
            ScanFragment previewFragment = new ScanFragment();
            addFragment(previewFragment, R.id.container_main_app);

            isCheckClick = CLICK_SCAN;
            setColorMenuBottom();

            return;
        }
    }
}
