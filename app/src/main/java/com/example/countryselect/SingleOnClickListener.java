package com.example.countryselect;

import android.os.SystemClock;
import android.view.View;

public abstract class SingleOnClickListener implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL=600;

    private long mLastClickTime;


    public abstract void onSingleClick(View v);

    @Override
    public final void onClick(View v) {
        long currentClickTime= SystemClock.uptimeMillis();
        long elapsedTime=currentClickTime-mLastClickTime;
        //有可能2次连击，也有可能3连击，保证mLastClickTime记录的总是上次click的时间
        mLastClickTime=currentClickTime;

        if(elapsedTime<=MIN_CLICK_INTERVAL)
            return;

        onSingleClick(v);
    }

}