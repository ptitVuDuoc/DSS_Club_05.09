package com.example.admin.dss_project.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.adapter.ListGiftAdapter;
import com.example.admin.dss_project.adapter.ListSeriaRegisterAdapter;
import com.example.admin.dss_project.custom.view.MyProgressDialog;
import com.example.admin.dss_project.fragment.InforRegisterSeriaFragment;
import com.example.admin.dss_project.fragment.LoginFragment;
import com.example.admin.dss_project.model.ListRegisterSeria;
import com.example.admin.dss_project.model.PostRawRegiterSeria;
import com.example.admin.dss_project.model.SeriInfo;
import com.example.admin.dss_project.retrofit.APIRegisterUser;
import com.example.admin.dss_project.retrofit.ApiUtils;
import com.example.admin.dss_project.ultility.KeyConst;
import com.example.admin.dss_project.ultility.PrefUtils;
import com.example.admin.dss_project.ultility.Statistic;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterSeriaActivity extends AppCompatActivity implements View.OnClickListener, ListSeriaRegisterAdapter.OnStickerListener {

    private String seria;
    private RecyclerView recyclerView;
    private ListSeriaRegisterAdapter listSeriaAdapter;
    private EditText editSeria;
    private ProgressDialog pleaseDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_seria);
        init();
    }

    private void init() {

        Intent intent = getIntent();
        seria = intent.getStringExtra(KeyConst.BUNLDE_CODE_SERIA);
        pleaseDialog = MyProgressDialog.newInstance(RegisterSeriaActivity.this, RegisterSeriaActivity.this.getResources().getString(R.string.Please));

        if (!seria.isEmpty()) {
            Statistic.addListSeria(seria);
        }

        addControl();
        addEvent();

        createRecycler();


    }

    private void addEvent() {
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_continue).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
    }

    private void addControl() {
        editSeria = findViewById(R.id.edt_seri);
    }

    private void createRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_list_register_seria);
        recyclerView.setLayoutManager(layoutManager);
        listSeriaAdapter = new ListSeriaRegisterAdapter(getApplicationContext(), Statistic.getListSeria());
        listSeriaAdapter.setOnItemListGiftListener(this);
        recyclerView.setAdapter(listSeriaAdapter);
    }

    public void showHideKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void clickRegisterSeria(List<String> strings) {

        pleaseDialog.show();

        if (strings.size() == 0) {
            pleaseDialog.dismiss();
            return;
        }

        APIRegisterUser mAPIService = ApiUtils.getAPIService();
        PostRawRegiterSeria postRawRegiterSeria = new PostRawRegiterSeria();
        postRawRegiterSeria.setTokenKey(Statistic.token);
        postRawRegiterSeria.setSoDienThoai(PrefUtils.getString(getApplicationContext(), KeyConst.NUMBER_PHONE_STATISTIC));
        postRawRegiterSeria.setArraySeria(Statistic.getListSeria());

        mAPIService.postRawJSONListRegisterSeria(postRawRegiterSeria).enqueue(new Callback<ListRegisterSeria>() {
            @Override
            public void onResponse(Call<ListRegisterSeria> call, Response<ListRegisterSeria> response) {

                if (response != null) {
                    pleaseDialog.dismiss();

                    if (response.body().getIsSuccess()){
                        InforRegisterSeriaFragment inforRegisterSeriaFragment = new InforRegisterSeriaFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(KeyConst.BUNLDE_LIST_INFOR_SERIA_REGISTER, (Serializable) response.body().getListResultSeria());
                        inforRegisterSeriaFragment.setArguments(bundle);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_left, R.anim.exit_to_left);
                        fragmentTransaction.add(R.id.container_infor_register_seria, inforRegisterSeriaFragment, InforRegisterSeriaFragment.class.getSimpleName());
                        fragmentTransaction.addToBackStack(InforRegisterSeriaFragment.class.getSimpleName());
                        fragmentTransaction.commit();
                        Toast.makeText(RegisterSeriaActivity.this, R.string.register_seria_success, Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<ListRegisterSeria> call, Throwable t) {
                pleaseDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {

        showHideKeyBoard(RegisterSeriaActivity.this);

        switch (view.getId()) {
            case R.id.btn_close:
                RegisterSeriaActivity.this.finish();
                Statistic.removeListSeria();
                break;

            case R.id.btn_continue:
                RegisterSeriaActivity.this.finish();
                break;

            case R.id.btn_add:

                if (editSeria.getText().toString().isEmpty()) {
                    Toast.makeText(this, R.string.enter_code_seria, Toast.LENGTH_SHORT).show();
                    return;
                }
                Statistic.addListSeria(editSeria.getText().toString());
                listSeriaAdapter.notifyDataSetChanged();
                editSeria.getText().clear();

                break;

            case R.id.btn_register:

                if(Statistic.getListSeria().size() == 0){
                    Toast.makeText(this, getString(R.string.enter_code_seria), Toast.LENGTH_SHORT).show();
                    return;
                }
                clickRegisterSeria(Statistic.getListSeria());
                Statistic.removeListSeria();
                listSeriaAdapter.notifyDataSetChanged();

                break;
        }
    }

    @Override
    public void onItemClickDeleteListener(int position) {
        Statistic.removeItemListSeria(position);
        listSeriaAdapter.notifyDataSetChanged();
        Toast.makeText(this, R.string.delete_success, Toast.LENGTH_SHORT).show();
    }
}
