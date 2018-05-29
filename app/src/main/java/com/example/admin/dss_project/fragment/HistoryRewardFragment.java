package com.example.admin.dss_project.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.adapter.HistoryRewardAdapter;
import com.example.admin.dss_project.custom.view.MyProgressDialog;
import com.example.admin.dss_project.listen.OnLoadMoreListener;
import com.example.admin.dss_project.model.GetListGiftRegisterd;
import com.example.admin.dss_project.model.GetListRegitedSeria;
import com.example.admin.dss_project.model.ListGiftRegistered;
import com.example.admin.dss_project.model.ListSeriaRegistered;
import com.example.admin.dss_project.retrofit.APIRegisterUser;
import com.example.admin.dss_project.retrofit.ApiUtils;
import com.example.admin.dss_project.ultility.KeyConst;
import com.example.admin.dss_project.ultility.PrefUtils;
import com.example.admin.dss_project.ultility.Statistic;
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
    private TextView txtDayStart;
    private TextView txtDayEnd;
    private DatePickerDialog datePickerDialog;
    private RelativeLayout btnFilterDialog;
    private View sheetView;
    private int mPageNumber = 0;
    private ProgressDialog pleaseDialog;
    private APIRegisterUser mAPIService;
    private ArrayList<ListGiftRegistered> list = new ArrayList<>();
    private int maxPageNumber;
    private HistoryRewardAdapter historyRewardAdapter;

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

//        if(mPageNumber == maxPageNumber) return ;

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
        setDateFilter();
    }

    private void setDateFilter(){
        txtDayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();

                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, final int i, final int i1, final int i2) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.DAY_OF_MONTH, i2);
                        cal.set(Calendar.MONTH, i1);
                        cal.set(Calendar.YEAR, i);
                        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String txt = fmt.format(cal.getTimeInMillis());
                        Statistic.DAY_START_REGISTER_GIFT = txt;

                        txtDayStart.setText(Statistic.DAY_START_REGISTER_GIFT);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        txtDayEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();

                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, final int i, final int i1, final int i2) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.DAY_OF_MONTH, i2);
                        cal.set(Calendar.MONTH, i1);
                        cal.set(Calendar.YEAR, i);
                        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String txt = fmt.format(cal.getTimeInMillis());
                        Statistic.DAY_END_REGISTER_GIFT = txt;

                        txtDayEnd.setText(Statistic.DAY_END_REGISTER_GIFT);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

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
//                if (dy > 0) {
//                    if(R.id.btn_filter == 0) return;
//                    findViewById(R.id.btn_filter).setVisibility(View.GONE);
//                } else if (dy < 0) {
//                    if(R.id.btn_filter == 0) return;
//                    findViewById(R.id.btn_filter).setVisibility(View.VISIBLE);
//                }
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
