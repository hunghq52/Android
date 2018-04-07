package com.raywenderlich.alltherages.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.raywenderlich.alltherages.LoginActivity;
import com.raywenderlich.alltherages.R;
import com.raywenderlich.alltherages.eventbus.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import jp.co.recruit_lifestyle.android.floatingview.FloatingViewListener;

import static com.facebook.GraphRequest.TAG;
/**
 * Created by hanhi on 4/11/2017.
 */

public class MessageService extends Service implements FloatingViewListener {
    private WindowManager mWindowManager;
    private ImageView mImgFloatingView;
    private boolean mIsFloatingViewAttached = false;


    @Override
    public void onCreate() {
        super.onCreate();
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        mImgFloatingView = new ImageView(this);
        mImgFloatingView.setImageResource(R.mipmap.ic_messenger);
        //mImgFloatingView.setBackgroundColor(Color.BLUE);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER | Gravity.LEFT;

        mWindowManager.addView(mImgFloatingView, params);

        mImgFloatingView.setVisibility(View.VISIBLE);
        mIsFloatingViewAttached = true;

        mImgFloatingView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private int countMove;
            private int countUp;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        countUp ++;
                        Log.d(TAG, "onTouch: ");
                        if (countUp  == 1 &&countMove<3) {
                            EventBus.getDefault().post(new MessageEvent());
                            removeView();
                        }else {
                            countUp = 0;
                            countMove =0;
                        }
                        
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        countMove++;
                        Log.d(TAG, "onTouch: hhhh");
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mImgFloatingView, params);

                        return true;
                    case MotionEvent.ACTION_BUTTON_RELEASE:
                        Log.d(TAG, "onTouch: ffffffffff");
                        return true;
                    case MotionEvent.BUTTON_TERTIARY:
                        Log.d(TAG, "onTouch: gggg");
                        return true;
                }
                return false;
            }
        });


        mImgFloatingView.setClickable(true);
        mImgFloatingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void removeView() {
        if (mImgFloatingView != null) {
            mWindowManager.removeView(mImgFloatingView);
            mIsFloatingViewAttached = false;
        }
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), "onDestroy", Toast.LENGTH_SHORT);
        super.onDestroy();
        removeView();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mIsFloatingViewAttached) {
            mWindowManager.addView(mImgFloatingView, mImgFloatingView.getLayoutParams());
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onFinishFloatingView() {
        stopSelf();
    }

    @Override
    public void onTouchFinished(boolean isFinishing, int x, int y) {
        Log.d("a", "onTouchFinished: ");
    }
}
