package com.example.admin.dss_project.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.activity.HomeActivity;
import com.example.admin.dss_project.activity.MainAppActivity;
import com.example.admin.dss_project.custom.view.MyProgressDialog;
import com.example.admin.dss_project.model.User;
import com.example.admin.dss_project.retrofit.APIRegisterUser;
import com.example.admin.dss_project.retrofit.ApiUtils;
import com.example.admin.dss_project.ultility.CheckNetwork;
import com.example.admin.dss_project.ultility.KeyConst;
import com.example.admin.dss_project.ultility.PrefUtils;
import com.example.admin.dss_project.ultility.Statistic;
import com.google.gson.JsonObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private ProgressDialog pleaseDialog;
    private APIRegisterUser mAPIService;
    private EditText txtNumberPhone, txtPassWord;
    private CheckBox checkRemember;
    private SweetAlertDialog pDialog;
    private Button btnConfirmDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    protected void initViews() {
        addControl();
        addEvent();

        if(!CheckNetwork.isNwConnected(getContext())){
            ((HomeActivity)getActivity()).showDialog(HomeActivity.WARNING,getString(R.string.net_work_off),getString(R.string.on_net_work), getContext());
            return;
        }

        setCheckRemember();
    }

    private void addControl() {

        pleaseDialog = MyProgressDialog.newInstance(mContext, mContext.getResources().getString(R.string.Please));
        txtNumberPhone = (EditText) findViewById(R.id.txt_number_phone);
        txtPassWord = (EditText) findViewById(R.id.txt_password);
        checkRemember = (CheckBox) findViewById(R.id.check_box);
    }

    private void addEvent() {

        findViewById(R.id.btn_forgot).setOnClickListener(this);
        findViewById(R.id.txt_register).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);

    }

    private void setCheckRemember(){

        checkRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                PrefUtils.putBoolean(getContext(),KeyConst.KEY_PREF_CHECK_REMEMBER,b);

            }
        });

        if(PrefUtils.getBoolean(getContext(),KeyConst.KEY_PREF_CHECK_REMEMBER)){
            txtNumberPhone.setText(PrefUtils.getString(getContext(),KeyConst.KEY_PREF_USER));
            txtPassWord.setText(PrefUtils.getString(getContext(),KeyConst.KEY_PREF_PASS_WORD));
        }

    }


    @Override
    public void onClick(View view) {

        if(!CheckNetwork.isNwConnected(getContext())){
            ((HomeActivity)getActivity()).showDialog(HomeActivity.WARNING,getString(R.string.net_work_off),getString(R.string.on_net_work), getContext());
            return;
        }

        switch (view.getId()){
            case R.id.btn_forgot:

                ForgotPassWordFragment forgotPassWordFragment = new ForgotPassWordFragment();

                ((HomeActivity)getActivity()).addFragment(forgotPassWordFragment);

                break;

            case R.id.txt_register:

                RegisterFragment registerFragment = new RegisterFragment();

                ((HomeActivity)getActivity()).addFragment(registerFragment);
                
                break;

            case R.id.btn_login:

                if(txtNumberPhone.getText().toString().isEmpty() || txtPassWord.getText().toString().isEmpty()){
                    Toast.makeText(mContext, R.string.not_empty, Toast.LENGTH_SHORT).show();
                    return;
                }else if(txtNumberPhone.getText().toString().length() > 11 || txtNumberPhone.getText().toString().length() < 9){
                    Toast.makeText(mContext, R.string.number_phone_fail, Toast.LENGTH_SHORT).show();
                    return;
                }
                pleaseDialog.show();
                mAPIService = ApiUtils.getAPIService();
                final JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
                jsonObject.addProperty(KeyConst.NUMBER_PHONE, txtNumberPhone.getText().toString());
                jsonObject.addProperty(KeyConst.PASSWORD, txtPassWord.getText().toString());

                mAPIService.postRawJSONLogin(jsonObject).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response != null){
                            pleaseDialog.dismiss();

                            if(response.body().getIsSuccess()){
                                PrefUtils.putString(getContext(),KeyConst.KEY_PREF_USER,jsonObject.get(KeyConst.NUMBER_PHONE).getAsString());
                                PrefUtils.putString(getContext(),KeyConst.KEY_PREF_PASS_WORD,jsonObject.get(KeyConst.PASSWORD).getAsString());

                                if (!response.body().getDaXacThuc()){
                                    SendOtpFragment sendOtpFragment = new SendOtpFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString(KeyConst.KEY_BUNDLE_NUMBER_PHONE,jsonObject.get(KeyConst.NUMBER_PHONE).getAsString());
                                    sendOtpFragment.setArguments(bundle);
                                    ((HomeActivity)getActivity()).addFragment(sendOtpFragment);
                                }else {
                                    Intent intent = new Intent(getActivity(), MainAppActivity.class);
                                    getActivity().finish();
                                    startActivity(intent);
                                }

                            }else {
                                ((HomeActivity)getActivity()).showDialog(HomeActivity.ERROR, getString(R.string.login_fail), response.body().getMessage(),getContext());
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });

//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        pleaseDialog.dismiss();
//                    }
//                }, 1000);

                break;
        }
    }
}
