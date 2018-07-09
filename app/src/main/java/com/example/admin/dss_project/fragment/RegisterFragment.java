package com.example.admin.dss_project.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.activity.HomeActivity;
import com.example.admin.dss_project.custom.view.MyProgressDialog;
import com.example.admin.dss_project.model.Register;
import com.example.admin.dss_project.retrofit.APIRegisterUser;
import com.example.admin.dss_project.retrofit.ApiUtils;
import com.example.admin.dss_project.ultility.KeyConst;
import com.example.admin.dss_project.ultility.PrefUtils;
import com.example.admin.dss_project.ultility.Statistic;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterFragment extends BaseFragment implements View.OnClickListener {

    private DatePickerDialog datePickerDialog;
    private EditText txtBirthday;
    private APIRegisterUser mAPIService;
    private ProgressDialog pleaseDialog;
    private EditText txtNumberPhone;
    private EditText txtName;
    private EditText txtEmail;
    private EditText txtAgency;
    private EditText txtAddress;
    private EditText txtPassword;
    private EditText txtConfirmPassword;
    private int len;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    protected void initViews() {
        addControl();
        addEvent();
        pleaseDialog = MyProgressDialog.newInstance(mContext, mContext.getResources().getString(R.string.Please));
    }

    private void addEvent() {
        findViewById(R.id.txt_birthday).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    private void addControl() {

        txtBirthday = (EditText) findViewById(R.id.txt_birthday);
        txtNumberPhone = (EditText) findViewById(R.id.txt_number_phone);
        txtName = (EditText) findViewById(R.id.txt_name);
        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtAgency = (EditText) findViewById(R.id.txt_agency);
        txtAddress = (EditText) findViewById(R.id.txt_address);
        txtPassword = (EditText) findViewById(R.id.txt_password);
        txtConfirmPassword = (EditText) findViewById(R.id.txt_password_confirm);
        txtBirthday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                len = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                switch (editable.length()) {
                    case 2:
                        if(editable.length() > len){
                            editable.append("/");
                        }
                        break;

                    case 5:
                        if(editable.length() > len){
                            editable.append("/");
                        }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_birthday:
                break;

            case R.id.btn_register:

                if (txtName.getText().toString().isEmpty() || txtNumberPhone.getText().toString().isEmpty()
                        || txtEmail.getText().toString().isEmpty() || txtPassword.getText().toString().isEmpty()
                        || txtAgency.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, R.string.not_empty, Toast.LENGTH_SHORT).show();
                    return;
                } else if (txtNumberPhone.getText().toString().length() > 11 || txtNumberPhone.getText().toString().length() < 10) {
                    Toast.makeText(mContext, R.string.number_phone_fail, Toast.LENGTH_SHORT).show();
                    return;
                } else if (!txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
                    Toast.makeText(mContext, R.string.confirm_pass_fail, Toast.LENGTH_SHORT).show();
                    return;
                }


                pleaseDialog.show();

                mAPIService = ApiUtils.getAPIService();

                final JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
                jsonObject.addProperty(KeyConst.NUMBER_PHONE, txtNumberPhone.getText().toString());
                jsonObject.addProperty(KeyConst.NAME, txtName.getText().toString());
                jsonObject.addProperty(KeyConst.AGENCY, txtAgency.getText().toString());
                jsonObject.addProperty(KeyConst.EMAIl, txtEmail.getText().toString());
                jsonObject.addProperty(KeyConst.ADDRESS, txtAddress.getText().toString());
                jsonObject.addProperty(KeyConst.BIRTH_DAY, txtBirthday.getText().toString());
                jsonObject.addProperty(KeyConst.PASSWORD, txtPassword.getText().toString());

                mAPIService.postRawJSONRegister(jsonObject).enqueue(new Callback<Register>() {
                    @Override
                    public void onResponse(Call<Register> call, Response<Register> response) {
                        if (response != null) {
                            pleaseDialog.dismiss();

                            if (response.body().getIsSuccess()) {
                                SendOtpFragment sendOtpFragment = new SendOtpFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString(KeyConst.KEY_BUNDLE_NUMBER_PHONE, jsonObject.get(KeyConst.NUMBER_PHONE).getAsString());
                                sendOtpFragment.setArguments(bundle);

                                PrefUtils.putString(getContext(), KeyConst.KEY_PREF_USER, jsonObject.get(KeyConst.NUMBER_PHONE).getAsString());
                                PrefUtils.putString(getContext(), KeyConst.KEY_PREF_PASS_WORD, jsonObject.get(KeyConst.PASSWORD).getAsString());
                                PrefUtils.putString(getContext(), KeyConst.NUMBER_PHONE_STATISTIC, jsonObject.get(KeyConst.NUMBER_PHONE).getAsString());

                                ((HomeActivity) getActivity()).addFragment(sendOtpFragment);
                            } else {
                                String mess = response.body().getMessage();
                                ((HomeActivity) getActivity()).showDialog(HomeActivity.ERROR, getString(R.string.register_failed), mess, getContext());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Register> call, Throwable t) {
                        pleaseDialog.dismiss();
                    }
                });
                break;

            case R.id.btn_back:

                getActivity().getSupportFragmentManager().popBackStack();

                break;
        }
    }
}
