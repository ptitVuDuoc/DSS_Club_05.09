package com.example.admin.dss_project.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.custom.view.MyProgressDialog;
import com.example.admin.dss_project.fragment.AccountFragment;
import com.example.admin.dss_project.fragment.BaseFragment;
import com.example.admin.dss_project.fragment.HistoryFragment;
import com.example.admin.dss_project.fragment.LoginFragment;
import com.example.admin.dss_project.fragment.ScanFragment;
import com.example.admin.dss_project.fragment.ScanQRcodeFragment;
import com.example.admin.dss_project.fragment.WinFragment;
import com.example.admin.dss_project.model.CheckExistsQRcode;
import com.example.admin.dss_project.model.GetScoresAcc;
import com.example.admin.dss_project.model.GetSizeNotifi;
import com.example.admin.dss_project.model.User;
import com.example.admin.dss_project.retrofit.APIRegisterUser;
import com.example.admin.dss_project.retrofit.ApiUtils;
import com.example.admin.dss_project.ultility.KeyConst;
import com.example.admin.dss_project.ultility.PrefUtils;
import com.example.admin.dss_project.ultility.Statistic;
import com.google.gson.JsonObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private APIRegisterUser mAPIServiceGetSize;
    private TextView txtCountNewNotifi;
    private int countNotifi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        launchActivity();

        init();
        txtScan.setTextColor(getResources().getColor(R.color.color_select_tab));
        iconScan.setColorFilter(getResources().getColor(R.color.color_select_tab));

        String idNotifi = getIntent().getStringExtra(KeyConst.KEY_ID_NOTIFI);

        if(idNotifi != null){
            Intent intent = new Intent(MainAppActivity.this,DetailNotifiActivity.class);
            intent.putExtra(KeyConst.KEY_ID_NOTIFI, idNotifi);
            startActivity(intent);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter(Statistic.ACTION_BROAD_CAST));

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setSizeNotifi();
        }
    };

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

        setSizeNotifi();

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
        txtCountNewNotifi = findViewById(R.id.txt_number_notifi);
    }

    public void showHideKeyBoard(Activity activity){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    private void setSizeNotifi(){
        final JsonObject jsonObject = new JsonObject();
        mAPIServiceGetSize = ApiUtils.getAPIService();
        mAPIServiceGetSize.postRawJSONGetCountNotification(jsonObject).enqueue(new Callback<GetSizeNotifi>() {
            @Override
            public void onResponse(Call<GetSizeNotifi> call, Response<GetSizeNotifi> response) {
                if (response != null) {
                    if (response.body().getIsSuccess()) {
                        countNotifi = response.body().getCount();
                        int intNotifi = response.body().getCount() - PrefUtils.getInt(getApplicationContext(),KeyConst.KEY_SIZE_LIST_NOTIFI);
                        if(intNotifi > 0){
                            findViewById(R.id.bg_cricle).setVisibility(View.VISIBLE);
                            txtCountNewNotifi.setVisibility(View.VISIBLE);
                            txtCountNewNotifi.setText(""+intNotifi);
                        }else {
                            findViewById(R.id.bg_cricle).setVisibility(View.GONE);
                            txtCountNewNotifi.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetSizeNotifi> call, Throwable t) {
            }
        });
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

    private boolean isCheck = false;
    private ProgressDialog pleaseDialog;

    private boolean clickWin(){

        pleaseDialog = MyProgressDialog.newInstance(MainAppActivity.this, getApplicationContext().getResources().getString(R.string.Please));
        pleaseDialog.show();

        APIRegisterUser mAPIService = ApiUtils.getAPIService();
        JsonObject jsonObject = new JsonObject();

        mAPIService.postRawJSONCheckExistsQRcode(jsonObject).enqueue(new Callback<CheckExistsQRcode>() {
            @Override
            public void onResponse(Call<CheckExistsQRcode> call, Response<CheckExistsQRcode> response) {
                if (response != null) {
                    isCheck = response.body().getIsSuccess();
                    pleaseDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CheckExistsQRcode> call, Throwable t) {
                pleaseDialog.dismiss();
            }
        });

        return isCheck;
    }

    @Override
    public void onClick(View view) {

        final Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_main_app);
        Fragment fragmentAcc = getSupportFragmentManager().findFragmentById(R.id.container_tab_account);

        switch (view.getId()) {
            case R.id.btn_nofi:

                Intent intent = new Intent(MainAppActivity.this,NotificationActivity.class);
                startActivity(intent);

                findViewById(R.id.bg_cricle).setVisibility(View.GONE);
                txtCountNewNotifi.setVisibility(View.GONE);

                PrefUtils.putInt(getApplicationContext(),KeyConst.KEY_SIZE_LIST_NOTIFI,countNotifi);

                break;

            case R.id.btn_reward:


                if (countClickListGift != 0){
                    return;
                }

                if(fragment instanceof ScanFragment){
                    ((ScanFragment) fragment).stopCamera();
                }

                Intent intentNoti = new Intent(MainAppActivity.this,GiftActivity.class);
                startActivity(intentNoti);
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

                pleaseDialog = MyProgressDialog.newInstance(MainAppActivity.this, getApplicationContext().getResources().getString(R.string.Please));
                pleaseDialog.show();

                APIRegisterUser mAPIService = ApiUtils.getAPIService();
                JsonObject jsonObject = new JsonObject();
                mAPIService.postRawJSONCheckExistsQRcode(jsonObject).enqueue(new Callback<CheckExistsQRcode>() {
                    @Override
                    public void onResponse(Call<CheckExistsQRcode> call, Response<CheckExistsQRcode> response) {
                        if (response != null) {
                            isCheck = response.body().getIsSuccess();
                            pleaseDialog.dismiss();
                            if(!isCheck){
                                SweetAlertDialog pDialog = new SweetAlertDialog(MainAppActivity.this, SweetAlertDialog.ERROR_TYPE);
                                pDialog.setTitleText(getApplicationContext().getString(R.string.win_reward));
                                pDialog.setContentText(getString(R.string.win_now));
                                pDialog.setCancelText(getApplicationContext().getString(R.string.cancel));
                                pDialog.setCancelable(false);
                                pDialog.show();
                                Button button = pDialog.findViewById(R.id.confirm_button);
                                button.setVisibility(View.GONE);
                            }else {

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
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
                                    }
                                },200);

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckExistsQRcode> call, Throwable t) {
                        pleaseDialog.dismiss();
                    }
                });
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        countClickListGift = 0;
        getScoresUser();
    }

    @Override
    protected void onStop() {

        int intNotifi = countNotifi - PrefUtils.getInt(getApplicationContext(),KeyConst.KEY_SIZE_LIST_NOTIFI);
        if(intNotifi > 0){
            findViewById(R.id.bg_cricle).setVisibility(View.VISIBLE);
            txtCountNewNotifi.setVisibility(View.VISIBLE);
            txtCountNewNotifi.setText(""+intNotifi);
        }else {
            findViewById(R.id.bg_cricle).setVisibility(View.GONE);
            txtCountNewNotifi.setVisibility(View.GONE);
        }

        super.onStop();
    }

    private void getScoresUser(){
        APIRegisterUser mAPIService = ApiUtils.getAPIService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
        jsonObject.addProperty(KeyConst.NUMBER_PHONE, PrefUtils.getString(getApplicationContext(), KeyConst.NUMBER_PHONE_STATISTIC));

        mAPIService.postRawJSONGetScoresAcc(jsonObject).enqueue(new Callback<GetScoresAcc>() {
            @Override
            public void onResponse(Call<GetScoresAcc> call, Response<GetScoresAcc> response) {
                if (response != null) {
                    if (response.body().getIsSuccess()) {
                        updateSocres(response.body().getSoDiemHienTai().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetScoresAcc> call, Throwable t) {
            }
        });
    }

    @Override
    public void onBackPressed() {

        final Fragment fragmentContainerMain = getSupportFragmentManager().findFragmentById(R.id.container_main_app);
        Fragment fragmentContainerTabAcc = getSupportFragmentManager().findFragmentById(R.id.container_tab_account);
        Fragment fragmentContainerScanQRcode= getSupportFragmentManager().findFragmentById(R.id.container_scan_qrcode);
        Fragment fragmentContainerWinfragment= getSupportFragmentManager().findFragmentById(R.id.container_home);

        if (fragmentContainerMain instanceof ScanFragment) {
            finish();
            return;
        }

        if (fragmentContainerWinfragment instanceof ScanQRcodeFragment){
            getSupportFragmentManager().popBackStack();
        }else if (fragmentContainerMain instanceof HistoryFragment || fragmentContainerMain instanceof WinFragment
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
