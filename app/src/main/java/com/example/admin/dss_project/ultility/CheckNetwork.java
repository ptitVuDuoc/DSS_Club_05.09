package com.example.admin.dss_project.ultility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.admin.dss_project.activity.HomeActivity;

/**
 * Created by duoc.vuvan.sv on 7/14/2017.
 */

public class CheckNetwork {
//    public boolean isNetworkOnline() {
//        boolean status=false;
//        try{
//            ConnectivityManager cm = (ConnectivityManager) getSystemService(ManHinhChinhActivity.CONNECTIVITY_SERVICE);
//            NetworkInfo netInfo = cm.getNetworkInfo(0);
//            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
//                status= true;
//            }else {
//                netInfo = cm.getNetworkInfo(1);
//                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
//                    status= true;
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//            return false;
//        }
//        return status;

//    }
    public static boolean isNwConnected(Context context) {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(HomeActivity.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()== NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()== NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;

    }
}

