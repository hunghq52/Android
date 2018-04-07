package com.raywenderlich.alltherages.adapter.viewholder;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.raywenderlich.alltherages.R;
import com.raywenderlich.alltherages.database.model.SingleOrder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by laptopTCC on 5/27/2017.
 */

public class RageOriginProductViewHolder extends RecyclerView.ViewHolder{
    private static String TAG = RageOriginProductViewHolder.class.toString();
    //ns là kí hiệu fragment RageNotShip
    @BindView(R.id.tv_type_comic) TextView
    tvTypeComic;
    @BindView(R.id.tv_them) TextView
    tvThem;
    @BindView(R.id.recycler_view) RecyclerView
    rvType;


    public RageOriginProductViewHolder(View itemView, Activity activity) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void bindData(final SingleOrder singleOrder){

    }
}
