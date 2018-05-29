package com.example.admin.dss_project.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.dss_project.R;

public class WinFragment extends BaseFragment implements View.OnClickListener {
    private ImageView btnPlay;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_win, container, false);
    }

    @Override
    protected void initViews() {
        addControl();
        addEvent();
    }

    private void addEvent() {
        findViewById(R.id.btn_play).setOnClickListener(this);
    }

    private void addControl() {
        btnPlay = (ImageView) findViewById(R.id.btn_play);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play:

                ScaleAnimation animation = new ScaleAnimation(1.1f, 1.1f, 1.1f, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(50);
                btnPlay.startAnimation(animation);
                if(true) return;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog = new Dialog(getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setContentView(R.layout.dialog_reward);
                        final ImageView btnConfirm = (ImageView) dialog.findViewById(R.id.btn_confirm);
                        btnConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ScaleAnimation animation2 = new ScaleAnimation(1.1f, 1.1f, 1.1f, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                animation2.setDuration(50);
                                btnConfirm.startAnimation(animation2);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                }, 150);
                            }
                        });
                        dialog.show();

                    }
                }, 150);

                break;
        }
    }
}
