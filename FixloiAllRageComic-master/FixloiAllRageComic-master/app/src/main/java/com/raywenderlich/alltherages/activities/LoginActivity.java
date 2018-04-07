package com.raywenderlich.alltherages.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.raywenderlich.alltherages.R;
import com.raywenderlich.alltherages.database.DBContext;
import com.raywenderlich.alltherages.networks.NetContext;
import com.raywenderlich.alltherages.networks.jsonmodels.Token;
import com.raywenderlich.alltherages.networks.jsonmodels.UserRegisterResponseJson;
import com.raywenderlich.alltherages.networks.services.UserService;
import com.raywenderlich.alltherages.utils.SharedPref;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Facebook
//Google

/**
 * Created by laptopTCC on 6/4/2017.
 */

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 007;
    private final int APP_REQUEST_CODE = 99;
    public static String TAG = LoginActivity.class.toString();
    @BindView(R.id.tv_slogan)
    TextView tv_slogan;
    @BindView(R.id.tv_appname)
    TextView tv_appname;
    @BindView(R.id.bt_loginGG)
    SignInButton btLoginGG;
    @BindView(R.id.bt_loginPhone)
    Button btPhone;
    Dialog dialogPhone;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private LoginButton bt_loginFacebook;
    private CallbackManager callbackManager;
    private String username;
    private String password;
    private String accessToken;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        dialogPhone = new Dialog(this);
        dialogPhone.setContentView(R.layout.dialog_phone_enter);
        dialogPhone.setTitle("UPDATE PHONE");

        bt_loginFacebook = (LoginButton) findViewById(R.id.bt_loginFaceBook);
        callbackManager = CallbackManager.Factory.create();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Đang đăng nhập");
        progressDialog.setMessage("Vui lòng đợi...");
        progressDialog.setCancelable(false);
        AccountKit.initialize(this);
        if(!isGooglePlayServicesAvailable(this)){
            Log.d(TAG,"onCreate: Không có google");
        }
        //Khởi tạo instance cho sharedPref và DBContext.

