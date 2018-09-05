package com.dss.dssClub.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.dss.dssClub.R;
import com.dss.dssClub.custom.view.MyProgressDialog;
import com.dss.dssClub.model.DetailNotifi;
import com.dss.dssClub.model.Notifi;
import com.dss.dssClub.retrofit.APIRegisterUser;
import com.dss.dssClub.retrofit.ApiUtils;
import com.dss.dssClub.ultility.KeyConst;
import com.dss.dssClub.ultility.PrefUtils;
import com.dss.dssClub.ultility.Statistic;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifiActivity extends AppCompatActivity implements View.OnClickListener {
    private Notifi notifi;
    private TextView txtTitleNotifi;
    private APIRegisterUser mAPIService;
    private ProgressDialog progressDialogPl;
    private TextView txtDate;
    private TextView txtContentNotifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifi);
        init();
    }

    private void init() {

        notifi = (Notifi) getIntent().getSerializableExtra(KeyConst.KEY_NOTIFI_DETAIL);

        String idNotifi = getIntent().getStringExtra(KeyConst.KEY_ID_NOTIFI);

        addControl();
        addEvent();

        if (idNotifi != null) {
            callAPI(idNotifi);
            int numberNotifi = PrefUtils.getInt(getApplicationContext(), KeyConst.KEY_SIZE_LIST_NOTIFI) + 1;
            PrefUtils.putInt(getApplicationContext(), KeyConst.KEY_SIZE_LIST_NOTIFI, numberNotifi);

        } else if (notifi != null) {
            txtTitleNotifi.setText(notifi.getTieuDe());
            txtDate.setText(notifi.getNgay());
            txtContentNotifi.setText(notifi.getNoiDung());
        }

    }

    private void addControl() {
        txtDate = findViewById(R.id.txt_date);
        txtTitleNotifi = findViewById(R.id.txt_title_notifi);
        txtContentNotifi = findViewById(R.id.txt_content_notifi);
    }

    private void callAPI(String id) {

        mAPIService = ApiUtils.getAPIService();
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
        jsonObject.addProperty(KeyConst.ID, id);
        progressDialogPl.show();

        mAPIService.postRawJSONGetInfoNotifi(jsonObject).enqueue(new Callback<DetailNotifi>() {
            @Override
            public void onResponse(Call<DetailNotifi> call, Response<DetailNotifi> response) {
                if (response != null) {
                    progressDialogPl.dismiss();
                    if (response.body().getIsSuccess()) {
                        txtTitleNotifi.setText(response.body().getTieuDe());
                        String datetime = "" + response.body().getNgay();
                        String content = "" + response.body().getNoiDung();
                        txtDate.setText(datetime);
                        txtContentNotifi.setText(content);
                        notifi.setID(response.body().getID());
                        if (response.body().getLinkWeb() != null) {
                            notifi.setLinkWeb(response.body().getLinkWeb());
                            findViewById(R.id.btn_see_detail).setVisibility(View.VISIBLE);
                        }else {
                            findViewById(R.id.btn_see_detail).setVisibility(View.GONE);
                        }
                        notifi.setNgay(datetime);
                        notifi.setNoiDung(content);
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailNotifi> call, Throwable t) {
                progressDialogPl.dismiss();
            }
        });
    }

    private void addEvent() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_see_detail).setOnClickListener(this);
        progressDialogPl = MyProgressDialog.newInstance(NotifiActivity.this, getApplicationContext().getResources().getString(R.string.Please));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;

            case R.id.btn_see_detail:
//                Intent intent = new Intent(NotifiActivity.this,DetailNotifiActivity.class);
//                intent.putExtra(KeyConst.KEY_NOTIFI_DETAIL, (Serializable) notifi);
//                startActivity(intent);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(notifi.getLinkWeb()));
                startActivity(i);
                break;
        }
    }
}
