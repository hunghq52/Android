package com.raywenderlich.alltherages.adapter.viewholder;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.raywenderlich.alltherages.R;
import com.raywenderlich.alltherages.database.DBContext;
import com.raywenderlich.alltherages.database.model.RageComic;
import com.raywenderlich.alltherages.database.model.SingleOrder;
import com.raywenderlich.alltherages.eventbus.SendRequestEvent;
import com.raywenderlich.alltherages.eventbus.TypeRequestEvent;
import com.raywenderlich.alltherages.utils.InputFilterMinMax;
import com.raywenderlich.alltherages.utils.Utils;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by laptopTCC on 5/27/2017.
 */

public class RageNotShipViewHolder extends RecyclerView.ViewHolder{
    private static String TAG = RageNotShipViewHolder.class.toString();
    //ns là kí hiệu fragment RageNotShip
    @BindView(R.id.iv_rage) ImageView
            nsRageImage;
    @BindView(R.id.tv_name) TextView
            nsName;
    @BindView(R.id.tv_price) TextView
            nsTvPrice;
    @BindView(R.id.btn_giam_sl) ImageView
            nsBtnGiam;
    @BindView(R.id.btn_tang_sl) ImageView
            nsBtnTang;
    @BindView(R.id.ed_sl_rage) EditText
            edTvSl;
    @BindView(R.id.iv_delete) ImageView
            iv_delete;

    SingleOrder singleOrder;

    Activity activity;

    public IBinder getWindowToken(){
        return edTvSl.getWindowToken();
    }

    public RageNotShipViewHolder(View itemView, Activity activity) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.activity = activity;
    }

    public void setSingleOrder(SingleOrder singleOrder) {
        this.singleOrder = singleOrder;
    }

    public ImageView getIv_delete() {
        return iv_delete;
    }

    public void setIv_delete(ImageView imageView) {
        this.iv_delete = imageView;
    }

    public EditText getEdTvSl() {
        return edTvSl;
    }

    public void setEdTvSl(EditText edTvSl) {
        this.edTvSl = edTvSl;
    }

    public ImageView getNsBtnGiam() {
        return nsBtnGiam;
    }

    public ImageView getNsBtnTang() {
        return nsBtnTang;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void bindData(final SingleOrder singleOrder){
        RageComic rageComic = singleOrder.getRageComic();
        Picasso.with(itemView.getContext()).load(rageComic.getUrl()).into(nsRageImage);
        nsName.setText(rageComic.getName());
        nsTvPrice.setText(String.format("%s %s",Utils.getPrice(rageComic.getNew_price()),"VND"));
        edTvSl.setFilters(new InputFilter[]{new InputFilterMinMax("1","10")});
        edTvSl.setText(String.format("%d",singleOrder.getCount()));
        edTvSl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handler = false;
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    doneEdit(singleOrder);
                    //Bắn sự kiện hide keyboard trong fragment
                    //EventBus.getDefault().post(new SendRequestEvent(TypeRequestEvent.HIDE_INPUT, getWindowToken()));
                    imm.hideSoftInputFromWindow(edTvSl.getWindowToken(), 0);
                    handler = true;
                }
                return handler;
            }
        });
    }

    private void doneEdit(SingleOrder singleOrder) {
        int sl = Integer.parseInt(edTvSl.getText().toString());
        //2. Lưu số lượng vào database
        DBContext.instance.changeQuantity(singleOrder,sl);
        EventBus.getDefault().post(new SendRequestEvent(singleOrder, TypeRequestEvent.CHANGE_PAYMENT));
    }
}
