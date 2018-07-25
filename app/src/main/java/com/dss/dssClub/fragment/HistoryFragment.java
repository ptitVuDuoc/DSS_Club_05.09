package com.dss.dssClub.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dss.dssClub.R;
import com.dss.dssClub.fragment.BaseFragment;


public class HistoryFragment extends BaseFragment implements View.OnClickListener {

    private ImageView btnHistoryRegisterSeri, btnHistoryReward;
    private TextView txtHistoryReward;
    private TextView txtHistoryRegisterSeri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    protected void initViews() {
        addControl();
        addEvent();

        HistoryRewardFragment historyRewardFragment = new HistoryRewardFragment();
        replaceFragment(historyRewardFragment, HistoryRewardFragment.class.getSimpleName());
    }

    private void addEvent() {

        btnHistoryRegisterSeri.setOnClickListener(this);
        btnHistoryReward.setOnClickListener(this);

    }

    private void addControl() {

        btnHistoryRegisterSeri = (ImageView) findViewById(R.id.btn_history_register_seri);
        btnHistoryReward = (ImageView) findViewById(R.id.btn_history_reward);
        txtHistoryReward = (TextView) findViewById(R.id.txt_history_reward);
        txtHistoryRegisterSeri= (TextView) findViewById(R.id.txt_history_regis_seri);
        btnHistoryReward.setImageResource(R.drawable.stroke_btn_history_gif_select);

    }

    public void replaceFragment(BaseFragment fragment,String name){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_left, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_history, fragment, name);
        fragmentTransaction.addToBackStack(name);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {

        Fragment fragment = getFragmentManager().findFragmentById(R.id.container_history);

        switch (view.getId()){
            case R.id.btn_history_register_seri:

                btnHistoryRegisterSeri.setImageResource(R.drawable.stroke_btn_history_regis_seri_select);
                btnHistoryReward.setImageResource(R.drawable.stroke_btn_history_gif);
                txtHistoryRegisterSeri.setTextColor(Color.parseColor("#ffffff"));
                txtHistoryReward.setTextColor(getResources().getColor(R.color.color_bg_btn_history));

                if(fragment instanceof HistoryRegisterSeriFragment){
                    return;
                }
                getFragmentManager().popBackStack();
                HistoryRegisterSeriFragment historyRegisterSeriFragment = new HistoryRegisterSeriFragment();
                replaceFragment(historyRegisterSeriFragment, HistoryRegisterSeriFragment.class.getSimpleName());

                break;

            case R.id.btn_history_reward:
                btnHistoryReward.setImageResource(R.drawable.stroke_btn_history_gif_select);
                btnHistoryRegisterSeri.setImageResource(R.drawable.stroke_btn_history_regis_seri);
                txtHistoryReward.setTextColor(Color.parseColor("#ffffff"));
                txtHistoryRegisterSeri.setTextColor(getResources().getColor(R.color.color_bg_btn_history));

                if(fragment instanceof HistoryRewardFragment){
                    return;
                }
                getFragmentManager().popBackStack();

                HistoryRewardFragment historyRewardFragment = new HistoryRewardFragment();
                replaceFragment(historyRewardFragment, HistoryRewardFragment.class.getSimpleName());

                break;
        }
    }
}
