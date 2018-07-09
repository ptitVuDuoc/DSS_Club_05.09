package com.example.admin.dss_project.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.custom.view.MyProgressDialog;
import com.example.admin.dss_project.model.DetailNotifi;
import com.example.admin.dss_project.model.GetListNotifi;
import com.example.admin.dss_project.model.Notifi;
import com.example.admin.dss_project.retrofit.APIRegisterUser;
import com.example.admin.dss_project.retrofit.ApiUtils;
import com.example.admin.dss_project.ultility.KeyConst;
import com.example.admin.dss_project.ultility.PrefUtils;
import com.example.admin.dss_project.ultility.Statistic;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailNotifiActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtTitleDetailNotifi;
    private WebView webView;
    private ProgressBar progressBarOne;
    private APIRegisterUser mAPIService;
    private ProgressDialog progressDialogPl;
    private Notifi notifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_notifi);
        init();
    }

    private void init() {

        notifi = (Notifi) getIntent().getSerializableExtra(KeyConst.KEY_NOTIFI_DETAIL);
        String idNotifi = getIntent().getStringExtra(KeyConst.KEY_ID_NOTIFI);
        txtTitleDetailNotifi = findViewById(R.id.txt_title_detail_notifi);
        progressBarOne = findViewById(R.id.progressBarOne);
        progressBarOne.setMax(100);
        webView = findViewById(R.id.web_view);
        webView.setWebViewClient(new WebClient());
        webView.getSettings().setJavaScriptEnabled(true);

        addEvent();

        if(idNotifi != null){

            callAPI(idNotifi);

        }else if(notifi != null){
            txtTitleDetailNotifi.setText(notifi.getTieuDe());
            webView.loadUrl(notifi.getLinkWeb());
        }


    }

    private void callAPI(String id){

        Log.d("xxxxxx1111",""+id);

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
                        txtTitleDetailNotifi.setText(response.body().getTieuDe());
                        webView.loadUrl(response.body().getLinkWeb());
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailNotifi> call, Throwable t) {
                progressDialogPl.dismiss();
            }
        });
    }

    class WebClient extends WebViewClient {
        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            progressBarOne.setVisibility(View.VISIBLE);
            progressBarOne.setProgress(view.getProgress());
            if(view.getProgress() == 100){
                progressBarOne.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBarOne.setVisibility(View.GONE);
        }
    }

    private void addEvent() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        progressDialogPl = MyProgressDialog.newInstance(DetailNotifiActivity.this, getApplicationContext().getResources().getString(R.string.Please));
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
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}
