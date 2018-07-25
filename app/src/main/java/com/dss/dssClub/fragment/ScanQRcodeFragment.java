package com.dss.dssClub.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dss.dssClub.R;
import com.dss.dssClub.activity.WinFailActivity;
import com.dss.dssClub.activity.WinSuccessActivity;
import com.dss.dssClub.custom.view.MyProgressDialog;
import com.dss.dssClub.model.CheckInQRcode;
import com.dss.dssClub.retrofit.APIRegisterUser;
import com.dss.dssClub.retrofit.ApiUtils;
import com.dss.dssClub.scan.ultility.CameraSelectorDialogFragment;
import com.dss.dssClub.scan.ultility.ZXingScannerQRcodeView;
import com.dss.dssClub.scan.ultility.ZXingScannerView;
import com.dss.dssClub.ultility.KeyConst;
import com.dss.dssClub.ultility.PrefUtils;
import com.dss.dssClub.ultility.Statistic;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanQRcodeFragment extends BaseFragment implements View.OnClickListener,

        CameraSelectorDialogFragment.CameraSelectorDialogListener, ZXingScannerQRcodeView.ResultHandler {

    private ZXingScannerQRcodeView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = -1;
    private FrameLayout fraCameraBase;
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";
    private ProgressDialog pleaseDialog;
    private ProgressDialog pleaseDialogCamera;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_qrcode, container, false);
    }

    @Override
    protected void initViews() {
        addControl();
        addEvent();
        init();
    }

    private void addEvent() {
    }

    private void addControl() {
        pleaseDialog = MyProgressDialog.newInstance(mContext, mContext.getResources().getString(R.string.Please));
        pleaseDialogCamera = MyProgressDialog.newInstance(mContext, mContext.getResources().getString(R.string.load_camera));
    }

    private void init() {
        fraCameraBase = getView().findViewById(R.id.fl_main_preview);
        mScannerView = new ZXingScannerQRcodeView(getActivity());
        mFlash = false;
        mAutoFocus = true;
        mSelectedIndices = null;
        mCameraId = -1;
        setupFormats();
        fraCameraBase.addView(mScannerView);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if (mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();
            for (int i = 0; i < ZXingScannerQRcodeView.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for (int index : mSelectedIndices) {
            formats.add(ZXingScannerQRcodeView.ALL_FORMATS.get(index));
        }
        if (mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    public void stopCamera() {
        if (mScannerView == null) return;
        mScannerView.stopCameraPreview();
    }

    public void resumeCamera() {
        if (mScannerView == null) return;
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        Log.d("onPause", "onPause");

    }

    @Override
    public void onStop() {
        super.onStop();
        mScannerView.stopCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void showLoadCamera() {
        pleaseDialogCamera.show();
    }

    public void hideLoadCamera() {
        pleaseDialogCamera.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();

        showLoadCamera();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mScannerView != null) {
                    mScannerView.setResultHandler(ScanQRcodeFragment.this);
                    mScannerView.startCamera(mCameraId);
                    mScannerView.setAutoFocus(mAutoFocus);
                    mScannerView.resumeCameraPreview(ScanQRcodeFragment.this);
                }
                hideLoadCamera();
            }
        }, 200);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
        outState.putInt(CAMERA_ID, mCameraId);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCameraSelected(int cameraId) {
        mCameraId = cameraId;
        mScannerView.startCamera(mCameraId);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void handleResult(Result rawResult) {

        postCode(rawResult.getText());

    }

    private void postCode(String code){
        pleaseDialog = MyProgressDialog.newInstance(getContext(), getContext().getResources().getString(R.string.Please));
        pleaseDialog.show();

        APIRegisterUser mAPIService = ApiUtils.getAPIService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
        jsonObject.addProperty(KeyConst.NUMBER_PHONE, PrefUtils.getString(getContext(), KeyConst.NUMBER_PHONE_STATISTIC));
        jsonObject.addProperty(KeyConst.QRCODE, code);

        mAPIService.postRawJSONCheckQRcode(jsonObject).enqueue(new Callback<CheckInQRcode>() {
            @Override
            public void onResponse(Call<CheckInQRcode> call, Response<CheckInQRcode> response) {
                if (response != null) {
                    pleaseDialog.dismiss();

                    if(response.body().getStatus()){
                        Intent intent = new Intent(getActivity(),WinSuccessActivity.class);
                        intent.putExtra(KeyConst.INTENT_PUT_GIFT_NAME,response.body().getGiftName());
                        startActivity(intent);
                        getFragmentManager().popBackStack();
                    }else {
                        Intent intent2 = new Intent(getActivity(),WinFailActivity.class);
                        intent2.putExtra(KeyConst.INTENT_PUT_GIFT_NAME,response.body().getGiftName());
                        startActivity(intent2);
                        getFragmentManager().popBackStack();

                    }


                }
            }

            @Override
            public void onFailure(Call<CheckInQRcode> call, Throwable t) {
                pleaseDialog.dismiss();
            }
        });

    }
}
