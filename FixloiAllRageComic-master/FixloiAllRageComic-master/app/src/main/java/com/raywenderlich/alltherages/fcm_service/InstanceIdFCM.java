package com.raywenderlich.alltherages.fcm_service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.raywenderlich.alltherages.utils.SharedPref;

/**
 * Created by tranh on 3/27/2017.
 */

public class InstanceIdFCM extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token= FirebaseInstanceId.getInstance().getToken();
        Log.e("aaaaa", String.format("onTokenRefresh: %s", token) );
        SharedPref.instance.putToken(token);
        FirebaseMessaging.getInstance().subscribeToTopic("user");

    }
}
