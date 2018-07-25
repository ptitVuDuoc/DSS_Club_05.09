package com.dss.dssClub.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dss.dssClub.R;
import com.dss.dssClub.fragment.BaseFragment;
import com.dss.dssClub.fragment.LoginFragment;
import com.dss.dssClub.ultility.Statistic;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txt1,txt2;
    public static final int WARNING = 1;
    public static final int ERROR = 2;
    private SweetAlertDialog pDialog;
    private Button btnConfirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }

        setContentView(R.layout.activity_home);

        addControll();
        addEvent();

        FirebaseMessaging.getInstance().subscribeToTopic(Statistic.TOPIC);

        LoginFragment fragment = new LoginFragment();
        addFragment(fragment);
    }

    public void setAnimationButton(RelativeLayout animationButton){
        ScaleAnimation animation = new ScaleAnimation(1.1f, 1.1f, 1.1f, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(50);
        animationButton.startAnimation(animation);
    }

    private void addEvent() {

    }

    private void addControll() {
    }

    public void showDialog(int type, String title, String content, Context context){

        switch (type){

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
//        btnConfirmDialog.setBackgroundColor(Color.parseColor("#DC6B59"));
        pDialog.setCancelText(getResources().getString(R.string.cancel));
        btnConfirmDialog.setVisibility(View.GONE);
    }

    public void addFragment(BaseFragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.container_home, fragment, LoginFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(LoginFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_home);
        if(fragment instanceof LoginFragment){
            finish();
        }else {
            super.onBackPressed();
        }
    }
}
