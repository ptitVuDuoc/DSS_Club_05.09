package com.example.admin.dss_project.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.activity.HomeActivity;
import com.example.admin.dss_project.activity.MainAppActivity;
import com.example.admin.dss_project.custom.view.MyProgressDialog;
import com.example.admin.dss_project.custom.view.scan.CameraPreview;
import com.example.admin.dss_project.fragment.BaseFragment;
import com.example.admin.dss_project.model.Register;
import com.example.admin.dss_project.model.SeriInfo;
import com.example.admin.dss_project.retrofit.APIRegisterUser;
import com.example.admin.dss_project.retrofit.ApiUtils;
import com.example.admin.dss_project.ultility.KeyConst;
import com.example.admin.dss_project.ultility.PrefUtils;
import com.example.admin.dss_project.ultility.Statistic;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.admin.dss_project.custom.view.scan.CameraSetup.getCameraInstance;
import static com.example.admin.dss_project.custom.view.scan.Constant.INT_CAMERA_MAX_FPS;
import static com.example.admin.dss_project.custom.view.scan.Constant.INT_CAMERA_MIN_FPS;
import static com.example.admin.dss_project.custom.view.scan.Constant.INT_CAMERA_ROTATE;


public class ScanFragment extends BaseFragment implements View.OnClickListener {

    private FrameLayout fraCameraBase;
    private Handler handlerCamera = null;
    private HandlerThread handlerThread = null;
    public Handler handlerMain = null;
    public CameraPreview mPreview = null;
    private MainAppActivity mainActivity = null;
    private Camera mCamera;
    private BottomSheetDialog mBottomSheetDialog;
    private EditText txtSeri;
    private RelativeLayout btnRegiterSeri;
    private ProgressDialog pleaseDialog;
    private TextView txtCodeProduct;
    private TextView txtNumberSeria;
    private TextView txtReward;
    private TextView txtDateRegister;
    private TextView txtSocres;

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
//        initCamera();
    }

    private void addEvent() {
        findViewById(R.id.btn_enter_code).setOnClickListener(this);
    }

    private void addControl() {
    }

    private void init() {

        mainActivity = (MainAppActivity) getActivity();
        fraCameraBase = getView().findViewById(R.id.fl_main_preview);

        MainAppActivity.qrCodeFinderView = getView().findViewById(R.id.qr_code_view);
    }


    public void initCamera() {
        mCamera = getCameraInstance();
        setupCameraPreview();
        mPreview = new CameraPreview((MainAppActivity) getActivity(), mCamera);
        fraCameraBase.addView(mPreview);
        mCamera = getmCamera();
        MainAppActivity.mCamera = mCamera;
    }

    public Camera getmCamera() {
        return mCamera;
    }

    public boolean setupCameraPreview(){
        try {
            Camera.Parameters params = mCamera.getParameters();
            params.setPreviewFpsRange(INT_CAMERA_MIN_FPS,INT_CAMERA_MAX_FPS);
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            mCamera.setDisplayOrientation(INT_CAMERA_ROTATE);
            mCamera.setParameters(params);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_enter_code:

                mBottomSheetDialog = new BottomSheetDialog(getActivity());
                final View sheetView = getActivity().getLayoutInflater().inflate(R.layout.dialog_enter_code, null);
                btnRegiterSeri = sheetView.findViewById(R.id.btn_register);
                txtSeri = sheetView.findViewById(R.id.txt_seri);
                mBottomSheetDialog.setCancelable(true);
                mBottomSheetDialog.setContentView(sheetView);
                mBottomSheetDialog.show();
                btnRegiterSeri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickRegister(txtSeri.getText().toString(), sheetView);
                    }
                });

                break;
        }
    }

    private void clickRegister(String s, final View view) {

        pleaseDialog = MyProgressDialog.newInstance(mContext, mContext.getResources().getString(R.string.Please));
        pleaseDialog.show();

        if(s.isEmpty())return;

        APIRegisterUser mAPIService = ApiUtils.getAPIService();

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KeyConst.TOKEN, Statistic.token);
        jsonObject.addProperty(KeyConst.NUMBER_PHONE, PrefUtils.getString(getContext(),KeyConst.NUMBER_PHONE_STATISTIC));
        jsonObject.addProperty(KeyConst.SERI,s);

        mAPIService.postRawJSONRegisterSeria(jsonObject).enqueue(new Callback<SeriInfo>() {
            @Override
            public void onResponse(Call<SeriInfo> call, Response<SeriInfo> response) {
                if (response != null) {

                    if (response.body().getIsSuccess()) {

                        Toast.makeText(mainActivity, R.string.register_seria_success, Toast.LENGTH_SHORT).show();
                        view.findViewById(R.id.view_detail_register_seria).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.view_register).setVisibility(View.GONE);
                        setContentDetail(view,response.body());
                        ((MainAppActivity)getActivity()).updateSocres(response.body().getSoDiemHienTai().toString());

                        pleaseDialog.dismiss();

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

    private void setContentDetail(View view, SeriInfo seriInfo){
        txtCodeProduct = (TextView) view.findViewById(R.id.txt_code_product);
        txtNumberSeria = (TextView) view.findViewById(R.id.txt_number_seria);
        txtReward = (TextView) view.findViewById(R.id.code_reward);
        txtSocres = (TextView) view.findViewById(R.id.txt_scores);

        txtCodeProduct.setText(seriInfo.getMaHangHoa());
        txtNumberSeria.setText(seriInfo.getSeria());
        txtReward.setText(seriInfo.getMaSoDuThuong());
        txtSocres.setText(seriInfo.getSoDiem().toString()+" điểm");
    }
}
