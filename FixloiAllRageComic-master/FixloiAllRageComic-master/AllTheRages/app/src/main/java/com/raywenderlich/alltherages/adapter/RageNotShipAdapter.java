package com.raywenderlich.alltherages.adapter;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.raywenderlich.alltherages.R;
import com.raywenderlich.alltherages.adapter.viewholder.RageNotShipViewHolder;
import com.raywenderlich.alltherages.database.DBContext;
import com.raywenderlich.alltherages.database.model.SingleOrder;
import com.raywenderlich.alltherages.eventbus.SendRequestEvent;
import com.raywenderlich.alltherages.eventbus.TypeRequestEvent;
import com.raywenderlich.alltherages.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by laptopTCC on 5/27/2017.
 */

public class RageNotShipAdapter extends RecyclerView.Adapter<RageNotShipViewHolder> implements Filterable {
    private static String TAG = RageNotShipAdapter.class.toString();
    Activity activity;

    public RageNotShipAdapter(Activity activity){
        this.activity = activity;
    }

    @Override
    public RageNotShipViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_order_detail,
                viewGroup,
                false);
        return new RageNotShipViewHolder(view,activity);
        //FoodInCardAdapter
    }

    List<SingleOrder> list;
    public void changeData(List<SingleOrder> list) {
        this.list = list;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(final RageNotShipViewHolder holder, int position) {
        //RageManager.instance.getRageManager();
        //get data at this position from realm
        final SingleOrder singleOrder = list.get(position);
        //Log.d(TAG,String.format("allSingleOrder: %s",list.toString()));
        final int sl = singleOrder.getCount();
        //final String mName = rageComic.getName();
        if(singleOrder != null) {
            holder.bindData(singleOrder);
        }

        holder.getNsBtnTang().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do tăng
                int newsl = sl+1;
                if (!Utils.checkQuantity(newsl)){
                    //Bắn event thông báo số lượng sai để sang bên ShipFragment bắt và Toast
                    EventBus.getDefault().post(new SendRequestEvent(singleOrder, TypeRequestEvent.QUANTITY_ERROR));
                    Log.d(TAG,String.format("Quantity Error:"));
                } else {
                    DBContext.instance.changeQuantity(singleOrder, newsl);
                    holder.getEdTvSl().setText("" + newsl);
                    Log.d(TAG, String.format("So luong SingleOrder: %d", newsl));
                    notifyDataSetChanged();
                    EventBus.getDefault().post(new SendRequestEvent(singleOrder, TypeRequestEvent.CHANGE_PAYMENT));
                }
            }
        });

        holder.getNsBtnGiam().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do giam
                int newsl = sl-1;
                if (!Utils.checkQuantity(newsl)){
                    //Do something
                    EventBus.getDefault().post(new SendRequestEvent(singleOrder, TypeRequestEvent.QUANTITY_ERROR));
                } else {
                    DBContext.instance.changeQuantity(singleOrder, newsl);
                    holder.getEdTvSl().setText("" + newsl);
                    Log.d(TAG, String.format("So luong SingleOrder: %d", newsl));
                    notifyDataSetChanged();
                    EventBus.getDefault().post(new SendRequestEvent(singleOrder, TypeRequestEvent.CHANGE_PAYMENT));
                }
            }
        });

        holder.getIv_delete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SendRequestEvent(singleOrder,TypeRequestEvent.DELETE));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}