package com.example.admin.dss_project.custom.view.scan;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.example.admin.dss_project.R;

import static android.graphics.PixelFormat.OPAQUE;

public final class QrCodeFinderView extends ConstraintLayout {

    private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192, 128, 64 };
    private Context mContext;
    private Paint mPaint;
    private int mScannerAlpha;
    private int mMaskColor;
    private int mFrameColor;
    private int mLaserColor;
    private Rect mFrameRect;
    private int mFocusThick;
    private int mAngleThick;
    private int mAngleLength;

    public Rect getmFrameRect() {
        return mFrameRect;
    }

    public QrCodeFinderView(Context context) {
        this(context, null);
    }

    public QrCodeFinderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QrCodeFinderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mPaint = new Paint();
        Resources resources = getResources();
        mMaskColor = resources.getColor(R.color.qr_code_finder_mask);
        mFrameColor = resources.getColor(R.color.colorPrimary);
        mLaserColor = resources.getColor(R.color.colorAccent);
        mFocusThick = 1;
        mScannerAlpha = 0;
        mAngleThick = 8;
        mAngleLength = 40;
        init(context);
    }



    private void init(Context context) {
        setWillNotDraw(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        ConstraintLayout relativeLayout = (ConstraintLayout) inflater.inflate(R.layout.view_center_main, this);
        mFrameRect = new Rect();
        fixedFinder();
    }

    private void fixedFinder() {
        int[] sizes = CameraSetup.getScreenSize(getContext());
        int widthDm = sizes[1];
        int heightDm = sizes[0];
        if (widthDm > heightDm){
            widthDm = sizes[0];
            heightDm = sizes[1];
        }
        // default fixed
        if (widthDm < heightDm) {
            mFrameRect.left = widthDm / 6;
            mFrameRect.top = heightDm / 4;
            mFrameRect.right = widthDm - widthDm / 6;
            mFrameRect.bottom = heightDm - heightDm / 4;
        }else {
            mFrameRect.left = widthDm / 4;
            mFrameRect.top = heightDm / 6;
            mFrameRect.right = widthDm - widthDm / 4;
            mFrameRect.bottom = heightDm - heightDm / 6;
        }
        // fixed max size
        if (mFrameRect.width() > Constant.INT_FIXED_MAX_SIZE){
            int sizeDown = mFrameRect.width() - Constant.INT_FIXED_MAX_SIZE;
            mFrameRect.left = mFrameRect.left + sizeDown /2;
            mFrameRect.right = mFrameRect.right - sizeDown/2;
        }
        if (mFrameRect.height() > Constant.INT_FIXED_MAX_SIZE){
            int sizeDown = mFrameRect.height() - Constant.INT_FIXED_MAX_SIZE;
            mFrameRect.top = mFrameRect.top + sizeDown /2;
            mFrameRect.bottom = mFrameRect.bottom - sizeDown/2;
        }
        // fixed min size
        if (mFrameRect.width() < Constant.INT_FIXED_MIN_SIZE) {
            if (Constant.INT_FIXED_MIN_SIZE < widthDm) {
                int sizeUp = Constant.INT_FIXED_MIN_SIZE - mFrameRect.width();
                mFrameRect.left = mFrameRect.left - sizeUp / 2;
                mFrameRect.right = mFrameRect.right + sizeUp /2;
            }else {
                mFrameRect.left = 0;
                mFrameRect.right = widthDm;
            }
        }
        if (mFrameRect.height() < Constant.INT_FIXED_MIN_SIZE) {
            if (Constant.INT_FIXED_MIN_SIZE < heightDm) {
                int sizeUp = Constant.INT_FIXED_MIN_SIZE - mFrameRect.height();
                mFrameRect.top = mFrameRect.left - sizeUp / 2;
                mFrameRect.bottom = mFrameRect.right + sizeUp /2;
            }else {
                mFrameRect.top = 0;
                mFrameRect.bottom = heightDm;
            }
        }
    }


    @Override
    public void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            return;
        }
        Rect frame = mFrameRect;
        if (frame == null) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        mPaint.setColor(mMaskColor);
        canvas.drawRect(0, 0, width, frame.top, mPaint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, mPaint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, mPaint);
        canvas.drawRect(0, frame.bottom + 1, width, height, mPaint);

        // draw Rect

        drawFocusRect(canvas, frame);

        // draw Angle

        drawAngle(canvas, frame);

        // draw Laser

        drawLaser(canvas, frame);

    }


    private void drawFocusRect(Canvas canvas, Rect rect) {
        mPaint.setColor(mFrameColor);
        //Up
        canvas.drawRect(rect.left , rect.top, rect.right , rect.top + mFocusThick, mPaint);
        //Left
        canvas.drawRect(rect.left, rect.top , rect.left + mFocusThick, rect.bottom ,
                mPaint);
        //Right
        canvas.drawRect(rect.right - mFocusThick, rect.top , rect.right, rect.bottom ,
                mPaint);
        //Down
        canvas.drawRect(rect.left , rect.bottom - mFocusThick, rect.right , rect.bottom,
                mPaint);
    }

    /**
     * Draw four purple angles
     *
     * @param canvas
     * @param rect
     */

    private void drawLaser(Canvas canvas, Rect rect) {
        mPaint.setColor(mLaserColor);
        mPaint.setAlpha(SCANNER_ALPHA[mScannerAlpha]);
        mScannerAlpha = (mScannerAlpha + 1) % SCANNER_ALPHA.length;
        int middle = rect.height() / 2 + rect.top;
        Rect rectL  = new Rect(rect.left ,middle - 1 ,rect.right, middle + 2);
        canvas.drawRect(rectL, mPaint);
        // delay draw repeat
        postInvalidateDelayed(Constant.LONG_TIME_DELAY_LASER,rectL.left,rectL.top,rectL.right,rectL.bottom);
    }


    private void drawAngle(Canvas canvas, Rect rect) {
        mPaint.setColor(mLaserColor);
        mPaint.setAlpha(OPAQUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mAngleThick);
        int left = rect.left;
        int top = rect.top;
        int right = rect.right;
        int bottom = rect.bottom;
        // Top left angle
        canvas.drawRect(left, top, left + mAngleLength, top + mAngleThick, mPaint);
        canvas.drawRect(left, top, left + mAngleThick, top + mAngleLength, mPaint);
        // Top right angle
        canvas.drawRect(right - mAngleLength, top, right, top + mAngleThick, mPaint);
        canvas.drawRect(right - mAngleThick, top, right, top + mAngleLength, mPaint);
        // bottom left angle
        canvas.drawRect(left, bottom - mAngleLength, left + mAngleThick, bottom, mPaint);
        canvas.drawRect(left, bottom - mAngleThick, left + mAngleLength, bottom, mPaint);
        // bottom right angle
        canvas.drawRect(right - mAngleLength, bottom - mAngleThick, right, bottom, mPaint);
        canvas.drawRect(right - mAngleThick, bottom - mAngleLength, right, bottom, mPaint);
    }

}
