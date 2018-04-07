package com.raywenderlich.alltherages;

import android.app.Application;

import com.raywenderlich.alltherages.database.DBContext;
import com.raywenderlich.alltherages.utils.SharedPref;

/**
 * Created by laptopTCC on 6/8/2017.
 */

public class Myapp extends Application {
    private static String TAG = Application.class.toString();
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPref.setInstance(this);
        DBContext.setInstance(this);
        //Log.d(TAG,String.format("DBContext singleOrder: %s",DBContext.instance.getAllSingleOrder().toString()));
    }
}
