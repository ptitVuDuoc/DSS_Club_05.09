package com.example.admin.dss_project.custom.view.scan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.admin.dss_project.activity.MainAppActivity;
import com.example.admin.dss_project.model.ImageBarCode;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ForkJoinPool;

import static com.example.admin.dss_project.custom.view.scan.Constant.INT_IMAGE_MAX_SIZE;
import static com.example.admin.dss_project.custom.view.scan.Constant.INT_IMAGE_SIZE;
import static com.example.admin.dss_project.custom.view.scan.Constant.INT_RESULT_WHAT;
import static com.example.admin.dss_project.custom.view.scan.Constant.LONG_TIME_DELAY_AUTO_FOCUS;
import static com.example.admin.dss_project.custom.view.scan.Constant.STRING_CAMERA_KEY_COMPLETE;
import static com.example.admin.dss_project.custom.view.scan.Constant.STRING_DATE_TIME_FORMART;

public class CameraPreview extends SurfaceView implements Camera.PreviewCallback, SurfaceHolder.Callback {

    private static final int INT_STEP_ONE = 1;

    private static final int INT_STEP_TWO = 2;
    private static Thread thread;

    private SurfaceHolder mHolder = null;

    private MainAppActivity MainAppActivity = null;

    public int width = 0;

    public int height = 0;

    public BarcodeDetector detector = null;

    public boolean isOpen = true;

    public static int previewWidth = 0;

    public static int previewHeight = 0;

    public int dmWidth = 0;

    public int dmHeight = 0;

    public static long count = 0;
    private ForkJoinPool poolExecutor;
    private Camera mCamera;

    public static AutoFocusThread autoFocusThread ;

    public CameraPreview(MainAppActivity activity, Camera camera) {
        super(activity);
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        this.mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.MainAppActivity = activity;
        // setup barcode with full options
        detector =
                new BarcodeDetector.Builder(getContext())
                        .setBarcodeFormats(Barcode.ALL_FORMATS)
                        .build();
        int[] sizes = CameraSetup.getScreenSize(getContext());
        dmWidth = sizes[0];
        dmHeight = sizes[1];
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            mCamera.setPreviewCallback(CameraPreview.this);
            isOpen = true;
            // config auto focus Camera all version with times
            initAutoFocus2();
        } catch (Exception e) {

        }
    }


    public void surfaceDestroyed(SurfaceHolder holder) {
//        isOpen = false;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        try {
            ++count;
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            mCamera.setPreviewCallback(CameraPreview.this);
            previewHeight = mCamera.getParameters().getPreviewSize().height;
            previewWidth = mCamera.getParameters().getPreviewSize().width;
            initAutoFocus2();
        } catch (Exception e) {

        }
//        CameraSetup.hideLoading(getContext());
    }


    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        try {
            processData(bytes, camera);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processData(byte[] bytes, Camera camera) {
        Log.d("xxxxxxxxxxxx", "0000000");
        height = camera.getParameters().getPreviewSize().height;
        width = camera.getParameters().getPreviewSize().width;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YuvImage yuvImage = new YuvImage(bytes, ImageFormat.NV21, width, height, null);
        Rect mainRect = new Rect(MainAppActivity.qrCodeFinderView.getmFrameRect());
        if (width > height) {
            int tmp = height;
            height = width;
            width = tmp;
        }
        float dx = width * 1f / dmWidth;
        float dy = height * 1f / dmHeight;
        int fixWidth = (int) (mainRect.width() * dx);
        int fixHeight = (int) (mainRect.height() * dy);
        int pointCenterX = width / 2;
        int pointCenterY = height / 2;
        Rect rect = new Rect();
        rect.top = pointCenterX - fixWidth / 2;
        rect.bottom = pointCenterX + fixWidth / 2;
        rect.left = pointCenterY - fixHeight / 2;
        rect.right = pointCenterY + fixHeight / 2;
        if (height < dmHeight || width < dmWidth) {
            yuvImage.compressToJpeg(rect, INT_IMAGE_MAX_SIZE, out);
        } else {
            yuvImage.compressToJpeg(rect, INT_IMAGE_SIZE, out);
        }
        byte[] imageBytes = out.toByteArray();
        Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        Bitmap buffer = image.copy(image.getConfig(), true);
        image = toGrayscale(image);
        if (image == null) {
            image = buffer;
        }
        scanBitmap(image, INT_STEP_TWO, imageBytes);
        Log.d("xxxxxxxxxxxx", "00000001111");
    }


    public static void initAutoFocus2() {
        stopAuto();
        autoFocusThread = new AutoFocusThread();
        thread = new Thread(autoFocusThread);
        thread.start();
    }

    private static void stopAuto() {
        if (thread != null && thread.isAlive()){
            try {
                autoFocusThread.terminate();
                thread.join();
                thread = null;
                autoFocusThread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    private boolean scanBitmap(Bitmap image, int count, byte[] arrays) {
        Frame frame = new Frame.Builder().setBitmap(image).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);
        // block if false
        if (barcodes.size() > 0) {
            Barcode thisCode = barcodes.valueAt(0);
            Bundle bundle = new Bundle();
            Message message = new Message();
            Date dNow = new Date();
            SimpleDateFormat dateFormat =
                    new SimpleDateFormat(STRING_DATE_TIME_FORMART);
            ImageBarCode imageBarCode = new ImageBarCode
                    (null, thisCode.rawValue, Base64.encodeToString(arrays, Base64.CRLF),
                            dateFormat.format(dNow));
            bundle.putSerializable(STRING_CAMERA_KEY_COMPLETE, imageBarCode);
            message.setData(bundle);
            message.what = INT_RESULT_WHAT;
//                MainAppActivity.handler.sendMessage(message);
//                ++count;

            Log.d("mmmmmmmmmm", "" + imageBarCode.getData());
            return true;
        }
        return false;
    }


    public void initAutoFocus() {
        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while (isOpen) {
                    try {
                        Thread.sleep(LONG_TIME_DELAY_AUTO_FOCUS);
                        Log.d("Autoxxxxxxxxxxxxx :", "focus");
                        mCamera.autoFocus(null);
                    } catch (Exception e) {

                    }

                }
            }
        });
    }

}