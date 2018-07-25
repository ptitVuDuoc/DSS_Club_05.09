package com.dss.dssClub.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dss.dssClub.R;
import com.dss.dssClub.activity.GiftActivity;
import com.dss.dssClub.adapter.ListGiftAdapter;
import com.dss.dssClub.custom.view.MyProgressDialog;
import com.dss.dssClub.listen.OnLoadMoreListener;
import com.dss.dssClub.model.Gift;
import com.dss.dssClub.model.ListGift;
import com.dss.dssClub.retrofit.APIRegisterUser;
import com.dss.dssClub.retrofit.ApiUtils;
import com.dss.dssClub.ultility.KeyConst;
import com.dss.dssClub.ultility.PrefUtils;
import com.dss.dssClub.ultility.Statistic;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListGiftFragment extends BaseFragment implements ListGiftAdapter.OnStickerListener, View.OnClickListener, OnLoadMoreListener {
    private RecyclerView recyclerView;
    private ProgressDialog pleaseDialog;
    private APIRegisterUser mAPIService;
    private ListGiftAdapter listGiftAdapter;
    private ArrayList<ListGift> listGifts = new ArrayList<>();
    private int pageNumber = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_gift, container, false);
    }

    @Override
    protected void initViews() {
        addControl();
        addEvent();
        createRecycler();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callAPI();
            }
        }, 200);

    }

    private void addEvent() {
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    private void addControl() {
        pleaseDialog = MyProgressDialog.newInstance(mContext, mContext.getResources().getString(R.string.Please));
    }

    private void createRecycler() {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_list_gift);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        listGiftAdapter = new ListGiftAdapter(recyclerView, getActivity(), listGifts).setOnItemListGiftListener(this);
        recyclerView.setAdapter(listGiftAdapter);
        listGiftAdapter.setOnLoadMoreListener(this);
    }

    private void callAPI() {
        pleaseDialog.show();
        pageNumber++;
        mAPIService = ApiUtils.getAPIService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
        jsonObject.addProperty(KeyConst.NUMBER_PHONE, PrefUtils.getString(getContext(), KeyConst.NUMBER_PHONE_STATISTIC));
        jsonObject.addProperty(KeyConst.PAGE_NUMBER, pageNumber);

        mAPIService.postRawJSONGetListGift(jsonObject).enqueue(new Callback<Gift>() {
            @Override
            public void onResponse(Call<Gift> call, Response<Gift> response) {
                if (response != null) {
                    pleaseDialog.dismiss();

                    if (response.body().getIsSuccess()) {
                        listGifts.addAll(response.body().getListGift());
                        listGiftAdapter.notifyDataSetChanged();
                        listGiftAdapter.setLoaded();
                    } else {
                        pageNumber--;
                    }
                }
            }

            @Override
            public void onFailure(Call<Gift> call, Throwable t) {
                pleaseDialog.dismiss();
                pageNumber--;
            }
        });
    }

    private void showDetailGift(int position , ArrayList<ListGift> listGifts) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        InfoGiftFragment infoGiftFragment = new InfoGiftFragment();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.add(R.id.container_main, infoGiftFragment, LoginFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(LoginFragment.class.getSimpleName());

        Bundle bundle = new Bundle();
        bundle.putString(KeyConst.BUNLDE_ID_GIFT,listGifts.get(position).getQuaTangID());
        infoGiftFragment.setArguments(bundle);

        fragmentTransaction.commit();

    }

    @Override
    public void onItemClickListener(int position) {
        showDetailGift(position, listGifts);

    }

    @Override
    public void onRewardClickListener(int position) {
        ((GiftActivity) getActivity()).showDialogConfirm(listGifts.get(position).getQuaTangID(), getContext(), pleaseDialog);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:

                getActivity().onBackPressed();

                break;
        }
    }

    @Override
    public void onLoadMore() {
        callAPI();
    }
}
