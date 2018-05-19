package com.example.admin.dss_project.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.adapter.ListGiftAdapter;
import com.example.admin.dss_project.custom.view.MyProgressDialog;
import com.example.admin.dss_project.fragment.BaseFragment;
import com.example.admin.dss_project.model.Gift;
import com.example.admin.dss_project.model.ListGift;
import com.example.admin.dss_project.model.User;
import com.example.admin.dss_project.retrofit.APIRegisterUser;
import com.example.admin.dss_project.retrofit.ApiUtils;
import com.example.admin.dss_project.ultility.KeyConst;
import com.example.admin.dss_project.ultility.PrefUtils;
import com.example.admin.dss_project.ultility.Statistic;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListGiftFragment extends BaseFragment implements ListGiftAdapter.OnStickerListener, View.OnClickListener {
    private RecyclerView recyclerView;
    private ProgressDialog pleaseDialog;
    private APIRegisterUser mAPIService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_gift, container, false);
    }

    @Override
    protected void initViews() {
        addControl();
        addEvent();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callAPI();
            }
        },200);
    }

    private void addEvent() {
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    private void addControl() {
        pleaseDialog = MyProgressDialog.newInstance(mContext, mContext.getResources().getString(R.string.Please));
    }

    private void createRecycler(List<ListGift> listGift){
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_list_gift);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        ListGiftAdapter stickerAdapter = new ListGiftAdapter(recyclerView, getActivity(),  listGift).setOnStickerListener(this);
        recyclerView.setAdapter(stickerAdapter);
    }

    private void callAPI(){
        pleaseDialog.show();
        mAPIService = ApiUtils.getAPIService();
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
        jsonObject.addProperty(KeyConst.NUMBER_PHONE, PrefUtils.getString(getContext(),KeyConst.NUMBER_PHONE_STATISTIC));
        jsonObject.addProperty(KeyConst.PAGE_NUMBER, 1);

        mAPIService.postRawJSONGetListGift(jsonObject).enqueue(new Callback<Gift>() {
            @Override
            public void onResponse(Call<Gift> call, Response<Gift> response) {
                if(response != null){
                    pleaseDialog.dismiss();

                    if(response.body().getIsSuccess()){
                        createRecycler(response.body().getListGift());
                    }
                }
            }

            @Override
            public void onFailure(Call<Gift> call, Throwable t) {
                pleaseDialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClickListener(int position) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:

                getActivity().onBackPressed();

                break;
        }
    }
}
