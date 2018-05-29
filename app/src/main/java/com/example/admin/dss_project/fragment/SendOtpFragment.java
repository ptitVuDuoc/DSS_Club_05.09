package com.example.admin.dss_project.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.activity.HomeActivity;
import com.example.admin.dss_project.activity.MainAppActivity;
import com.example.admin.dss_project.custom.view.MyProgressDialog;
import com.example.admin.dss_project.model.SendCodeOTP;
import com.example.admin.dss_project.model.User;
import com.example.admin.dss_project.model.ValidateOTP;
import com.example.admin.dss_project.retrofit.APIRegisterUser;
import com.example.admin.dss_project.retrofit.ApiUtils;
import com.example.admin.dss_project.ultility.KeyConst;
import com.example.admin.dss_project.ultility.PrefUtils;
import com.example.admin.dss_project.ultility.Statistic;
import com.google.gson.JsonObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SendOtpFragment extends BaseFragment implements View.OnClickListener {


    private EditText txtOTP;
    private APIRegisterUser mAPIService;
    private ProgressDialog pleaseDialog;
    private SweetAlertDialog pDialog;
    private Button btnConfirmDialog;
    private TextView txtContentOTP;
    private String numberphone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_otp, container, false);
    }

    @Override
    protected void initViews() {
        numberphone = getArguments().getString(KeyConst.KEY_BUNDLE_NUMBER_PHONE);
        addControl();
        addEvent();
    }

    private void addEvent() {
        findViewById(R.id.btn_send_otp).setOnClickListener(this);
        findViewById(R.id.btn_resend_otp).setOnClickListener(this);
        pleaseDialog = MyProgressDialog.newInstance(mContext, mContext.getResources().getString(R.string.Please));
    }

    private void addControl() {

        txtOTP = (EditText) findViewById(R.id.txt_code_otp);
        txtContentOTP = (TextView) findViewById(R.id.txt_content_confirm_otp);
        String content = getResources().getString(R.string.please_confirm_otp, numberphone);
        txtContentOTP.setText(content);

    }

    private void setClickLogin() {
        pleaseDialog.show();
        mAPIService = ApiUtils.getAPIService();
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
        jsonObject.addProperty(KeyConst.NUMBER_PHONE, PrefUtils.getString(getContext(),KeyConst.KEY_PREF_USER));
        jsonObject.addProperty(KeyConst.PASSWORD, PrefUtils.getString(getContext(),KeyConst.KEY_PREF_PASS_WORD));

        mAPIService.postRawJSONLogin(jsonObject).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response != null) {
                    pleaseDialog.dismiss();

                    if (response.body().getIsSuccess()) {
                        PrefUtils.putString(getContext(), KeyConst.NUMBER_PHONE_STATISTIC, jsonObject.get(KeyConst.NUMBER_PHONE).getAsString());
                        Intent intent = new Intent(getActivity(), MainAppActivity.class);
                        getActivity().finish();
                        intent.putExtra(KeyConst.USER, response.body());
                        startActivity(intent);

                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                pleaseDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_otp:

                if (txtOTP.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, R.string.not_empty, Toast.LENGTH_SHORT).show();
                    return;
                }

                pleaseDialog.show();

                mAPIService = ApiUtils.getAPIService();
                final JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
                jsonObject.addProperty(KeyConst.NUMBER_PHONE, numberphone);
                jsonObject.addProperty(KeyConst.OTP, txtOTP.getText().toString());

                mAPIService.postRawJSONSendOtp(jsonObject).enqueue(new Callback<ValidateOTP>() {
                    @Override
                    public void onResponse(Call<ValidateOTP> call, Response<ValidateOTP> response) {
                        if (response != null) {
                            pleaseDialog.dismiss();

                            if (response.body().getIsSuccess()) {
                                pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                pDialog.setTitleText(getString(R.string.confirm_otp_success));
                                pDialog.show();
                                btnConfirmDialog = pDialog.findViewById(R.id.confirm_button);
                                btnConfirmDialog.setText(R.string.oke);
                                btnConfirmDialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        pDialog.dismiss();
//                                        Intent intent = new Intent(getActivity(), MainAppActivity.class);
//                                        getActivity().finish();
//                                        startActivity(intent);
                                        setClickLogin();
                                    }
                                });
                            } else {
                                ((HomeActivity) getActivity()).showDialog(HomeActivity.ERROR, getString(R.string.send_otp_fail), response.body().getMessage(), getContext());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ValidateOTP> call, Throwable t) {
                        pleaseDialog.dismiss();
                    }
                });
                break;

            case R.id.btn_resend_otp:
                pleaseDialog.show();
                mAPIService = ApiUtils.getAPIService();
                JsonObject jsonObject2 = new JsonObject();
                jsonObject2.addProperty(KeyConst.TOKEN, Statistic.token);
                jsonObject2.addProperty(KeyConst.NUMBER_PHONE, numberphone);

                mAPIService.postRawJSONGetCodeOTP(jsonObject2).enqueue(new Callback<SendCodeOTP>() {
                    @Override
                    public void onResponse(Call<SendCodeOTP> call, Response<SendCodeOTP> response) {
                        if (response != null) {
                            pleaseDialog.dismiss();

                            if (response.body().getIsSuccess()) {
                                String mess = getResources().getString(R.string.send_otp_success, numberphone);
                                pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                pDialog.setTitleText(getString(R.string.success));
                                pDialog.setContentText(mess);
                                pDialog.show();
                                btnConfirmDialog = pDialog.findViewById(R.id.confirm_button);
                                btnConfirmDialog.setText(R.string.oke);

                            } else {
                                String mess = response.body().getMessage();
                                ((HomeActivity) getActivity()).showDialog(HomeActivity.ERROR, getString(R.string.send_otp_fail), mess, getContext());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SendCodeOTP> call, Throwable t) {

                    }
                });

                break;
        }
    }
}
