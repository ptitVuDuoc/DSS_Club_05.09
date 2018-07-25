package com.dss.dssClub.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dss.dssClub.R;
import com.dss.dssClub.activity.HomeActivity;
import com.dss.dssClub.activity.MainAppActivity;
import com.dss.dssClub.activity.RegisterSeriaActivity;
import com.dss.dssClub.custom.view.MyProgressDialog;
import com.dss.dssClub.model.SeriInfo;
import com.dss.dssClub.retrofit.APIRegisterUser;
import com.dss.dssClub.retrofit.ApiUtils;
import com.dss.dssClub.scan.ultility.CameraSelectorDialogFragment;
import com.dss.dssClub.scan.ultility.ZXingScannerView;
import com.dss.dssClub.ultility.KeyConst;
import com.dss.dssClub.ultility.PrefUtils;
import com.dss.dssClub.ultility.Statistic;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanFragment extends BaseFragment implements View.OnClickListener,
        ZXingScannerView.ResultHandler,
        CameraSelectorDialogFragment.CameraSelectorDialogListener {

    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";
    private MainAppActivity mainActivity = null;
    private BottomSheetDialog mBottomSheetDialog;
    private EditText txtSeri;
    private TextView btnRegiterSeri;
    private ProgressDialog pleaseDialog;
    private ProgressDialog pleaseDialogCamera;
    private TextView txtCodeProduct;
    private TextView txtNumberSeria;
    private TextView txtReward;
    private TextView txtDateRegister;
    private TextView txtSocres;
    private View sheetView;
    private ZXingScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = -1;
    private FrameLayout fraCameraBase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }

    @Override
    protected void initViews() {
        addControl();
        addEvent();

        init();

        Statistic.initDataList();

    }

    private void init() {
        fraCameraBase = getView().findViewById(R.id.fl_main_preview);
        mScannerView = new ZXingScannerView(getActivity());
        mFlash = false;
        mAutoFocus = true;
        mSelectedIndices = null;
        mCameraId = -1;
        setupFormats();
        fraCameraBase.addView(mScannerView);
        mScannerView.setAutoFocus(mAutoFocus);
    }


    private void addEvent() {
        findViewById(R.id.btn_enter_code).setOnClickListener(this);
    }

    private void addControl() {

        pleaseDialog = MyProgressDialog.newInstance(mContext, mContext.getResources().getString(R.string.Please));
        pleaseDialogCamera = MyProgressDialog.newInstance(mContext, mContext.getResources().getString(R.string.load_camera));
    }

    public void showLoadCamera() {
        pleaseDialogCamera.show();
    }

    public void hideLoadCamera() {
        pleaseDialogCamera.dismiss();
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
    public void handleResult(Result rawResult) {
//        try {
//            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
//            r.play();
//        } catch (Exception e) {}
        Intent intent = new Intent(getContext(), RegisterSeriaActivity.class);
        intent.putExtra(KeyConst.BUNLDE_CODE_SERIA,rawResult.getText());
        startActivity(intent);
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if (mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();
            for (int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for (int index : mSelectedIndices) {
            formats.add(ZXingScannerView.ALL_FORMATS.get(index));
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

    @Override
    public void onResume() {
        super.onResume();

        showLoadCamera();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mScannerView != null) {
                    mScannerView.setResultHandler(ScanFragment.this);
                    mScannerView.startCamera(mCameraId);
                    mScannerView.setAutoFocus(mAutoFocus);
                    mScannerView.resumeCameraPreview(ScanFragment.this);
                }
                hideLoadCamera();
            }
        }, 200);
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                if(mScannerView != null){
//                    mScannerView.setResultHandler(ScanFragment.this);
//                    mScannerView.startCamera(mCameraId);
//                    mScannerView.setAutoFocus(mAutoFocus);
//                    mScannerView.resumeCameraPreview(ScanFragment.this);
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                hideLoadCamera();
//            }
//        }.execute();
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setHasOptionsMenu(true);
    }


    private void clickCloseDialog() {
        mBottomSheetDialog.dismiss();

        if (mScannerView == null) return;
        mScannerView.setResultHandler(this);
        mScannerView.setAutoFocus(mAutoFocus);
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_enter_code:
                Intent intent = new Intent(getContext(), RegisterSeriaActivity.class);
                intent.putExtra(KeyConst.BUNLDE_CODE_SERIA,"");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCameraSelected(int cameraId) {
        mCameraId = cameraId;
        mScannerView.startCamera(mCameraId);
        mScannerView.setAutoFocus(mAutoFocus);
    }
}
