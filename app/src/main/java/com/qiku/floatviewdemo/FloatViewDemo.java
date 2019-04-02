package com.qiku.floatviewdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

public class FloatViewDemo extends AppCompatActivity {
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams params;
    private FloatView mLayout;
    private final int REQUEST_READ_PHONE_STATE = 12;
    private final int REQUEST_PERMISSION_CODE = 10;
    private  boolean  isStart = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_view_demo);

       if(checkPermission(FloatViewDemo.this)) {

       } else {
           Toast.makeText(FloatViewDemo.this, "此应用未授予覆盖其他app上层的权限！！！", Toast.LENGTH_SHORT);
       }
 /*       if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SYSTEM_ALERT_WINDOW);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(FloatViewDemo.this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, REQUEST_READ_PHONE_STATE);
                Log.d("FloatViewDemo","wanlihua debug request permission : " + REQUEST_READ_PHONE_STATE);
            } else {
                Toast.makeText(FloatViewDemo.this,
                        "此应用需要覆盖其他app上层的权限！！！",
                        Toast.LENGTH_SHORT).show();
            }
          }*/
    }



    private void showView() {
        mLayout = new FloatView(getApplicationContext());
        mLayout.setBackgroundResource(R.drawable.ic_qq);
        //get windowManager
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        params = ((MyApplication)getApplication()).getMywmParams();

        //params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){//6.0+
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            params.type =  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        params.format = 1;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                |WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        params.alpha = 1.0f;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;
        params.width=140;
        params.height=140;

        mWindowManager.addView(mLayout, params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLayout!=null)
            mWindowManager.removeView(mLayout);
    }

/*    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                    Log.d("FloatViewDemo","wanlihua debug PERMISSION_GRANTED");
                }
                break;

            default:
                break;
        }
    }*/


    public boolean checkPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !Settings.canDrawOverlays(activity)) {
            Toast.makeText(activity, "此应用需要覆盖其他app上层的权限，请授权", Toast.LENGTH_SHORT).show();
            activity.startActivityForResult(
                    new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + activity.getPackageName())), REQUEST_PERMISSION_CODE);
            return false;
        }
        showView();
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (!Settings.canDrawOverlays(FloatViewDemo.this)) {
                // SYSTEM_ALERT_WINDOW permission not granted...
                Toast.makeText(FloatViewDemo.this, "此应用未授予覆盖其他app上层的权限！！！", Toast.LENGTH_SHORT);
            }else{
                showView();
            }
        }
    }

}
