package com.dss.dssClub.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.dss.dssClub.R;
import com.dss.dssClub.custom.view.MyProgressDialog;
import com.dss.dssClub.fragment.InfoGiftFragment;
import com.dss.dssClub.fragment.ListGiftFragment;
import com.dss.dssClub.fragment.LoginFragment;
import com.dss.dssClub.model.GetScoresAcc;
import com.dss.dssClub.model.RewardGift;
import com.dss.dssClub.retrofit.APIRegisterUser;
import com.dss.dssClub.retrofit.ApiUtils;
import com.dss.dssClub.ultility.KeyConst;
import com.dss.dssClub.ultility.PrefUtils;
import com.dss.dssClub.ultility.Statistic;
import com.google.gson.JsonObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiftActivity extends AppCompatActivity {

    private SweetAlertDialog pDialog;
    private Button btnConfirmDialog;
    private ProgressDialog pleaseDialog;
    private APIRegisterUser mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);
        init();
    }

    private void init() {

        ListGiftFragment listGiftFragment = new ListGiftFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_main, listGiftFragment, LoginFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(LoginFragment.class.getSimpleName());
        fragmentTransaction.commit();

        addControl();

    }

    private void addControl() {
        pleaseDialog = MyProgressDialog.newInstance(getApplicationContext(), getApplicationContext().getResources().getString(R.string.Please));
    }

    public void showDialogConfirm(final String adGift, Context context, final ProgressDialog progressDialogPl) {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        pDialog.setTitleText(getString(R.string.confirm_reward));
        pDialog.setContentText(getString(R.string.how_to_reward));
        pDialog.setCancelable(false);
        pDialog.setConfirmText(getString(R.string.reward_gift));
        pDialog.setCancelText(getString(R.string.cancel));
        pDialog.show();
        btnConfirmDialog = pDialog.findViewById(R.id.confirm_button);
        btnConfirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAPIReward(adGift, getApplicationContext(), progressDialogPl);
            }
        });
    }

    public void callAPIReward(String adGift, final Context context, final ProgressDialog progressDialogPl) {
        progressDialogPl.show();
        mAPIService = ApiUtils.getAPIService();
        final String numberphone = PrefUtils.getString(context, KeyConst.NUMBER_PHONE_STATISTIC);
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
        jsonObject.addProperty(KeyConst.NUMBER_PHONE, numberphone);
        jsonObject.addProperty(KeyConst.ID_GIFT, adGift);

        mAPIService.postRawJSONRewradGift(jsonObject).enqueue(new Callback<RewardGift>() {
            @Override
            public void onResponse(Call<RewardGift> call, Response<RewardGift> response) {
                if (response != null) {
                    progressDialogPl.dismiss();
                    if (response.body().getIsSuccess()) {
                        pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        Button btnCancel = pDialog.findViewById(R.id.cancel_button);
                        btnCancel.setVisibility(View.GONE);
//                        String content = String.format(getString(R.string.reward_success), numberphone);
                        pDialog.setContentText(getString(R.string.reward_success));
//                        Button btnConfirm = pDialog.findViewById(R.id.confirm_button);
//                        btnConfirm.setText(getString(R.string.confirm));
                        btnConfirmDialog.setText(getString(R.string.confirm));
                        updateScoresAcc(context,progressDialogPl);

                    } else {
                        pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        pDialog.setContentText(getString(R.string.reward_fail) + response.body().getMessage());
                    }
                    if (pDialog != null) {
//                        pDialog.setConfirmText(getString(R.string.confirm));
                        btnConfirmDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                pDialog.dismiss();
                            }
                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<RewardGift> call, Throwable t) {
                progressDialogPl.dismiss();
            }
        });
    }

    private void updateScoresAcc(Context context, final ProgressDialog progressDialogPl){
        progressDialogPl.show();
        mAPIService = ApiUtils.getAPIService();
        final String numberphone = PrefUtils.getString(context, KeyConst.NUMBER_PHONE_STATISTIC);
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
        jsonObject.addProperty(KeyConst.NUMBER_PHONE, numberphone);

        mAPIService.postRawJSONGetScoresAcc(jsonObject).enqueue(new Callback<GetScoresAcc>() {
            @Override
            public void onResponse(Call<GetScoresAcc> call, Response<GetScoresAcc> response) {
                if (response != null) {
                    progressDialogPl.dismiss();
                    if (response.body().getIsSuccess()) {
                        MainAppActivity.updateSocres(response.body().getSoDiemHienTai().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetScoresAcc> call, Throwable t) {
                progressDialogPl.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_main);

        if (fragment instanceof ListGiftFragment) {
            GiftActivity.this.finish();
        } else if (fragment instanceof InfoGiftFragment) {
            getSupportFragmentManager().popBackStack();
        }

    }
}
