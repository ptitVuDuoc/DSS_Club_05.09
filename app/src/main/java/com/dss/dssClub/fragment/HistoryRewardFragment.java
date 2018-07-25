package com.dss.dssClub.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dss.dssClub.R;
import com.dss.dssClub.adapter.HistoryRewardAdapter;
import com.dss.dssClub.custom.view.MyProgressDialog;
import com.dss.dssClub.listen.OnLoadMoreListener;
import com.dss.dssClub.model.GetListGiftRegisterd;
import com.dss.dssClub.model.GetListRegitedSeria;
import com.dss.dssClub.model.ListGiftRegistered;
import com.dss.dssClub.model.ListSeriaRegistered;
import com.dss.dssClub.retrofit.APIRegisterUser;
import com.dss.dssClub.retrofit.ApiUtils;
import com.dss.dssClub.ultility.KeyConst;
import com.dss.dssClub.ultility.PrefUtils;
import com.dss.dssClub.ultility.Statistic;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryRewardFragment extends BaseFragment implements View.OnClickListener, OnLoadMoreListener, HistoryRewardAdapter.OnItemListener {
    private TextView txtEndDay;
    private TextView txtStartDay;
    private BottomSheetDialog mBottomSheetDialog;
    private EditText txtDayStart;
    private EditText txtDayEnd;
    private DatePickerDialog datePickerDialog;
    private RelativeLayout btnFilterDialog;
    private View sheetView;
    private int mPageNumber = 0;
    private ProgressDialog pleaseDialog;
    private APIRegisterUser mAPIService;
    private ArrayList<ListGiftRegistered> list = new ArrayList<>();
    private int maxPageNumber;
    private HistoryRewardAdapter historyRewardAdapter;
    private int len;
    private int len2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_reward, container, false);
    }

    @Override
    protected void initViews() {
        addControl();
        addEvent();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initRecyclerView();

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String endDay = fmt.format(cal.getTimeInMillis());

                cal.set(Calendar.MONTH,cal.get(Calendar.MONTH) - 1);
                String startDay = fmt.format(cal.getTimeInMillis());

                Statistic.DAY_END_REGISTER_GIFT = endDay;
                Statistic.DAY_START_REGISTER_GIFT = startDay;

                callAPI(startDay,endDay);
            }
        },200);
    }

    private void addEvent() {
        findViewById(R.id.btn_filter).setOnClickListener(this);
        sheetView.findViewById(R.id.btn_close).setOnClickListener(this);
    }

    private void addControl() {

        pleaseDialog = MyProgressDialog.newInstance(mContext, mContext.getResources().getString(R.string.Please));

        mBottomSheetDialog = new BottomSheetDialog(getActivity());
        sheetView = getActivity().getLayoutInflater().inflate(R.layout.dialog_filter, null);
        mBottomSheetDialog.setCancelable(false);
        mBottomSheetDialog.setContentView(sheetView);

    }


    private void callAPI(String startDay, String endDay){
        pleaseDialog.show();

        mPageNumber++;
        mAPIService = ApiUtils.getAPIService();
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
        jsonObject.addProperty(KeyConst.NUMBER_PHONE, PrefUtils.getString(getContext(),KeyConst.NUMBER_PHONE_STATISTIC));
        jsonObject.addProperty(KeyConst.START_DAY, startDay);
        jsonObject.addProperty(KeyConst.END_DAY, endDay);
        jsonObject.addProperty(KeyConst.PAGE_NUMBER, mPageNumber);

        mAPIService.postRawJSONGetListGiftRegistered(jsonObject).enqueue(new Callback<GetListGiftRegisterd>() {
            @Override
            public void onResponse(Call<GetListGiftRegisterd> call, Response<GetListGiftRegisterd> response) {
                if(response != null){
                    if(response.body().getIsSuccess()){
                        list.addAll(response.body().getListGiftRegistered());
                        maxPageNumber = response.body().getPageCount();
                    }else {
                        mPageNumber--;
                    }

                    if(list != null){
                        if(list.size() == 0){
                            findViewById(R.id.txt_null_list).setVisibility(View.VISIBLE);
                        }else {
                            findViewById(R.id.txt_null_list).setVisibility(View.GONE);
                        }
//                        setDataRecyclerView(recyclerView,response.body().getListSeriaRegistered());
                        historyRewardAdapter.notifyDataSetChanged();
                        historyRewardAdapter.setLoaded();
                    }

                    pleaseDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GetListGiftRegisterd> call, Throwable t) {
                pleaseDialog.dismiss();
                mPageNumber--;
            }

        });
    }

    private void setContentDialogFilter(View view){
        txtDayStart = view.findViewById(R.id.txt_start_day);
        txtDayEnd = view.findViewById(R.id.txt_end_day);
        btnFilterDialog = view.findViewById(R.id.btn_filter_list);
        txtDayStart.setText(Statistic.DAY_START_REGISTER_GIFT);
        txtDayEnd.setText(Statistic.DAY_END_REGISTER_GIFT);

        txtDayStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                len2 = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                switch (editable.length()){
                    case 2:
                        if(editable.length() > len2){
                            editable.append("/");
                        }
                        break;

                    case 5:
                        if(editable.length() > len2){
                            editable.append("/");
                        }
                        break;
                }
            }
        });

        txtDayEnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                len = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                switch (editable.length()){
                    case 2:
                        if(editable.length() > len){
                            editable.append("/");
                        }
                        break;

                    case 5:
                        if(editable.length() > len){
                            editable.append("/");
                        }
                        break;
                }
            }
        });

        setDateFilter();
    }

    private void setDateFilter(){

        Statistic.DAY_START_REGISTER_GIFT = txtDayStart.getText().toString();
        Statistic.DAY_END_REGISTER_GIFT = txtDayEnd.getText().toString();

        btnFilterDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPageNumber = 1;
                list.clear();
                callAPI(Statistic.DAY_START_REGISTER_GIFT, Statistic.DAY_END_REGISTER_GIFT);
                mBottomSheetDialog.dismiss();
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_history_reward);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        historyRewardAdapter = new HistoryRewardAdapter(recyclerView, getContext(), list);
        historyRewardAdapter.setOnLoadMoreListener(this);
        historyRewardAdapter.setOnItemListener(this);
        recyclerView.setAdapter(historyRewardAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_filter:

                setContentDialogFilter(sheetView);
                mBottomSheetDialog.show();
                break;

            case R.id.btn_filter_list:

                break;

            case R.id.btn_close:

                if (mBottomSheetDialog != null) {
                    mBottomSheetDialog.dismiss();
                }

                break;


        }
    }

    private void setContentDialogBottomSheet(ListGiftRegistered contentDialogBottomSheet, View view){
        TextView txtName = view.findViewById(R.id.txt_name);
        TextView txtStatus = view.findViewById(R.id.txt_status);
        TextView txtCodeGift = view.findViewById(R.id.txt_code_gift);
        TextView txtDateRegister = view.findViewById(R.id.txt_date_register);

        txtName.setText(contentDialogBottomSheet.getTenQuaTang());
        txtStatus.setText(contentDialogBottomSheet.getTinhTrang());
        txtCodeGift.setText(contentDialogBottomSheet.getMaQuaTang());
        txtDateRegister.setText(contentDialogBottomSheet.getNgayDangKy());
    }

    @Override
    public void onLoadMore() {
        callAPI(Statistic.DAY_START_REGISTER_GIFT,Statistic.DAY_END_REGISTER_GIFT);
    }

    @Override
    public void onItemClickListener(ListGiftRegistered listGiftRegistered) {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getActivity());
        final View sheetView = getActivity().getLayoutInflater().inflate(R.layout.detail_tiem_register_gift, null);
        mBottomSheetDialog.setCancelable(true);
        setContentDialogBottomSheet(listGiftRegistered, sheetView);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
    }
}
