package com.example.admin.club.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.admin.club.R;
import com.example.admin.club.model.User;
import com.example.admin.club.retrofit.APIRegisterUser;
import com.example.admin.club.retrofit.ApiUtils;
import com.example.admin.club.ultility.CheckNetwork;
import com.example.admin.club.ultility.KeyConst;
import com.example.admin.club.ultility.PrefUtils;
import com.example.admin.club.ultility.Statistic;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private APIRegisterUser mAPIService;
    private String idNotifi = null;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        setContentView(R.layout.activity_splash);

        requestStoragePermission();

    }

    private void init(){

        if (!CheckNetwork.isNwConnected(getApplicationContext())) {

            checkNetWork();

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (PrefUtils.getBoolean(getApplicationContext(), KeyConst.KEY_LOGIN_LOGOUT)) {
                        setClickLogin();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                        SplashActivity.this.finish();
                        startActivity(intent);
                    }

                }
            }, 1500);
        }
    }

    private void requestStoragePermission() {
        Dexter.withActivity(SplashActivity.this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.VIBRATE,
                        Manifest.permission.INTERNET)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                                init();
//                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        } else {

                            init();

                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle(R.string.need_per);
        builder.setMessage(R.string.this_app_needs_per);
        builder.setPositiveButton(R.string.goto_setting, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void checkNetWork() {
        pDialog = new SweetAlertDialog(SplashActivity.this, SweetAlertDialog.WARNING_TYPE);
        pDialog.setTitleText(getString(R.string.log_out));
        pDialog.setContentText(getString(R.string.content_logout));
        pDialog.setCancelButton(getString(R.string.cancel), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                pDialog.dismiss();
                finish();
            }
        });
        pDialog.setConfirmButton(getString(R.string.retry), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                if (!CheckNetwork.isNwConnected(getApplicationContext())) {

                    checkNetWork();

                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (PrefUtils.getBoolean(getApplicationContext(), KeyConst.KEY_LOGIN_LOGOUT)) {
                                setClickLogin();
                            } else {
                                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                                SplashActivity.this.finish();
                                startActivity(intent);
                            }

                        }
                    }, 1500);
                }
            }
        });
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void setClickLogin() {
        mAPIService = ApiUtils.getAPIService();
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
        jsonObject.addProperty(KeyConst.NUMBER_PHONE, PrefUtils.getString(getApplicationContext(), KeyConst.KEY_PREF_USER));
        jsonObject.addProperty(KeyConst.PASSWORD, PrefUtils.getString(getApplicationContext(), KeyConst.KEY_PREF_PASS_WORD));

        mAPIService.postRawJSONLogin(jsonObject).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response != null) {
                    Intent intent = new Intent(SplashActivity.this, MainAppActivity.class);
                    SplashActivity.this.finish();
                    intent.putExtra(KeyConst.USER, response.body());
                    intent.putExtra(KeyConst.KEY_ID_NOTIFI, idNotifi);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }
}
