package com.example.admin.dss_project.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.adapter.ListNotifiAdapter;
import com.example.admin.dss_project.adapter.ListSeriaRegisterAdapter;
import com.example.admin.dss_project.custom.view.MyProgressDialog;
import com.example.admin.dss_project.listen.OnLoadMoreListener;
import com.example.admin.dss_project.model.GetListNotifi;
import com.example.admin.dss_project.model.GetSizeNotifi;
import com.example.admin.dss_project.model.Notifi;
import com.example.admin.dss_project.model.RewardGift;
import com.example.admin.dss_project.retrofit.APIRegisterUser;
import com.example.admin.dss_project.retrofit.ApiUtils;
import com.example.admin.dss_project.ultility.KeyConst;
import com.example.admin.dss_project.ultility.PrefUtils;
import com.example.admin.dss_project.ultility.Statistic;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity implements ListNotifiAdapter.OnStickerListener, View.OnClickListener ,OnLoadMoreListener{

    private RecyclerView recyclerView;
    private ListNotifiAdapter listNotifiAdapter;
    private ProgressDialog progressDialogPl;
    private APIRegisterUser mAPIService;
    private int mPageNumber = 0;
    private List<Notifi> notifis = new ArrayList<>();
    private APIRegisterUser mAPIServiceGetSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        init();
    }

    private void init() {

        addEvent();
        addControl();
        initList();
        callAPI();
        setSizeNotifi();
    }

    private void setSizeNotifi(){
        final JsonObject jsonObject = new JsonObject();
        mAPIServiceGetSize = ApiUtils.getAPIService();
        mAPIServiceGetSize.postRawJSONGetCountNotification(jsonObject).enqueue(new Callback<GetSizeNotifi>() {
            @Override
            public void onResponse(Call<GetSizeNotifi> call, Response<GetSizeNotifi> response) {
                if (response != null) {
                    if (response.body().getIsSuccess()) {
                        PrefUtils.putInt(getApplicationContext(),KeyConst.KEY_SIZE_LIST_NOTIFI,response.body().getCount());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetSizeNotifi> call, Throwable t) {
            }
        });
    }

    private void addEvent() {
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    private void addControl() {

        progressDialogPl = MyProgressDialog.newInstance(NotificationActivity.this, getApplicationContext().getResources().getString(R.string.Please));
        recyclerView = findViewById(R.id.recycler_list_notifi);
    }

    private void callAPI(){

        progressDialogPl.show();

        mPageNumber++;

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String endDay = fmt.format(cal.getTimeInMillis());

        cal.set(Calendar.MONTH,cal.get(Calendar.MONTH) - 5);
        String startDay = fmt.format(cal.getTimeInMillis());

        mAPIService = ApiUtils.getAPIService();
        final String numberphone = PrefUtils.getString(getApplicationContext(), KeyConst.NUMBER_PHONE_STATISTIC);
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
        jsonObject.addProperty(KeyConst.NUMBER_PHONE, numberphone);
        jsonObject.addProperty(KeyConst.START_DAY, startDay);
        jsonObject.addProperty(KeyConst.END_DAY, endDay);
        jsonObject.addProperty(KeyConst.PAGE_NUMBER, mPageNumber);

        mAPIService.postRawJSONGetListNotifi(jsonObject).enqueue(new Callback<GetListNotifi>() {
            @Override
            public void onResponse(Call<GetListNotifi> call, Response<GetListNotifi> response) {
                if (response != null) {
                    progressDialogPl.dismiss();
                    if (response.body().getIsSuccess()) {
                        notifis.addAll(response.body().getList());
                        listNotifiAdapter.notifyDataSetChanged();
                        listNotifiAdapter.setLoaded();
                    }else {
                        mPageNumber--;
                    }
                }
            }

            @Override
            public void onFailure(Call<GetListNotifi> call, Throwable t) {
                progressDialogPl.dismiss();
                mPageNumber--;
            }
        });
    }

    private void initList(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        listNotifiAdapter = new ListNotifiAdapter(recyclerView,getApplicationContext(), notifis);
        listNotifiAdapter.setOnItemListGiftListener(this);
        listNotifiAdapter.setOnLoadMoreListener(this);
        recyclerView.setAdapter(listNotifiAdapter);
    }

    @Override
    public void onItemClickListener(Notifi notifi) {

        Intent intent = new Intent(NotificationActivity.this,DetailNotifiActivity.class);
        intent.putExtra(KeyConst.KEY_NOTIFI_DETAIL, (Serializable) notifi);
        startActivity(intent);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:

                finish();

                break;
        }
    }

    @Override
    public void onLoadMore() {
        callAPI();
    }
}
