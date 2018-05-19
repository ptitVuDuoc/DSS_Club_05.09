package com.example.admin.dss_project.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.adapter.HistoryRegisterSeriaAdapter;
import com.example.admin.dss_project.adapter.HistoryRewardAdapter;
import com.example.admin.dss_project.fragment.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class HistoryRewardFragment extends BaseFragment implements View.OnClickListener {
    private TextView txtEndDay;
    private TextView txtStartDay;

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
        initRecyclerView();
    }

    private void addEvent() {
        findViewById(R.id.btn_filter).setOnClickListener(this);
    }

    private void addControl() {
    }

    private void initRecyclerView() {
        ArrayList<String> strings = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            strings.add("a");
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_history_reward);
        recyclerView.setLayoutManager(layoutManager);
        HistoryRewardAdapter stickerAdapter = new HistoryRewardAdapter(getActivity(), strings);
        recyclerView.setAdapter(stickerAdapter);
    }

    private void setContentDialogFilter(View view){
        txtStartDay = view.findViewById(R.id.txt_start_day);
        txtEndDay = view.findViewById(R.id.txt_end_day);
        txtStartDay.setOnClickListener(this);
        txtEndDay.setOnClickListener(this);
        view.findViewById(R.id.btn_filter_list).setOnClickListener(this);
    }

    private void showDatePicker(final TextView textView){
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, final int i, final int i1, final int i2) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, i2);
                cal.set(Calendar.MONTH, i1);
                cal.set(Calendar.YEAR, i);
                SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String txt = fmt.format(cal.getTimeInMillis());
                textView.setText(txt);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

//                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void postRawJson(){

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_filter:

                BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getActivity());
                final View sheetView = getActivity().getLayoutInflater().inflate(R.layout.dialog_filter, null);
                mBottomSheetDialog.setCancelable(true);
                mBottomSheetDialog.setContentView(sheetView);
                mBottomSheetDialog.show();
                break;

            case R.id.txt_start_day:

               showDatePicker(txtStartDay);

                break;

            case R.id.txt_end_day:

                showDatePicker(txtEndDay);

                break;

            case R.id.btn_filter_list:



                break;


        }
    }
}
