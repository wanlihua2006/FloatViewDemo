package com.qiku.floatviewdemo;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class FloatView extends View {
    private String TAG = "FloatView";
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;

    private WindowManager wm = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    private WindowManager.LayoutParams wmParams =  ((MyApplication)getContext().getApplicationContext()).getMywmParams();

    public FloatView(Context applicationContext) {
        super(applicationContext);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //get screen position
        x = event.getRawX();
        y = event.getRawY() - 25;
        Log.d(TAG,"Current position X: " + x +", Y" + y);
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //get view position
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                Log.d(TAG,"Start position mTouchStartX: " + mTouchStartX +", mTouchStartY" + mTouchStartY);
                break;
            case MotionEvent.ACTION_MOVE:
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                updateViewPosition();
                mTouchStartX=mTouchStartY=0;
                break;
        }
        return true;
    }


    public void updateViewPosition() {
        wmParams.x = (int) (x - mTouchStartX);
        wmParams.y = (int) (y - mTouchStartY);
        wm.updateViewLayout(this, wmParams);
    }
}
