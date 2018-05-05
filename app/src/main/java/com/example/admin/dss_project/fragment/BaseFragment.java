package com.example.admin.dss_project.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class BaseFragment extends Fragment {
    protected Context mContext = null;
    @Override
    public Context getContext() {

        if (mContext != null) return mContext;


        return super.getContext();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    public View findViewById(int id) {
        return getView().findViewById(id);
    }

    protected abstract void initViews();


}
