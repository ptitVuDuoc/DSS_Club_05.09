package com.example.admin.dss_project.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.fragment.BaseFragment;

public class AccountFragment extends BaseFragment {

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
    }

    private void addControl() {
    }
}
