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
import android.widget.FrameLayout;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.activity.MainAppActivity;
import com.example.admin.dss_project.custom.view.MyProgressDialog;
import com.example.admin.dss_project.custom.view.scan.CameraPreview;
import com.example.admin.dss_project.fragment.BaseFragment;

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
                View sheetView = getActivity().getLayoutInflater().inflate(R.layout.dialog_enter_code, null);
                Button button = sheetView.findViewById(R.id.btn_show);
                mBottomSheetDialog.setCancelable(true);
                mBottomSheetDialog.setContentView(sheetView);
                mBottomSheetDialog.show();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ProgressDialog pleaseDialog = MyProgressDialog.newInstance(mContext, mContext.getResources().getString(R.string.Please));
                        pleaseDialog.show();
                    }
                });

                break;
        }
    }
}
