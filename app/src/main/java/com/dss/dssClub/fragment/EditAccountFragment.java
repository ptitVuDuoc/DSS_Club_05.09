package com.dss.dssClub.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dss.dssClub.R;
import com.dss.dssClub.custom.view.MyProgressDialog;
import com.dss.dssClub.model.EditAcc;
import com.dss.dssClub.model.User;
import com.dss.dssClub.retrofit.APIRegisterUser;
import com.dss.dssClub.retrofit.ApiUtils;
import com.dss.dssClub.ultility.KeyConst;
import com.dss.dssClub.ultility.PrefUtils;
import com.dss.dssClub.ultility.Statistic;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAccountFragment extends BaseFragment implements View.OnClickListener {
    private EditText txtName;
    private EditText txtNameAgency;
    private EditText txtEmail;
    private EditText txtAddress;
    private EditText txtBirthday;
    private DatePickerDialog datePickerDialog;
    private ProgressDialog pleaseDialog;
    private APIRegisterUser mAPIService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_account, container, false);
    }

    @Override
    protected void initViews() {
        addControl();
        addEvent();

        setText();
    }

    private void addEvent() {
        txtName = (EditText) findViewById(R.id.txt_name);
        txtNameAgency = (EditText) findViewById(R.id.txt_name_agency);
        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtAddress = (EditText) findViewById(R.id.txt_address);
        txtBirthday = (EditText) findViewById(R.id.txt_birthday);

        txtBirthday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                switch (editable.length()){
                    case 2:
                        txtBirthday.setText(txtBirthday.getText().toString()+"/");
                        txtBirthday.setSelection(txtBirthday.getText().length());
                        break;

                    case 5:
                        txtBirthday.setText(txtBirthday.getText().toString()+"/");
                        txtBirthday.setSelection(txtBirthday.getText().length());
                }
            }
        });
    }

    private void setText(){
        User user = (User) getArguments().getSerializable(KeyConst.KEY_PUT_EXTRA_SETTING_ACCOUNT);
        if(user == null) return;;

        txtName.setText(user.getHoVaTen());
        txtNameAgency.setText(user.getTenDaiLy());
        txtEmail.setText(user.getEmail());
        txtAddress.setText(user.getDiaChi());
        txtBirthday.setText(user.getNgaySinh());
    }

    private void addControl() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_save_edit_acc).setOnClickListener(this);
        findViewById(R.id.txt_birthday).setOnClickListener(this);
        pleaseDialog = MyProgressDialog.newInstance(mContext, mContext.getResources().getString(R.string.Please));
    }

    private void clickSaveEditAcc(){

        if(txtName.getText().toString().isEmpty()||txtNameAgency.getText().toString().isEmpty()||txtAddress.getText().toString().isEmpty()
                || txtBirthday.getText().toString().isEmpty() || txtEmail.getText().toString().isEmpty()){
            Toast.makeText(mContext, R.string.not_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        pleaseDialog.show();

        mAPIService = ApiUtils.getAPIService();

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
        jsonObject.addProperty(KeyConst.NUMBER_PHONE, PrefUtils.getString(getContext(),KeyConst.NUMBER_PHONE_STATISTIC));
        jsonObject.addProperty(KeyConst.NAME, txtName.getText().toString());
        jsonObject.addProperty(KeyConst.AGENCY, txtNameAgency.getText().toString());
        jsonObject.addProperty(KeyConst.EMAIl, txtEmail.getText().toString());
        jsonObject.addProperty(KeyConst.ADDRESS, txtAddress.getText().toString());
        jsonObject.addProperty(KeyConst.BIRTH_DAY, txtBirthday.getText().toString());

        mAPIService.postRawJSONEditAcc(jsonObject).enqueue(new Callback<EditAcc>() {
            @Override
            public void onResponse(Call<EditAcc> call, Response<EditAcc> response) {
                if (response != null) {
                    pleaseDialog.dismiss();

                    if (response.body().getIsSuccess()) {
                        Toast.makeText(mContext, R.string.edit_Acc_success, Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                        PrefUtils.putBoolean(getContext(),KeyConst.KEY_CHECK_EDIT_PROFILE,true);

                    } else {
                        Toast.makeText(mContext, R.string.edit_Acc_fail, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<EditAcc> call, Throwable t) {

                pleaseDialog.dismiss();

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                getActivity().finish();
                break;

            case R.id.btn_save_edit_acc:
                clickSaveEditAcc();
                break;

            case R.id.txt_birthday:

                break;
        }
    }
}