//        Log.d(TAG, String.format("onCreate: %s", SharedPref.instance.getUser()));
        //Nếu chưa đăng nhập (SharedPref.getName() null) thì đăng nhập qua firebaseAuth.
        //Nếu đăng nhập rồi thì lôi thông tin từ SharedPref.
        printKeyHash(this);
        Log.d(TAG,String.format("Check username: %s",SharedPref.instance.getUser()));
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    Log.e(TAG,String.format("onAuthStateChanged: Uid: %s; DisplayName: %s; PhotoUrl: %s;  ProviderId: %s;Email: %s",
                            firebaseAuth.getCurrentUser().getUid(),
                            firebaseAuth.getCurrentUser().getDisplayName(),
                            firebaseAuth.getCurrentUser().getPhotoUrl(),
                            firebaseAuth.getCurrentUser().getProviderId(),
                            firebaseAuth.getCurrentUser().getEmail()));
                    //lưu vào SharedPred các thông tin UID, DisplayName, PhotoUrl và đăng nhập bằng UID, token.
                    sentRequestGG(firebaseAuth);
                }
            }
        };

        btPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneLogin();
            }
        });

        context = this;
        FacebookSdk.sdkInitialize(getApplicationContext());
        initFaceBook();
        Typeface typefaceSlogan = Typeface.createFromAsset(getAssets(), "fonts/Inconsolata-Regular.ttf");
        Typeface typefaceAppname = Typeface.createFromAsset(getAssets(), "fonts/Nunito-Black.ttf");
        tv_slogan.setTypeface(typefaceSlogan);
        tv_appname.setTypeface(typefaceAppname);
        TextView textView = (TextView) btLoginGG.getChildAt(0);
        textView.setText("Tiếp tục với Google");
        textView.setPadding(0, 0, 10, 0);
        ggAction();
    }

    private void login(String username, String password) {
        UserService userService = NetContext.instance.create(UserService.class);
        MediaType jsonType = MediaType.parse("application/json");
        String loginJson = (new Gson().toJson(new UserRegisterResponseJson(username, password)));
        final RequestBody loginBody = RequestBody.create(jsonType, loginJson);
        // create call
        Call<Token> loginCall = userService.login(loginBody);
        loginCall.enqueue(new Callback<Token>() {

            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                //token = response.body().getToken();
                if(response.code() == 200){
                    Toast.makeText(LoginActivity.this,String.format("Response body: %s, Token: %s",response.code(),
                            response.body().getToken()),Toast.LENGTH_SHORT).show();
                    accessToken = response.body().getToken();
                    gotoMainActivity();
                } else {
                    Toast.makeText(LoginActivity.this, "Could not parse body", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sentRequestGG(FirebaseAuth firebaseAuth) {
        //cach dang nhap nay se lay Uid cua gmail lam current usser
        if (firebaseAuth.getCurrentUser() != null) {
            SharedPref.instance.putName(firebaseAuth.getCurrentUser().getDisplayName());
            SharedPref.instance.putPassword(firebaseAuth.getCurrentUser().getUid());
            SharedPref.instance.putUrlAvatar(String.valueOf(firebaseAuth.getCurrentUser().getPhotoUrl()));
            SharedPref.instance.putUser(firebaseAuth.getCurrentUser().getDisplayName());
            SharedPref.instance.putEmail(firebaseAuth.getCurrentUser().getEmail());
            username = firebaseAuth.getCurrentUser().getEmail();
            password = firebaseAuth.getCurrentUser().getUid();

            UserService userService = NetContext.instance.create(UserService.class);
            MediaType jsonType = MediaType.parse("application/json");
            String loginJson = (new Gson().toJson(new UserRegisterResponseJson(username, password)));
            final RequestBody loginBody = RequestBody.create(jsonType, loginJson);
            // create call
            Call<Token> loginCall = userService.postNewAccount(loginBody);
            loginCall.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    DBContext.instance.cleanCart();
                    SharedPref.instance.putCount(0);
                    //EventBus.getDefault().postSticky(new SentUserIdEvent(u.getId().get$oid()));
                    //Dang nhap 1 tai khoan moi hay tai khoan cu deu tra ve 307
                    if (response.code() == 307){
                        login(username, password);
                    } else {
                        Toast.makeText(LoginActivity.this, "Could not parse body", Toast.LENGTH_SHORT).show();
                    }
                }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi xác minh tài khoản google!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
            });
        }
    }


    private void initFaceBook() {
        bt_loginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(Profile.getCurrentProfile() == null){
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            SharedPref.instance.putName(currentProfile.getName());
                            SharedPref.instance.putUrlAvatar(String.valueOf(currentProfile.getProfilePictureUri(900,900)));
                            SharedPref.instance.putFacebook(String.valueOf(currentProfile.getLinkUri()));
                            Log.d(TAG,String.format("onCurrentProfileChanged:  Facebook: %s; Name: %s; FirstName: %s; MidleName: %s; Lastname: %s; URL_AVATA: %s; ID: %s"
                                    ,currentProfile.getLinkUri(), currentProfile.getName(), currentProfile.getFirstName(), currentProfile.getMiddleName(),
                                    currentProfile.getLastName(), currentProfile.getProfilePictureUri(90,90),currentProfile.getId()));
                            //Facebook: https://www.facebook.com/app_scoped_user_id/1285832128200687/; Name: Khánh Đình Phạm; FirstName: Phạm; MidleName: Đình; Lastname: Khánh; URL_AVATA: https://graph.facebook.com/1285832128200687/picture?height=30&width=30&migration_overrides=%7Boctober_2012%3Atrue%7D; ID: 1285832128200687
                            username = currentProfile.getId();
                            password = currentProfile.getId();
                            UserService userService = NetContext.instance.create(UserService.class);
                            MediaType jsonType = MediaType.parse("application/json");
                            String loginJson = (new Gson().toJson(new UserRegisterResponseJson(username, password)));
                            final RequestBody loginBody = RequestBody.create(jsonType, loginJson);
                            // create call
                            Call<Token> loginCall = userService.postNewAccount(loginBody);
                            loginCall.enqueue(new Callback<Token>() {
                                @Override
                                public void onResponse(Call<Token> call, Response<Token> response) {
                                    DBContext.instance.cleanCart();
                                    SharedPref.instance.putCount(0);
                                    //EventBus.getDefault().postSticky(new SentUserIdEvent(u.getId().get$oid()));
                                    //Dang nhap 1 tai khoan moi hay tai khoan cu deu tra ve 307
                                    if (response.code() == 307){
                                        login(username, password);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Could not parse body", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Token> call, Throwable t) {
                                    Toast.makeText(LoginActivity.this, "Lỗi xác minh tài khoản google!", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                            mProfileTracker.stopTracking();
                        }
                    };
                    //no need to call stackTracking on mProfileTracker
                    //because it is called by its constructor, internally.
                } else {
                    final Profile profile = Profile.getCurrentProfile();
                    SharedPref.instance.putName(profile.getName());
                    SharedPref.instance.putUrlAvatar(String.valueOf(profile.getProfilePictureUri(900, 900)));
                    SharedPref.instance.putFacebook(String.valueOf(profile.getLinkUri()));
                    Log.d(TAG,String.format("not null onCurrentProfileChanged:  Facebook: %s; Name: %s; FirstName: %s; MidleName: %s; Lastname: %s; URL_AVATA: %s; ID: %s"
                            ,profile.getLinkUri(), profile.getName(), profile.getFirstName(), profile.getMiddleName(),
                            profile.getLastName(), profile.getProfilePictureUri(30,30),profile.getId()));
                    username = profile.getId();
                    password = profile.getId();
                    UserService userService = NetContext.instance.create(UserService.class);
                    MediaType jsonType = MediaType.parse("application/json");
                    String loginJson = (new Gson().toJson(new UserRegisterResponseJson(username, password)));
                    final RequestBody loginBody = RequestBody.create(jsonType, loginJson);
                    // create call
                    Call<Token> loginCall = userService.postNewAccount(loginBody);
                    loginCall.enqueue(new Callback<Token>() {
                        @Override
                        public void onResponse(Call<Token> call, Response<Token> response) {
                            DBContext.instance.cleanCart();
                            SharedPref.instance.putCount(0);
                            //EventBus.getDefault().postSticky(new SentUserIdEvent(u.getId().get$oid()));
                            //Dang nhap 1 tai khoan moi hay tai khoan cu deu tra ve 307
                            if (response.code() == 307){
                                login(username, password);
                            } else {
                                Toast.makeText(LoginActivity.this, "Could not parse body", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Token> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Lỗi xác minh tài khoản google!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try{
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();
            //retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);
            Log.e("package name =",context.getApplicationContext().getPackageName());
            for (android.content.pm.Signature signature : packageInfo.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                Log.e("Key hash = ", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e2) {
            Log.e("No such an algorithm", e2.toString());
        } catch (Exception e3){
            Log.e("Exception",e3.toString());
        }
        return key;
    }

    private void ggAction() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(LoginActivity.this.getResources().getString(R.string.default_web_client_id))
                .requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this,"có lỗi xảy ra!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        btLoginGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGG();
            }
        });
    }

    private void signInGG() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    ProgressDialog progressDialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        progressDialog.show();
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                //Google sign in was success, authenticate with firebase
                Log.e(TAG, String.format("onActivityResults: %s", result));
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                //Google sign in fail update UI approperiately
                Log.e(TAG, String.format("onActivityResults: %s",result.getStatus()));
                Toast.makeText(this, "Loi dang nhap, thu lai sau", Toast.LENGTH_SHORT).show();
                //...
            }
        } else if(requestCode == APP_REQUEST_CODE){
            //Confirm that this response matches your request
            final AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if(loginResult.getError() != null){
                Log.d(TAG,loginResult.getError().getErrorType().getMessage());
            } else if(loginResult.wasCancelled()){
                Log.d(TAG,"Login canceled");
            } else {
                //Log.d(TAG,String.format("Request code, AccessToken: %s; Id: %s", loginResult.getAccessToken().getToken(), loginResult.getAccessToken().getAccountId()));
                if (loginResult.getAccessToken().getToken() != null){
                    getAccount();
                } else {
                    Log.d(TAG,String.format("AuthorizationCode:%s...", loginResult.getAuthorizationCode()));
                    //getAccount();
                }
                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
//                goToMyLoggedInActivity();
            }
            // Surface the result to your user in an appropriate way.
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode != APP_REQUEST_CODE && requestCode != RC_SIGN_IN){
            progressDialog.dismiss();
        }
    }

    private void getAccount() {
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                Log.d(TAG,String.format("Accountkit information: Email: %s, Id: %s, PhoneNumber: %s",account.getEmail(),account.getId(),
                        account.getPhoneNumber().toString()));
                SharedPref.instance.putPhoneNumber(account.getPhoneNumber().toString());
                SharedPref.instance.putEmail(account.getEmail());
                SharedPref.instance.putPassword(account.getId());
                username = SharedPref.instance.getPhoneNumber();
                password = SharedPref.instance.getPassword();
                username = account.getId();
                password = account.getId();
                UserService userService = NetContext.instance.create(UserService.class);
                MediaType jsonType = MediaType.parse("application/json");
                String loginJson = (new Gson().toJson(new UserRegisterResponseJson(username, password)));
                final RequestBody loginBody = RequestBody.create(jsonType, loginJson);
                // create call
                Call<Token> loginCall = userService.postNewAccount(loginBody);
                loginCall.enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        DBContext.instance.cleanCart();
                        SharedPref.instance.putCount(0);
                        //EventBus.getDefault().postSticky(new SentUserIdEvent(u.getId().get$oid()));
                        //Dang nhap 1 tai khoan moi hay tai khoan cu deu tra ve 307
                        if (response.code() == 307){
                            login(username, password);
                        } else {
                            Toast.makeText(LoginActivity.this, "Could not parse body", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Lỗi xác minh tài khoản google!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

            }

            @Override
            public void onError(final AccountKitError error) {
                Log.e(TAG,String.format("AccountKit Error: %s", error.toString()));
                progressDialog.dismiss();
                // Handle Error
            }
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "No internet connection. Offline using",
                                    Toast.LENGTH_SHORT).show();
                            gotoMainActivity();
                        }
                        // ...
                    }
                });
    }

    private void phoneLogin() {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        /*UIManager uiManager;
        uiManager = new SkinManager(CLASSIC, Color.GREEN,-1,SkinManager.Tint.BLACK,0.55D);
        configurationBuilder.setUIManager(uiManager);*/
        configurationBuilder.setDefaultCountryCode("+84");
        String[] whiteslist = new String[]{"VN"};

        configurationBuilder.setSMSWhitelist(whiteslist);
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());

        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                btLoginGG.setVisibility(View.GONE);
                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle("Lỗi");
                alertDialog.setMessage("Thiết bị của bạn không có Dịch vụ của Google. Vui lòng tiếp tục với tài khoản facebook của bạn ");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return false;
        }
        return true;
    }


    //Lấy Avatar
    public static URL extractFacebookIcon(String id) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL imageURL = new URL("http://graph.facebook.com/" + id
                    + "/picture?type=large");
            return imageURL;
        } catch (Throwable e) {
            return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    public void onChangeActivity(){
        progressDialog.dismiss();
        finish();
        Toast.makeText(context,"ĐĂNG NHẬP THÀNH CÔNG",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("name", SharedPref.instance.getName());
        i.putExtra("url_avatar", SharedPref.instance.getUrlAvatar());
        i.putExtra("user", SharedPref.instance.getUser());
        i.putExtra("facebook", SharedPref.instance.getFacebook());
        i.putExtra("email", SharedPref.instance.getEmail());
        i.putExtra("phone_number", SharedPref.instance.getPhoneNumber());
        startActivity(i);
    }

    private void gotoMainActivity(){
        SharedPref.instance.putToken(accessToken);
        SharedPref.instance.put(new UserRegisterResponseJson(username, password, accessToken));

        if(SharedPref.instance.getPhoneNumber() == null){
            dialogPhone.show();
            final EditText ed_std = (EditText) dialogPhone.findViewById(R.id.ed_sdt);
            final Button btn_update = (Button) dialogPhone.findViewById(R.id.btn_update);

            btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(LoginActivity.this,"UPDATE THÀNH CÔNG", Toast.LENGTH_SHORT).show();
                    SharedPref.instance.putPhoneNumber(ed_std.getText().toString());
                    onChangeActivity();
                }
            });
            Log.d(TAG,String.format("PHONE NUMBER: %s",ed_std.getText().toString()));
        } else {
            onChangeActivity();
        }
    }
}
