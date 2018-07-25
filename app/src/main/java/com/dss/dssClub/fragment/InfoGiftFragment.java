package com.dss.dssClub.fragment;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dss.dssClub.R;
import com.dss.dssClub.activity.GiftActivity;
import com.dss.dssClub.custom.view.MyProgressDialog;
import com.dss.dssClub.model.DetailGift;
import com.dss.dssClub.model.ListGift;
import com.dss.dssClub.model.RewardGift;
import com.dss.dssClub.retrofit.APIRegisterUser;
import com.dss.dssClub.retrofit.ApiUtils;
import com.dss.dssClub.ultility.KeyConst;
import com.dss.dssClub.ultility.PrefUtils;
import com.dss.dssClub.ultility.Statistic;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoGiftFragment extends BaseFragment implements View.OnClickListener {

    private ArrayList<ListGift> listGifts = new ArrayList<>();
    private String idGift;
    private TextView txtNameGift, txtScores, txtDescribe;
    private SweetAlertDialog pDialog;
    private ProgressDialog pleaseDialog;
    private APIRegisterUser mAPIService;
    private ImageView imageGift;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_gift, container, false);
    }

    @Override
    protected void initViews() {
        addEvent();
        addControl();

        idGift = getArguments().getString(KeyConst.BUNLDE_ID_GIFT);
        callAPi(idGift);

    }

    private void addControl() {
        txtNameGift = (TextView) findViewById(R.id.txt_name_gift);
        txtScores = (TextView) findViewById(R.id.txt_scores);
        txtDescribe  = (TextView) findViewById(R.id.txt_describe);
        imageGift = (ImageView) findViewById(R.id.image_gift);
        pleaseDialog = MyProgressDialog.newInstance(getContext(), getContext().getResources().getString(R.string.Please));

    }

    private void addEvent() {
        findViewById(R.id.view_infoGift).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_reward).setOnClickListener(this);
    }

    private void callAPi(String idGift){
        pleaseDialog.show();
        mAPIService = ApiUtils.getAPIService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
        jsonObject.addProperty(KeyConst.ID_GIFT, idGift);

        mAPIService.postRawJSONDetailGift(jsonObject).enqueue(new Callback<DetailGift>() {
            @Override
            public void onResponse(Call<DetailGift> call, Response<DetailGift> response) {
                if (response != null) {
                    pleaseDialog.dismiss();
                    if (response.body().getIsSuccess()) {

                        txtNameGift.setText(response.body().getTenQuaTang());
                        txtDescribe.setText(response.body().getMoTa());
                        String scores = "-"+response.body().getSoDiemDoi()+" "+" điểm";
                        txtScores.setText(scores);

                        Glide.with(mContext)
                                .load(response.body().getLinkAnh())
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        return false;
                                    }
                                })
                                .into(imageGift);

                    }
                }
            }

            @Override
            public void onFailure(Call<DetailGift> call, Throwable t) {
                pleaseDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.view_infoGift:
                break;

            case R.id.btn_back:
                getActivity().onBackPressed();
                break;

            case R.id.btn_reward:

                ((GiftActivity)getActivity()).showDialogConfirm(idGift,getContext(),pleaseDialog);

                break;

        }
    }
}