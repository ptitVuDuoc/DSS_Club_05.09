package com.example.admin.dss_project.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
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
import com.example.admin.dss_project.custtom.dialog.MyProgressDialog;
import com.example.admin.dss_project.model.ForgotPass;
import com.example.admin.dss_project.retrofit.APIRegisterUser;
import com.example.admin.dss_project.retrofit.ApiUtils;
import com.example.admin.dss_project.ultility.KeyConst;
import com.example.admin.dss_project.ultility.Statistic;
import com.google.gson.JsonObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassWordFragment extends BaseFragment implements View.OnClickListener {

    private EditText txtNumberPhone;
    private APIRegisterUser mAPIService;
    private ProgressDialog pleaseDialog;
    private SweetAlertDialog pDialog;
    private Button btnConfirmDialog;
    private TextView txtTitleForgotPass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_pass_word, container, false);
    }

    @Override
    protected void initViews() {
        addControl();
        addEvent();
    }

    private void addEvent() {
        findViewById(R.id.btn_forgot).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        pleaseDialog = MyProgressDialog.newInstance(mContext, mContext.getResources().getString(R.string.Please));
    }

    private void addControl() {
        txtTitleForgotPass = (TextView) findViewById(R.id.txt_title_forgot_pass);
        txtNumberPhone = (EditText) findViewById(R.id.txt_number_phone);

        Shader myShader = new LinearGradient(
                0, 0, 0, 100,
                Color.parseColor("#0173B9"), Color.parseColor("#023D67"),
                Shader.TileMode.CLAMP );
        txtTitleForgotPass.getPaint().setShader( myShader );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_forgot:

                if (txtNumberPhone.getText().toString().isEmpty()){
                    Toast.makeText(mContext, R.string.not_empty, Toast.LENGTH_SHORT).show();
                    return;
                }


                pleaseDialog.show();
                mAPIService = ApiUtils.getAPIService();
                final JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
                jsonObject.addProperty(KeyConst.NUMBER_PHONE, txtNumberPhone.getText().toString());

                mAPIService.postRawJSONForGotPass(jsonObject).enqueue(new Callback<ForgotPass>() {
                    @Override
                    public void onResponse(Call<ForgotPass> call, Response<ForgotPass> response) {
                        if(response != null){
                            pleaseDialog.dismiss();

                            if(response.body().getIsSuccess()){
                                pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                pDialog.setTitleText(getString(R.string.success));
                                pDialog.show();
                                btnConfirmDialog = pDialog.findViewById(R.id.confirm_button);
                                btnConfirmDialog.setText(R.string.oke);
                                btnConfirmDialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        LoginFragment loginFragment = new LoginFragment();
                                        ((HomeActivity)getActivity()).addFragment(loginFragment);
                                        pDialog.dismiss();
                                    }
                                });
                            }else {
                                ((HomeActivity)getActivity()).showDialog(HomeActivity.ERROR, getString(R.string.get_pass_fail), response.body().getMessage(),getContext());
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ForgotPass> call, Throwable t) {

                    }
                });

                break;

            case R.id.btn_back:

                getActivity().getSupportFragmentManager().popBackStack();

                break;
        }
    }
}
