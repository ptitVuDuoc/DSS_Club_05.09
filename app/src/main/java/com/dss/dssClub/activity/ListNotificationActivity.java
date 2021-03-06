package com.dss.dssClub.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dss.dssClub.R;
import com.dss.dssClub.adapter.ListNotifiAdapter;
import com.dss.dssClub.custom.view.MyProgressDialog;
import com.dss.dssClub.listen.OnLoadMoreListener;
import com.dss.dssClub.model.GetListNotifi;
import com.dss.dssClub.model.Notifi;
import com.dss.dssClub.retrofit.APIRegisterUser;
import com.dss.dssClub.retrofit.ApiUtils;
import com.dss.dssClub.ultility.KeyConst;
import com.dss.dssClub.ultility.PrefUtils;
import com.dss.dssClub.ultility.Statistic;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListNotificationActivity extends AppCompatActivity implements ListNotifiAdapter.OnStickerListener, View.OnClickListener ,OnLoadMoreListener{

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
        setContentView(R.layout.activity_list_notification);
        init();
    }

    private void init() {

        addEvent();
        addControl();
        initList();
        callAPI();
    }

    private void addEvent() {
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    private void addControl() {

        progressDialogPl = MyProgressDialog.newInstance(ListNotificationActivity.this, getApplicationContext().getResources().getString(R.string.Please));
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

        Intent intent = new Intent(ListNotificationActivity.this,NotifiActivity.class);
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
