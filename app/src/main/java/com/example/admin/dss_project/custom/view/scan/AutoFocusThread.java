package com.example.admin.dss_project.custom.view.scan;

import android.util.Log;

import static com.example.admin.dss_project.activity.MainAppActivity.mCamera;

public class AutoFocusThread implements Runnable {

    private boolean isRun = true;

    public void terminate(){
        isRun = false;
    }

    @Override
    public void run() {
        while (isRun){
            try {
                if(mCamera != null){
                    Log.d("Autoxxxxx","focus");
                }
                Thread.sleep(Constant.LONG_TIME_DELAY_AUTO_FOCUS);
                mCamera.autoFocus(null);
            }catch (Exception e){

            }
        }
    }
}
