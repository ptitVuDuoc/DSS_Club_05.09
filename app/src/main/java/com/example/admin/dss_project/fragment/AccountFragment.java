package com.example.admin.dss_project.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.activity.HomeActivity;
import com.example.admin.dss_project.activity.MainAppActivity;
import com.example.admin.dss_project.activity.SettingAccountActivity;
import com.example.admin.dss_project.custom.view.MyProgressDialog;
import com.example.admin.dss_project.model.User;
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

public class AccountFragment extends BaseFragment implements View.OnClickListener {

    private SweetAlertDialog pDialog;
    private ProgressDialog pleaseDialog;
    private APIRegisterUser mAPIService;
    private TextView txtNameAcc,txtNameAgency,txtEmail,txtAddress,txtBirthday;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    protected void initViews() {
        addControl();
        addEvent();
        setContent();
    }

    private void addEvent() {
        findViewById(R.id.btn_change_pass).setOnClickListener(this);
        findViewById(R.id.btn_edit_acc).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);
        pleaseDialog = MyProgressDialog.newInstance(mContext, mContext.getResources().getString(R.string.Please));
    }

    private void addControl() {
        txtNameAcc = (TextView) findViewById(R.id.txt_name_acc);
        txtNameAgency = (TextView) findViewById(R.id.txt_name_agency);
        txtEmail = (TextView) findViewById(R.id.txt_email);
        txtAddress = (TextView) findViewById(R.id.txt_address);
        txtBirthday = (TextView) findViewById(R.id.txt_birthday);
    }

    private void setContent(){

        pleaseDialog.show();
        mAPIService = ApiUtils.getAPIService();
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
        jsonObject.addProperty(KeyConst.NUMBER_PHONE, PrefUtils.getString(getContext(),KeyConst.NUMBER_PHONE_STATISTIC));

        mAPIService.postRawJSONGetUserInfo(jsonObject).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response != null){
                    pleaseDialog.dismiss();

                    if(response.body().getIsSuccess()){

                        txtNameAcc.setText(response.body().getHoVaTen());
                        txtNameAgency.setText(response.body().getTenDaiLy());
                        txtAddress.setText(response.body().getDiaChi());
                        txtBirthday.setText(response.body().getNgaySinh().toString());
                        txtEmail.setText(response.body().getEmail());

                    }else {
//                        ((HomeActivity)getActivity()).showDialog(HomeActivity.ERROR,getString(R.string.dowload_data_fail), "" ,getContext());
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_change_pass:

                Intent intentPass = new Intent(getActivity(), SettingAccountActivity.class);
                intentPass.putExtra(KeyConst.KEY_PUT_EXTRA_SETTING_ACCOUNT, Statistic.KEY_CHANGE_PASS);
                startActivity(intentPass);
                break;

            case R.id.btn_edit_acc:

                Intent intentEdit = new Intent(getActivity(), SettingAccountActivity.class);
                intentEdit.putExtra(KeyConst.KEY_PUT_EXTRA_SETTING_ACCOUNT,Statistic.KEY_EDIT_ACCOUNT);
                startActivity(intentEdit);
                break;

            case R.id.btn_logout:
                pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText(getString(R.string.log_out));
                pDialog.setContentText(getString(R.string.content_logout));
                pDialog.setCancelButton(getString(R.string.cancel), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        pDialog.dismiss();
                    }
                });
                pDialog.setConfirmButton(getString(R.string.yes), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                pDialog.setCancelable(false);
                pDialog.show();

                break;
        }
    }
}
