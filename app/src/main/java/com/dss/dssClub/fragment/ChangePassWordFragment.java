package com.dss.dssClub.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.dss.dssClub.R;
import com.dss.dssClub.activity.HomeActivity;
import com.dss.dssClub.custom.view.MyProgressDialog;
import com.dss.dssClub.fragment.BaseFragment;
import com.dss.dssClub.model.ChangePass;
import com.dss.dssClub.model.User;
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


public class ChangePassWordFragment extends BaseFragment implements View.OnClickListener {

    private APIRegisterUser mAPIService;
    private ProgressDialog pleaseDialog;
    private TextView txtPassNew,txtPassOld, txtPassConfirm;
    private SweetAlertDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_pass_word, container, false);
    }

    @Override
    protected void initViews() {
        addControl();
        addEvent();
    }

    private void addControl() {
        pleaseDialog = MyProgressDialog.newInstance(mContext, mContext.getResources().getString(R.string.Please));
        txtPassOld = (TextView) findViewById(R.id.txt_old_password);
        txtPassNew = (TextView) findViewById(R.id.txt_new_password);
        txtPassConfirm = (TextView) findViewById(R.id.txt_confirm_password);
    }

    private void addEvent() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_change_pass).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:

                getActivity().finish();

                break;

            case R.id.btn_change_pass:

                if(txtPassConfirm.getText().toString().isEmpty() || txtPassNew.getText().toString().isEmpty() || txtPassOld.getText().toString().isEmpty()){
                    Toast.makeText(mContext, R.string.not_empty, Toast.LENGTH_SHORT).show();
                    return;
                }else if (!txtPassConfirm.getText().toString().equals(txtPassNew.getText().toString())){
                    Toast.makeText(mContext, R.string.confirm_pass_fail, Toast.LENGTH_SHORT).show();
                    return;
                }

                pleaseDialog.show();
                mAPIService = ApiUtils.getAPIService();
                final JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
                jsonObject.addProperty(KeyConst.NUMBER_PHONE, PrefUtils.getString(getContext(),KeyConst.NUMBER_PHONE_STATISTIC));
                jsonObject.addProperty(KeyConst.PASS_OLD, txtPassOld.getText().toString());
                jsonObject.addProperty(KeyConst.PASS_NEW, txtPassNew.getText().toString());

                mAPIService.postRawJSONChangePass(jsonObject).enqueue(new Callback<ChangePass>() {
                    @Override
                    public void onResponse(Call<ChangePass> call, Response<ChangePass> response) {
                        if(response != null){
                            pleaseDialog.dismiss();

                            PrefUtils.putString(getContext(),KeyConst.KEY_PREF_PASS_WORD,txtPassOld.getText().toString());

                            if(response.body().getIsSuccess()){
                                pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                                pDialog.setTitleText(getString(R.string.change_password_title));
                                pDialog.setContentText(getString(R.string.change_pass_success));
                                pDialog.setConfirmButton(getString(R.string.yes), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        pDialog.dismiss();
                                        PrefUtils.putString(getContext(),KeyConst.KEY_PREF_PASS_WORD,txtPassNew.getText().toString());
                                        getActivity().finish();
                                    }
                                });
                                pDialog.setCancelable(false);
                                pDialog.show();

                            }else {
                                pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                                pDialog.setTitleText(getString(R.string.change_password_title));
                                pDialog.setContentText(response.body().getMessage());
                                pDialog.setConfirmButton(getString(R.string.cancel), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        pDialog.dismiss();
                                    }
                                });
                                pDialog.setCancelable(false);
                                pDialog.show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ChangePass> call, Throwable t) {
                        pleaseDialog.dismiss();
                    }
                });


                break;
        }
    }
}
