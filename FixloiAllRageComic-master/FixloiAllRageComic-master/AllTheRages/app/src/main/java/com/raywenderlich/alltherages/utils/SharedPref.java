package com.raywenderlich.alltherages.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.raywenderlich.alltherages.networks.jsonmodels.UserRegisterResponseJson;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by laptopTCC on 5/27/2017.
 */

public class SharedPref {
    private final String TAG = SharedPref.class.toString();
    private final String KEY_PREF = "ragecomic";
    private final String NAME = "name"; //ten that: gom ho va ten
    private final String USER = "user"; //username su dung trong db mlab: co the la email, sdt, facebook.
    private final String EMAIL = "email";
    private final String FACEBOOK = "facebook";
    private final String PHONE_NUMBER = "phone_number";
    private final String TOKEN = "token";
    private final String URL_AVATAR = "url_avatar";
    private final String COUNT = "count";
    private final String PASSWORD = "password";
    private final String DESCRIPTION = "description";//mo ta cua ragecomic
    private final String URL_RAGE_PIC = "url_rage_pic";//hinh anh cau ragecomic.
    private final String TOTAL_SPEND = "totalspend";
    private SharedPreferences sharedPreferences;
    Gson gson ;
    public SharedPreferences.Editor editor;
    public static SharedPref instance;

    public static void setInstance(Context context) {
        instance = new SharedPref(context);
    }

    private SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences(
                KEY_PREF, Context.MODE_PRIVATE
        );

        gson = new Gson();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void cleanAll() {
        String s = SharedPref.instance.getToken();
        String phone = SharedPref.instance.getPhoneNumber();
        sharedPreferences.edit().clear().apply();
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
        //AccountKit.logOut();
        sharedPreferences.edit().putString(TOKEN, s).apply();
        sharedPreferences.edit().putString(PHONE_NUMBER, phone).apply();
    }

    public String getToken() {
        return TOKEN;
    }

    public String getName() {
        return sharedPreferences.getString(NAME, null);
    }

    public String getUser() {
        return sharedPreferences.getString(USER, null);
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL, null);
    }

    public String getFacebook() {
        return sharedPreferences.getString(FACEBOOK, null);
    }

    public String getPhoneNumber() {
        return sharedPreferences.getString(PHONE_NUMBER, null);
    }

    public int getCount() {
        return sharedPreferences.getInt(COUNT, 0);
    }

    public String getPassword() {
        return sharedPreferences.getString(PASSWORD, null);
    }

    public String getDescription() {
        return sharedPreferences.getString(DESCRIPTION, null);
    }

    public String getUrlAvatar() {
        return sharedPreferences.getString(URL_AVATAR, null);
    }

    public String getUrlRagePic() {
        return sharedPreferences.getString(URL_RAGE_PIC, null);
    }

    public float getTotalspend() {return sharedPreferences.getFloat(TOTAL_SPEND, Float.parseFloat(null));}
    ///put


    public void putName(String name) {
        sharedPreferences.edit().putString(NAME, name).apply();
    }

    public void putUser(String user) {
        sharedPreferences.edit().putString(USER, user).apply();
    }

    public void putFacebook(String facebook) {
        sharedPreferences.edit().putString(FACEBOOK, facebook).apply();
    }

    public void putEmail(String email) {
        sharedPreferences.edit().putString(EMAIL, email).apply();
    }


    public void putUrlAvatar(String urlAvatar) {
        sharedPreferences.edit().putString(URL_AVATAR, urlAvatar).apply();
    }

    public void putUrlRagePic(String urlRagePic) {
        sharedPreferences.edit().putString(URL_RAGE_PIC, urlRagePic).apply();
    }

    public void putDescription(String description) {
        sharedPreferences.edit().putString(DESCRIPTION, description).apply();
    }

    public void putToken(String token) {
        sharedPreferences.edit().putString(TOKEN, token).apply();
    }

    public void putPhoneNumber(String phoneNumber) {
        sharedPreferences.edit().putString(PHONE_NUMBER, phoneNumber).apply();
    }

    public void putPassword(String password) {
        sharedPreferences.edit().putString(PASSWORD, password).apply();
    }

    public void putCount(int count) {
        sharedPreferences.edit().putInt(COUNT, count).apply();
    }

    public void putTotalSpend(Float totalSpend) {sharedPreferences.edit().putFloat(TOTAL_SPEND, totalSpend).apply();}


    public String getAccessToken() {
        if (getLoginCredential() != null)
        {
            return getLoginCredential().getToken();
        }
        return null;
    }

    private UserRegisterResponseJson getLoginCredential() {
        String loginJson = sharedPreferences.getString(TOKEN, null);
        if (loginJson == null)
        {
            return null;
        }
        //Convert json to java object
        UserRegisterResponseJson loginCredential  = gson.fromJson(loginJson, UserRegisterResponseJson.class);

        return  loginCredential;
    }

    public void put(UserRegisterResponseJson loginCredential){
        String loginJson = gson.toJson(loginCredential);
        sharedPreferences.edit().putString(TOKEN, loginJson).commit();
    }
}
