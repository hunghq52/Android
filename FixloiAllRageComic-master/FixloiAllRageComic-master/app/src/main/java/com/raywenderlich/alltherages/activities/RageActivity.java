package com.raywenderlich.alltherages.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.raywenderlich.alltherages.R;
import com.raywenderlich.alltherages.database.model.RageComic;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RageActivity extends AppCompatActivity {
    private static String TAG = RageActivity.class.toString();
    @BindView(R.id.tv_name_detail)
    TextView name;
    @BindView(R.id.im_comic_detail)
    ImageView imageView;
    @BindView(R.id.tv_description)
    TextView description;
    @BindView(R.id.bt_buy)
    Button bt_buy;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    public static RageComic rageComic;

    public RageComic getRageComic() {
        return rageComic;
    }

    public void setRageComic(RageComic rageComic) {
        this.rageComic = rageComic;
    }
    Dialog dialog_buy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rage);
        ButterKnife.bind(this);
        dialog_buy = new Dialog(this);
        dialog_buy.setContentView(R.layout.dialog_buy);
        dialog_buy.setTitle("BUY RAGE");
        setupUI();
    }

    @Override
    public void onStart(){
        super.onStart();
        //EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        //EventBus.getDefault().unregister(this);
    }

    private void setupUI() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });
        String mName = rageComic.getName();
        name.setText(mName);
        Log.d(TAG,String.format("Url comic:%s",rageComic.getUrl()));
        Picasso.with(this).load(rageComic.getUrl()).into(imageView);
        String mDescription = rageComic.getDescription();
        description.setText(mDescription);

        bt_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_buy.show();
            }
        });
    }

    public void onBack(){
        finish();
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    /*@Subscribe (sticky = true,threadMode = ThreadMode.MAIN)
    public void getRageComic(RageComicReturn rageComicReturn){
    rageComic = rageComicReturn.getRageComic();
    }*/
}
