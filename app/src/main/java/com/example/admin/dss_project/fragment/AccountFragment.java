package com.example.admin.dss_project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.activity.HomeActivity;
import com.example.admin.dss_project.activity.MainAppActivity;

public class AccountFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    protected void initViews() {
        addControl();
        addEvent();
    }

    private void addEvent() {
        findViewById(R.id.btn_change_pass).setOnClickListener(this);
    }

    private void addControl() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_change_pass:

                ChangePassWordFragment changePassWordFragment = new ChangePassWordFragment();
                ((MainAppActivity)getActivity()).addFragment(changePassWordFragment, R.id.container_tab_account);

                break;
        }
    }
}
