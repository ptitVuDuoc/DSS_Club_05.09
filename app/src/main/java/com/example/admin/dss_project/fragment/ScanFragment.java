package com.example.admin.dss_project.fragment;

import android.content.Context;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.activity.MainAppActivity;
import com.example.admin.dss_project.custom.view.scan.CameraPreview;
import com.example.admin.dss_project.fragment.BaseFragment;

import static com.example.admin.dss_project.custom.view.scan.CameraSetup.getCameraInstance;
import static com.example.admin.dss_project.custom.view.scan.Constant.INT_CAMERA_MAX_FPS;
import static com.example.admin.dss_project.custom.view.scan.Constant.INT_CAMERA_MIN_FPS;
import static com.example.admin.dss_project.custom.view.scan.Constant.INT_CAMERA_ROTATE;


public class ScanFragment extends BaseFragment {

    private FrameLayout fraCameraBase;
    private Handler handlerCamera = null;
    private HandlerThread handlerThread = null;
    public Handler handlerMain = null;
    public CameraPreview mPreview = null;
    private MainAppActivity mainActivity = null;
    private Camera mCamera;

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
        initCamera();
    }

    private void addEvent() {
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
}
