package com.example.admin.dss_project.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.activity.HomeActivity;
import com.example.admin.dss_project.activity.MainAppActivity;
import com.example.admin.dss_project.custom.view.MyProgressDialog;
import com.example.admin.dss_project.model.SeriInfo;
import com.example.admin.dss_project.retrofit.APIRegisterUser;
import com.example.admin.dss_project.retrofit.ApiUtils;
import com.example.admin.dss_project.scan.ultility.CameraSelectorDialogFragment;
import com.example.admin.dss_project.scan.ultility.ZXingScannerView;
import com.example.admin.dss_project.ultility.KeyConst;
import com.example.admin.dss_project.ultility.PrefUtils;
import com.example.admin.dss_project.ultility.Statistic;
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
    private RelativeLayout btnRegiterSeri;
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
        showDialogEnterCode(rawResult.getText());
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
        if(mScannerView == null)return;
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
                if(mScannerView != null){
                    mScannerView.setResultHandler(ScanFragment.this);
                    mScannerView.startCamera(mCameraId);
                    mScannerView.setAutoFocus(mAutoFocus);
                    mScannerView.resumeCameraPreview(ScanFragment.this);
                }
                hideLoadCamera();
            }
        },200);
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

    public void showDialogEnterCode(String code) {

        mBottomSheetDialog = new BottomSheetDialog(getActivity());
        sheetView = getActivity().getLayoutInflater().inflate(R.layout.dialog_enter_code, null);
        btnRegiterSeri = sheetView.findViewById(R.id.btn_register);
        txtSeri = sheetView.findViewById(R.id.txt_seri);
        mBottomSheetDialog.setCancelable(false);
        mBottomSheetDialog.setContentView(sheetView);

        if (code != null) {
            txtSeri.setText(code);
        }

        mBottomSheetDialog.show();
        btnRegiterSeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickRegister(txtSeri.getText().toString(), sheetView);
            }
        });
        sheetView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCloseDialog();
            }
        });

        sheetView.findViewById(R.id.btn_close_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCloseDialog();
            }
        });
    }

    private void clickCloseDialog() {
        mBottomSheetDialog.dismiss();

        if(mScannerView == null) return;
        mScannerView.setResultHandler(this);
        mScannerView.setAutoFocus(mAutoFocus);
        mScannerView.resumeCameraPreview(this    );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_enter_code:
                mScannerView.stopCameraPreview();
                if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing()) return;
                showDialogEnterCode(null);
                break;
        }
    }

    private void clickRegister(String s, final View view) {

        pleaseDialog.show();

        if (s.isEmpty()) return;

        APIRegisterUser mAPIService = ApiUtils.getAPIService();

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
        jsonObject.addProperty(KeyConst.NUMBER_PHONE, PrefUtils.getString(getContext(), KeyConst.NUMBER_PHONE_STATISTIC));
        jsonObject.addProperty(KeyConst.SERI, s);

        mAPIService.postRawJSONRegisterSeria(jsonObject).enqueue(new Callback<SeriInfo>() {
            @Override
            public void onResponse(Call<SeriInfo> call, Response<SeriInfo> response) {
                if (response != null) {

                    pleaseDialog.dismiss();

                    if (response.body().getIsSuccess()) {

                        Toast.makeText(getContext(), R.string.register_seria_success, Toast.LENGTH_SHORT).show();
                        view.findViewById(R.id.view_detail_register_seria).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.view_register).setVisibility(View.GONE);
                        setContentDetail(view, response.body());
                        ((MainAppActivity) getActivity()).updateSocres(response.body().getSoDiemHienTai().toString());

                    } else {
                        String mess = response.body().getMessage();
                        ((MainAppActivity) getActivity()).showDialog(HomeActivity.ERROR, getString(R.string.register_seria_fail), mess, getContext());
                    }
                }
            }

            @Override
            public void onFailure(Call<SeriInfo> call, Throwable t) {
                pleaseDialog.dismiss();
            }
        });
    }

    private void setContentDetail(View view, SeriInfo seriInfo) {
        txtCodeProduct = (TextView) view.findViewById(R.id.txt_code_product);
        txtNumberSeria = (TextView) view.findViewById(R.id.txt_number_seria);
        txtReward = (TextView) view.findViewById(R.id.code_reward);
        txtSocres = (TextView) view.findViewById(R.id.txt_scores);

        txtCodeProduct.setText(seriInfo.getMaHangHoa());
        txtNumberSeria.setText(seriInfo.getSeria());
        txtReward.setText(seriInfo.getMaSoDuThuong());
        txtSocres.setText(seriInfo.getSoDiem().toString() + " điểm");
    }

    @Override
    public void onCameraSelected(int cameraId) {
        mCameraId = cameraId;
        mScannerView.startCamera(mCameraId);
        mScannerView.setAutoFocus(mAutoFocus);
    }
}
