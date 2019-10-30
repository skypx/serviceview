package com.example.myapplication.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.example.myapplication.R;

/**
 * show view in window
 */
public class ServiceView extends Service {

    private Context mContext;
    private WindowManager mWindowManager;
    private View mWindowView;
    private static final String CHANNEL_WHATEVER = "background-Service";
    private static final String TAG = "pxwen";
    private boolean isDebug = false;


    public ServiceView() {
    }

    /**
     * oncreate
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        setBackRunning();
        Log.d(TAG, "service oncreate");

        mContext = getBaseContext();
        if (isDebug) {
            createPopUpWindow();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * Android O 上面新加的限制，必须在service启动之后5秒内来开启一个通知，否则会产生ANR
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setBackRunning() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_WHATEVER,
                "ReverseSupportService start",
                NotificationManager.IMPORTANCE_LOW);

        NotificationManager manager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }

        Notification notification = new Notification.Builder(getApplicationContext(),
                CHANNEL_WHATEVER).build();
        startForeground(1, notification);
    }


    public void createPopUpWindow() {
        Log.d(TAG, "createPopUpWindow E");
        if (mContext == null) {
            return;
        }
        mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        mWindowView = LayoutInflater.from(mContext).inflate(R.layout.my_window_layout, null);
        WindowManager.LayoutParams params = setUpWindowPara(new WindowManager.LayoutParams());
        mWindowManager.addView(mWindowView, params);
        mWindowView.setVisibility(View.VISIBLE);
        Log.d(TAG, "createPopUpWindow X");
    }

    /**
     * 设置全屏
     * @param params
     * @return
     */
    protected WindowManager.LayoutParams setUpWindowPara(WindowManager.LayoutParams params) {

        params.type = (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT <= 24) ?
                WindowManager.LayoutParams.TYPE_TOAST : WindowManager.LayoutParams.TYPE_PHONE;
        params.format = PixelFormat.TRANSLUCENT;

        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
        params.flags = 17368856;
        params.dimAmount = -1f;
        params.gravity = 8388659;
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;//SvRvcManager.RvcWindowType;
        }
        params.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                WindowManager.LayoutParams.FLAG_FULLSCREEN;
        params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        params.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
        params.format = PixelFormat.TRANSLUCENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        return params;
    }

    protected void showNavigationBar(View view) {
        if (view != null) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    protected void hideNavigationBar(View view) {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; // hide nav bar

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            uiFlags |= 0x00001000;
        } else {
            uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        view.setSystemUiVisibility(uiFlags);
    }

    protected void disableTouchOnOverlay(WindowManager windowManager,View view) {
        WindowManager.LayoutParams params
                = (WindowManager.LayoutParams) view.getLayoutParams();
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                & ~WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        windowManager.updateViewLayout(view, params);
    }

    protected void enableTouchOnOverlay(WindowManager windowManager,View view) {
        WindowManager.LayoutParams params
                = (WindowManager.LayoutParams) view.getLayoutParams();
        params.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                & ~WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        windowManager.updateViewLayout(view, params);
    }

}
