package com.raywenderlich.alltherages.adapter.viewholder;

import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.raywenderlich.alltherages.R;
import com.raywenderlich.alltherages.database.model.RageComic;
import com.raywenderlich.alltherages.utils.Utils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by laptopTCC on 5/27/2017.
 */

public class RageComicViewHolder extends RecyclerView.ViewHolder{
    private static String TAG = RageComicViewHolder.class.toString();
    @BindView(R.id.im_comic) ImageView
    mImageView;
    @BindView(R.id.tv_name) TextView
    name;
    @BindView(R.id.tv_price) TextView
    price;
    @BindView(R.id.tv_price_old) TextView
    price_old;
    @BindView(R.id.rt_rate) RatingBar
    rate;
    @BindView(R.id.btn_buyrage) ImageButton
    btn_buyrage;
    @BindView(R.id.tv_percent) TextView
    percent;

    public ImageButton getBtn_buyrage() {
        return btn_buyrage;
    }

    public RageComicViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void bindData(final RageComic rageComic){
        Picasso.with(itemView.getContext()).load(rageComic.getUrl()).into(mImageView);
        name.setText(rageComic.getName());
        price.setText(String.format("%s %s",Utils.getPrice(rageComic.getNew_price()),"VND"));
        price_old.setText(String.format("%s %s",Utils.getPrice(rageComic.getOld_price()),"VND"));
        price_old.setPaintFlags(price_old.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        percent.setText(String.format("%s %s",Utils.getPercent(rageComic.getDiscount_rate()),"%"));
        rate.setRating(4);
    }
}
