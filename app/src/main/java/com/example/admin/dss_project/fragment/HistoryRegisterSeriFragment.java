package com.example.admin.dss_project.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
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

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.adapter.HistoryRegisterSeriaAdapter;
import com.example.admin.dss_project.custom.view.MyProgressDialog;
import com.example.admin.dss_project.listen.OnLoadMoreListener;
import com.example.admin.dss_project.model.GetListRegitedSeria;
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

public class HistoryRegisterSeriFragment extends BaseFragment implements View.OnClickListener, HistoryRegisterSeriaAdapter.OnStickerListener, OnLoadMoreListener {

    private ProgressDialog pleaseDialog;
    private APIRegisterUser mAPIService;
    private ArrayList<ListSeriaRegistered> getListRegitedSerias = new ArrayList<>() ;
    private RecyclerView recyclerView;
    private EditText txtDayStart;
    private EditText txtDayEnd;
    private DatePickerDialog datePickerDialog;
    private RelativeLayout btnFilterDialog;
    private ArrayList<ListSeriaRegistered> list = new ArrayList<>();
    private BottomSheetDialog mBottomSheetDialog;
    private View sheetView;
    private HistoryRegisterSeriaAdapter historyRegisterAdapter;
    private int mPageNumber = 0;
    private int maxPageNumber;
    private int len;
    private int len2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_register_seri, container, false);
    }

    @Override
    protected void initViews() {
        addControl();
        addEvent();

        initRecyclerView();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String endDay = fmt.format(cal.getTimeInMillis());

        cal.set(Calendar.MONTH,cal.get(Calendar.MONTH) - 1);
        String startDay = fmt.format(cal.getTimeInMillis());

        Statistic.DAY_END_REGISTER_SIREA = endDay;
        Statistic.DAY_START_REGISTER_SIREA = startDay;

        callAPI(startDay,endDay);


    }

    private void addEvent() {
        pleaseDialog = MyProgressDialog.newInstance(mContext, mContext.getResources().getString(R.string.Please));
        findViewById(R.id.btn_filter).setOnClickListener(this);

    }

    private void addControl() {
        mBottomSheetDialog = new BottomSheetDialog(getActivity());
        sheetView = getActivity().getLayoutInflater().inflate(R.layout.dialog_filter, null);
        mBottomSheetDialog.setCancelable(false);
        mBottomSheetDialog.setContentView(sheetView);
    }


    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.list_history_register_seria);
        recyclerView.setLayoutManager(layoutManager);

        historyRegisterAdapter = new HistoryRegisterSeriaAdapter(recyclerView,getActivity(),  list).setOnStickerListener(this);
        recyclerView.setAdapter(historyRegisterAdapter);
        historyRegisterAdapter.setOnLoadMoreListener(this);

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

        mAPIService.postRawJSONGetListRegistedSeria(jsonObject).enqueue(new Callback<GetListRegitedSeria>() {
            @Override
            public void onResponse(Call<GetListRegitedSeria> call, Response<GetListRegitedSeria> response) {
                if(response != null){
                    if(response.body().getIsSuccess()){
                       list.addAll(response.body().getListSeriaRegistered());
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
                        historyRegisterAdapter.notifyDataSetChanged();
                        historyRegisterAdapter.setLoaded();
                    }

                    Log.d("list.size",""+list.size());

                    pleaseDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GetListRegitedSeria> call, Throwable t) {
                pleaseDialog.dismiss();
                mPageNumber--;
            }

        });
    }

    private void setContentDialogFilter(View view){
        txtDayStart = view.findViewById(R.id.txt_start_day);
        txtDayEnd = view.findViewById(R.id.txt_end_day);
        btnFilterDialog = view.findViewById(R.id.btn_filter_list);
        txtDayStart.setText(Statistic.DAY_START_REGISTER_SIREA);
        txtDayEnd.setText(Statistic.DAY_END_REGISTER_SIREA);

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
        Statistic.DAY_START_REGISTER_SIREA = txtDayStart.getText().toString();
        Statistic.DAY_END_REGISTER_SIREA = txtDayEnd.getText().toString();

        btnFilterDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPageNumber = 1;
                list.clear();
                callAPI(Statistic.DAY_START_REGISTER_SIREA, Statistic.DAY_END_REGISTER_SIREA);
                mBottomSheetDialog.dismiss();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_filter:
                setContentDialogFilter(sheetView);
                mBottomSheetDialog.show();
                sheetView.findViewById(R.id.btn_close).setOnClickListener(this);

                break;

            case R.id.btn_close:

                mBottomSheetDialog.dismiss();

                break;
        }
    }

    @Override
    public void onItemStickerClickListener(ListSeriaRegistered listSeriaRegistered) {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getActivity());
        final View sheetView = getActivity().getLayoutInflater().inflate(R.layout.detail_tiem_register_seria, null);
        mBottomSheetDialog.setCancelable(true);
        setContentDialogBottomSheet(listSeriaRegistered, sheetView);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
    }

    private void setContentDialogBottomSheet(ListSeriaRegistered contentDialogBottomSheet, View view){
        TextView txtName = view.findViewById(R.id.txt_name);
        TextView txtProduct = view.findViewById(R.id.txt_code_product);
        TextView txtSeria = view.findViewById(R.id.code_seria);
        TextView txtCodeWard = view.findViewById(R.id.code_reward);
        TextView txtDateRegister = view.findViewById(R.id.txt_date_register);
        TextView txtScores = view.findViewById(R.id.txt_scores);

        txtName.setText(contentDialogBottomSheet.getTenHangHoa());
        txtProduct.setText(contentDialogBottomSheet.getMaHangHoa());
        txtSeria.setText(contentDialogBottomSheet.getSeria());
        txtCodeWard.setText(contentDialogBottomSheet.getMaSoDuThuong());
        txtDateRegister.setText(contentDialogBottomSheet.getNgayDangKy());
        String scores = contentDialogBottomSheet.getSoDiem().toString() + " " + getString(R.string.scores);
        Log.d("datRegister=",""+contentDialogBottomSheet.getNgayDangKy());
        txtScores.setText(scores);
    }

    @Override
    public void onLoadMore() {
       callAPI(Statistic.DAY_START_REGISTER_SIREA,Statistic.DAY_END_REGISTER_SIREA);
    }
}
