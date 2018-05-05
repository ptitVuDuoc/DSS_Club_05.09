package com.example.admin.dss_project.custom.view.scan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.util.SparseArray;

import com.example.admin.dss_project.model.ImageBarCode;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CameraSetup {

    public static final int INT_STEP_TWO = 2;

    public static ThreadPoolExecutor poolExecutor = null;

    public static final int INT_STEP_ONE = 1;


    public static void initThreadPool() {
        poolExecutor = new ThreadPoolExecutor(Constant.INT_NUMBER_OF_CORES,Constant.INT_NUMBER_OF_CORES,Constant.INT_MAX_TIME_LIVE, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /*
    *  Setup camera with min and max FPS = [30000,30000]
    *
    *  Setup display using rotate default = 90
    * */


    public static int[] getScreenSize(Context context) {
        int width = context.getResources().getSystem().getDisplayMetrics().widthPixels;
        int height = context.getResources().getSystem().getDisplayMetrics().heightPixels;
        return new int[]{width, height};
    }

    public static void pauseCamera(Camera camera){
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
        }
    }

    public static Camera resumeCamera(Camera camera, Camera.PreviewCallback previewCallback){
        try {
            camera.startPreview();
            camera.setPreviewCallback(previewCallback);
        }catch (Exception e){
            try {
                camera.reconnect();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return camera;
    }

    public static boolean scanBitmap(Bitmap image, int count, byte[] arrays,Context context){
        Frame frame = new Frame.Builder().setBitmap(image).build();
        BarcodeDetector detector = new BarcodeDetector.Builder(context)
                            .setBarcodeFormats
                                    (Barcode.ALL_FORMATS)
                            .build();
        SparseArray<Barcode> barcodes = detector.detect(frame);
        if (count == INT_STEP_ONE || count == INT_STEP_TWO) {
            // block if false
            if (barcodes.size() > 0) {
                Barcode thisCode = barcodes.valueAt(0);
                Bundle bundle = new Bundle();
                Message message = new Message();
                Date dNow = new Date( );
                SimpleDateFormat dateFormat =
                        new SimpleDateFormat(Constant.STRING_DATE_TIME_FORMART, Locale.getDefault());
                ImageBarCode imageBarCode = new ImageBarCode
                        (null,thisCode.rawValue, Base64.encodeToString(arrays, Base64.CRLF),
                                dateFormat.format(dNow));
                bundle.putSerializable(Constant.STRING_CAMERA_KEY_COMPLETE,imageBarCode);
                message.setData(bundle);
                message.what = Constant.INT_RESULT_WHAT;
//                if (MainActivity.handler != null) {
//                    MainActivity.handler.sendMessage(message);
//                }
                ++count;
                return  true;
            }
        }
        return false;
    }

    public static Bitmap setImgResult(String data){
        byte[] imageBytes = Base64.decode(data,Base64.CRLF);
        Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        image = Bitmap.createScaledBitmap(image, image.getWidth()>>1, image.getHeight()>>1, false);
        return image;
    }

    public static byte[] bitmapToBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, Constant.INT_IMAGE_SIZE, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static void showLoading(Context context){
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage(context.getResources().getString(R.string.pdl_loading));
//        progressDialog.show();
    }

    public static void hideLoading(Context context){
//        if (progressDialog != null) {
//            if (progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
//        }
    }

}
