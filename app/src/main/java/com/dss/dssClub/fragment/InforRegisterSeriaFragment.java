package com.dss.dssClub.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dss.dssClub.R;
import com.dss.dssClub.adapter.ListInforSeriaRegisterAdapter;
import com.dss.dssClub.model.ListResultSerium;
import com.dss.dssClub.ultility.KeyConst;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InforRegisterSeriaFragment extends BaseFragment implements View.OnClickListener, ListInforSeriaRegisterAdapter.OnStickerListener {

    private List<ListResultSerium> listResultSeriums;
    private RecyclerView recyclerView;
    private ListInforSeriaRegisterAdapter listInforSeriaAdapter;
    private BottomSheetDialog mBottomSheetDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_infor_register_seria, container, false);
    }

    @Override
    protected void initViews() {
        addControl();
        addEvent();
        Bundle bundle = getArguments();
        listResultSeriums = (List<ListResultSerium>) bundle.getSerializable(KeyConst.BUNLDE_LIST_INFOR_SERIA_REGISTER);
        if (listResultSeriums != null) {
            initRecycler(listResultSeriums);
        }
    }

    private void addEvent() {
        findViewById(R.id.view_infor_register_seria).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
    }

    private void addControl() {
    }

    private void initRecycler(List<ListResultSerium> listResultSeriums) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_list_infor_register_seria);
        recyclerView.setLayoutManager(layoutManager);
        listInforSeriaAdapter = new ListInforSeriaRegisterAdapter(getContext(), listResultSeriums);
        listInforSeriaAdapter.setOnItemListGiftListener(this);
        recyclerView.setAdapter(listInforSeriaAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_infor_register_seria:

                break;

            case R.id.btn_close:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void onItemClickListener(int position) {
        if(listResultSeriums.get(position).getIsSuccess()){
            showDialogInforItem(listResultSeriums.get(position));
        }else {
            SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText(getContext().getString(R.string.register_seria_fail));
            pDialog.setContentText(listResultSeriums.get(position).getMessage());
            pDialog.setCancelText(getContext().getString(R.string.cancel));
            pDialog.setCancelable(false);
            pDialog.show();
            Button button = pDialog.findViewById(R.id.confirm_button);
            button.setVisibility(View.GONE);
        }
    }

    private void showDialogInforItem(ListResultSerium listResultSerium){
        mBottomSheetDialog = new BottomSheetDialog(getActivity());
        View sheetView = getActivity().getLayoutInflater().inflate(R.layout.dialog_enter_code, null);
        mBottomSheetDialog.setCancelable(false);
        mBottomSheetDialog.setContentView(sheetView);
        TextView txtCodeProduct = (TextView) sheetView.findViewById(R.id.txt_code_product);
        TextView txtNumberSeria = (TextView) sheetView.findViewById(R.id.txt_number_seria);
        TextView txtReward = (TextView) sheetView.findViewById(R.id.code_reward);
        TextView txtSocres = (TextView) sheetView.findViewById(R.id.txt_scores);

        txtCodeProduct.setText(listResultSerium.getMaHangHoa());
        txtNumberSeria.setText(listResultSerium.getSeria());
        txtReward.setText(listResultSerium.getMaSoDuThuong());
        txtSocres.setText(listResultSerium.getSoDiem().toString() + " điểm");
        mBottomSheetDialog.show();

        LinearLayoutCompat btnClose = sheetView.findViewById(R.id.btn_close_2);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });
    }
}
