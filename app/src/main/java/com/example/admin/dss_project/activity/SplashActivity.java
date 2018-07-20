package com.example.admin.dss_project.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.fragment.SendOtpFragment;
import com.example.admin.dss_project.model.User;
import com.example.admin.dss_project.retrofit.APIRegisterUser;
import com.example.admin.dss_project.retrofit.ApiUtils;
import com.example.admin.dss_project.ultility.KeyConst;
import com.example.admin.dss_project.ultility.PrefUtils;
import com.example.admin.dss_project.ultility.Statistic;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private APIRegisterUser mAPIService;
    private String idNotifi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        setContentView(R.layout.activity_splash);
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                idNotifi = getIntent().getStringExtra(KeyConst.KEY_ID_NOTIFI);
//
//                if( idNotifi != null){

                    if(PrefUtils.getBoolean(getApplicationContext(), KeyConst.KEY_LOGIN_LOGOUT)){
                        setClickLogin();
                    }else {
                        Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                        SplashActivity.this.finish();
                        startActivity(intent);
                    }

//                }else {
//                    if(PrefUtils.getBoolean(getApplicationContext(), KeyConst.KEY_LOGIN_LOGOUT)){
//                        setClickLogin();
//                    }else {
//                        Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
//                        SplashActivity.this.finish();
//                        startActivity(intent);
//                    }
//                }
            }
        },1500);
    }

    private void setClickLogin(){
        mAPIService = ApiUtils.getAPIService();
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
        jsonObject.addProperty(KeyConst.NUMBER_PHONE, PrefUtils.getString(getApplicationContext(),KeyConst.KEY_PREF_USER));
        jsonObject.addProperty(KeyConst.PASSWORD, PrefUtils.getString(getApplicationContext(),KeyConst.KEY_PREF_PASS_WORD));

        mAPIService.postRawJSONLogin(jsonObject).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response != null){
                    Intent intent = new Intent(SplashActivity.this,MainAppActivity.class);
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
