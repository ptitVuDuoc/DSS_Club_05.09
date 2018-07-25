package com.dss.dssClub.custom.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class MyCusttomtext extends TextView {
    public MyCusttomtext(Context context) {
        super(context);
        init();
    }

    public MyCusttomtext(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyCusttomtext(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public MyCusttomtext(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Roboto.ttf");
        setTypeface(tf, Typeface.NORMAL);
    }
}
