package com.raywenderlich.alltherages.adapter;

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
import com.raywenderlich.alltherages.adapter.viewholder.RageComicViewHolder;
import com.raywenderlich.alltherages.database.DBContext;
import com.raywenderlich.alltherages.database.model.RageComic;
import com.raywenderlich.alltherages.eventbus.RageComicBuy;
import com.raywenderlich.alltherages.eventbus.RageComicReturn;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by laptopTCC on 5/27/2017.
 */

public class RageComicAdapterFrance extends RecyclerView.Adapter<RageComicViewHolder> implements Filterable {
    private static String TAG = RageComicAdapterFrance.class.toString();
    @Override
    public RageComicViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_item_rage_comic,
                viewGroup,
                false);
        return new RageComicViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(RageComicViewHolder holder, int position) {
        //RageManager.instance.getRageManager();
        //get data at this position from realm
        final RageComic rageComic = DBContext.instance.getAllRageComic().get(position);
        //final String mName = rageComic.getName();
        if(rageComic != null) {
            holder.bindData(rageComic);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new RageComicReturn(rageComic));
                //EventBus.getDefault().post(new ChangeActivity());
                Log.d(TAG,"clickitem: %s"+rageComic.toString());
            }
        });
        holder.getBtn_buyrage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new RageComicBuy(rageComic));
            }
        });
    }

    @Override
    public int getItemCount() {
        return DBContext.instance.getAllRageComic().size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}